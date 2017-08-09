package org.jboss.as.quickstarts.jsf;

import java.io.IOException;
import java.util.UUID;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;

public class CSRFPortletFilter implements javax.portlet.filter.RenderFilter, javax.portlet.filter.ActionFilter {
    private FilterConfig filterConfig = null;
    
    public static final String CSRFTOKEN_NAME = "CSRFTOKEN_NAME";


    public void doFilter(RenderRequest renderRequest,
                    RenderResponse renderResponse, FilterChain filterChain)
                    throws IOException, PortletException {

            System.out.println("#### Before RenderRequest : " + renderRequest.getContextPath());
            
            PortletSession session = renderRequest.getPortletSession();
            
            if (session.getAttribute(CSRFTOKEN_NAME) == null) {
                // first access
                String randomId = UUID.randomUUID().toString();
                System.out.println("randomId = "+randomId);
                session.setAttribute(CSRFTOKEN_NAME, randomId);
            }
            
            filterChain.doFilter(renderRequest, renderResponse);
            
            System.out.println("#### After RenderRequest");
    }

    public void doFilter(ActionRequest actionRequest, ActionResponse actionResponse,
                    FilterChain filterChain) throws IOException, PortletException {
            System.out.println("#### Before ActionRequest : " + actionRequest.getContextPath());
 
            PortletSession session = actionRequest.getPortletSession();

            String token = actionRequest.getParameter("CSRFTOKEN_NAME");
            if (token == null) {
                System.out.println("token is null");
                throw new PortletException("token is null");
            } else if (!token.equals(session.getAttribute(CSRFTOKEN_NAME))) {
                System.out.println("token doesn't match");
                throw new PortletException("token doesn't match");
            } else {
                System.out.println("token matches");
            }
            
            filterChain.doFilter(actionRequest, actionResponse);
            
            System.out.println("#### After ActionRequest");
    }

    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws PortletException {
            System.out.println("#### PORTLET FILTER INIT ####");
            this.filterConfig = filterConfig;
    }

}