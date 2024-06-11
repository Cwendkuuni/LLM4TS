
package org.scribe.model;

import java.net.*;
import java.io.*;
import org.scribe.utils.*;
import java.util.*;

public class Response
{
    private static final String EMPTY = "";
    private int code;
    private String body;
    private InputStream stream;
    private Map<String, String> headers;
    
    Response(final HttpURLConnection connection) throws IOException {
        try {
            connection.connect();
            this.code = connection.getResponseCode();
            this.headers = this.parseHeaders(connection);
            this.stream = (this.wasSuccessful() ? connection.getInputStream() : connection.getErrorStream());
        }
        catch (UnknownHostException e) {
            this.code = 404;
            this.body = "";
        }
    }
    
    private String parseBodyContents() {
        return this.body = StreamUtils.getStreamContents(this.getStream());
    }
    
    private Map<String, String> parseHeaders(final HttpURLConnection conn) {
        final Map<String, String> headers = new HashMap<String, String>();
        for (final String key : conn.getHeaderFields().keySet()) {
            headers.put(key, conn.getHeaderFields().get(key).get(0));
        }
        return headers;
    }
    
    private boolean wasSuccessful() {
        return this.getCode() >= 200 && this.getCode() < 400;
    }
    
    public String getBody() {
        return (this.body != null) ? this.body : this.parseBodyContents();
    }
    
    public InputStream getStream() {
        return this.stream;
    }
    
    public int getCode() {
        return this.code;
    }
    
    public Map<String, String> getHeaders() {
        return this.headers;
    }
    
    public String getHeader(final String name) {
        return this.headers.get(name);
    }
}
