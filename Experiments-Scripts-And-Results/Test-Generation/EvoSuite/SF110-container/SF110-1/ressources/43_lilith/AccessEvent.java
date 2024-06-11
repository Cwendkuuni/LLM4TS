package de.huxhorn.lilith.data.access;

import java.io.*;
import java.util.*;

public class AccessEvent implements Serializable
{
    private static final long serialVersionUID = -716078283933754505L;
    private Long timeStamp;
    private LoggerContext loggerContext;
    private String requestURI;
    private String requestURL;
    private String remoteHost;
    private String remoteUser;
    private String protocol;
    private String method;
    private String serverName;
    private String remoteAddress;
    private Map<String, String> requestHeaders;
    private Map<String, String> responseHeaders;
    private Map<String, String[]> requestParameters;
    private int localPort;
    private int statusCode;
    
    public Long getTimeStamp() {
        return this.timeStamp;
    }
    
    public void setTimeStamp(final Long timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public LoggerContext getLoggerContext() {
        return this.loggerContext;
    }
    
    public void setLoggerContext(final LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
    }
    
    public String getRequestURI() {
        return this.requestURI;
    }
    
    public void setRequestURI(final String requestURI) {
        this.requestURI = requestURI;
    }
    
    public String getRequestURL() {
        return this.requestURL;
    }
    
    public void setRequestURL(final String requestURL) {
        this.requestURL = requestURL;
    }
    
    public String getRemoteHost() {
        return this.remoteHost;
    }
    
    public void setRemoteHost(final String remoteHost) {
        this.remoteHost = remoteHost;
    }
    
    public String getRemoteUser() {
        return this.remoteUser;
    }
    
    public void setRemoteUser(final String remoteUser) {
        this.remoteUser = remoteUser;
    }
    
    public String getProtocol() {
        return this.protocol;
    }
    
    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }
    
    public String getMethod() {
        return this.method;
    }
    
    public void setMethod(final String method) {
        this.method = method;
    }
    
    public String getServerName() {
        return this.serverName;
    }
    
    public void setServerName(final String serverName) {
        this.serverName = serverName;
    }
    
    public String getRemoteAddress() {
        return this.remoteAddress;
    }
    
    public void setRemoteAddress(final String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
    
    public Map<String, String> getRequestHeaders() {
        return this.requestHeaders;
    }
    
    public void setRequestHeaders(final Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }
    
    public Map<String, String> getResponseHeaders() {
        return this.responseHeaders;
    }
    
    public void setResponseHeaders(final Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }
    
    public Map<String, String[]> getRequestParameters() {
        return this.requestParameters;
    }
    
    public void setRequestParameters(final Map<String, String[]> requestParameters) {
        this.requestParameters = requestParameters;
    }
    
    public int getLocalPort() {
        return this.localPort;
    }
    
    public void setLocalPort(final int localPort) {
        this.localPort = localPort;
    }
    
    public int getStatusCode() {
        return this.statusCode;
    }
    
    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AccessEvent event = (AccessEvent)o;
        if (this.localPort != event.localPort) {
            return false;
        }
        if (this.statusCode != event.statusCode) {
            return false;
        }
        Label_0088: {
            if (this.loggerContext != null) {
                if (this.loggerContext.equals(event.loggerContext)) {
                    break Label_0088;
                }
            }
            else if (event.loggerContext == null) {
                break Label_0088;
            }
            return false;
        }
        Label_0121: {
            if (this.method != null) {
                if (this.method.equals(event.method)) {
                    break Label_0121;
                }
            }
            else if (event.method == null) {
                break Label_0121;
            }
            return false;
        }
        Label_0154: {
            if (this.protocol != null) {
                if (this.protocol.equals(event.protocol)) {
                    break Label_0154;
                }
            }
            else if (event.protocol == null) {
                break Label_0154;
            }
            return false;
        }
        Label_0187: {
            if (this.remoteAddress != null) {
                if (this.remoteAddress.equals(event.remoteAddress)) {
                    break Label_0187;
                }
            }
            else if (event.remoteAddress == null) {
                break Label_0187;
            }
            return false;
        }
        Label_0220: {
            if (this.remoteHost != null) {
                if (this.remoteHost.equals(event.remoteHost)) {
                    break Label_0220;
                }
            }
            else if (event.remoteHost == null) {
                break Label_0220;
            }
            return false;
        }
        Label_0253: {
            if (this.remoteUser != null) {
                if (this.remoteUser.equals(event.remoteUser)) {
                    break Label_0253;
                }
            }
            else if (event.remoteUser == null) {
                break Label_0253;
            }
            return false;
        }
        Label_0286: {
            if (this.requestHeaders != null) {
                if (this.requestHeaders.equals(event.requestHeaders)) {
                    break Label_0286;
                }
            }
            else if (event.requestHeaders == null) {
                break Label_0286;
            }
            return false;
        }
        Label_0319: {
            if (this.requestURI != null) {
                if (this.requestURI.equals(event.requestURI)) {
                    break Label_0319;
                }
            }
            else if (event.requestURI == null) {
                break Label_0319;
            }
            return false;
        }
        Label_0352: {
            if (this.requestURL != null) {
                if (this.requestURL.equals(event.requestURL)) {
                    break Label_0352;
                }
            }
            else if (event.requestURL == null) {
                break Label_0352;
            }
            return false;
        }
        Label_0385: {
            if (this.responseHeaders != null) {
                if (this.responseHeaders.equals(event.responseHeaders)) {
                    break Label_0385;
                }
            }
            else if (event.responseHeaders == null) {
                break Label_0385;
            }
            return false;
        }
        Label_0418: {
            if (this.serverName != null) {
                if (this.serverName.equals(event.serverName)) {
                    break Label_0418;
                }
            }
            else if (event.serverName == null) {
                break Label_0418;
            }
            return false;
        }
        if (this.timeStamp != null) {
            if (!this.timeStamp.equals(event.timeStamp)) {
                return false;
            }
        }
        else if (event.timeStamp != null) {
            return false;
        }
        return true;
        b = false;
        return b;
    }
    
    @Override
    public int hashCode() {
        int result = (this.timeStamp != null) ? this.timeStamp.hashCode() : 0;
        result = 31 * result + ((this.loggerContext != null) ? this.loggerContext.hashCode() : 0);
        result = 31 * result + ((this.requestURI != null) ? this.requestURI.hashCode() : 0);
        result = 31 * result + ((this.requestURL != null) ? this.requestURL.hashCode() : 0);
        result = 31 * result + ((this.remoteHost != null) ? this.remoteHost.hashCode() : 0);
        result = 31 * result + ((this.remoteUser != null) ? this.remoteUser.hashCode() : 0);
        result = 31 * result + ((this.protocol != null) ? this.protocol.hashCode() : 0);
        result = 31 * result + ((this.method != null) ? this.method.hashCode() : 0);
        result = 31 * result + ((this.serverName != null) ? this.serverName.hashCode() : 0);
        result = 31 * result + ((this.remoteAddress != null) ? this.remoteAddress.hashCode() : 0);
        result = 31 * result + this.localPort;
        result = 31 * result + this.statusCode;
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("AccessEvent[");
        result.append("loggerContext=").append(this.loggerContext).append(", ");
        result.append("timeStamp=").append(this.timeStamp);
        result.append("]");
        return result.toString();
    }
}
