package org.scribe.model;

import org.scribe.exceptions.*;
import org.scribe.utils.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.util.concurrent.*;

class Request
{
    private static final String CONTENT_LENGTH = "Content-Length";
    private String url;
    private Verb verb;
    private Map<String, String> querystringParams;
    private Map<String, String> bodyParams;
    private Map<String, String> headers;
    private String payload;
    private HttpURLConnection connection;
    private String charset;
    private byte[] bytePayload;
    private boolean connectionKeepAlive;
    
    public Request(final Verb verb, final String url) {
        this.payload = null;
        this.bytePayload = null;
        this.connectionKeepAlive = false;
        this.verb = verb;
        this.url = url;
        this.querystringParams = new HashMap<String, String>();
        this.bodyParams = new HashMap<String, String>();
        this.headers = new HashMap<String, String>();
    }
    
    public Response send() {
        try {
            this.createConnection();
            return this.doSend();
        }
        catch (IOException ioe) {
            throw new OAuthException("Problems while creating connection", ioe);
        }
    }
    
    private void createConnection() throws IOException {
        final String effectiveUrl = URLUtils.appendParametersToQueryString(this.url, this.querystringParams);
        if (this.connection == null) {
            System.setProperty("http.keepAlive", this.connectionKeepAlive ? "true" : "false");
            this.connection = (HttpURLConnection)new URL(effectiveUrl).openConnection();
        }
    }
    
    Response doSend() throws IOException {
        this.connection.setRequestMethod(this.verb.name());
        this.addHeaders(this.connection);
        if (this.verb.equals(Verb.PUT) || this.verb.equals(Verb.POST)) {
            this.addBody(this.connection, this.getByteBodyContents());
        }
        return new Response(this.connection);
    }
    
    void addHeaders(final HttpURLConnection conn) {
        for (final String key : this.headers.keySet()) {
            conn.setRequestProperty(key, this.headers.get(key));
        }
    }
    
    void addBody(final HttpURLConnection conn, final byte[] content) throws IOException {
        conn.setRequestProperty("Content-Length", String.valueOf(content.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(content);
    }
    
    public void addHeader(final String key, final String value) {
        this.headers.put(key, value);
    }
    
    public void addBodyParameter(final String key, final String value) {
        this.bodyParams.put(key, value);
    }
    
    public void addQuerystringParameter(final String key, final String value) {
        this.querystringParams.put(key, value);
    }
    
    public void addPayload(final String payload) {
        this.payload = payload;
    }
    
    public void addPayload(final byte[] payload) {
        this.bytePayload = payload;
    }
    
    public Map<String, String> getQueryStringParams() {
        try {
            final Map<String, String> params = new HashMap<String, String>();
            final String queryString = new URL(this.url).getQuery();
            params.putAll(URLUtils.queryStringToMap(queryString));
            params.putAll(this.querystringParams);
            return params;
        }
        catch (MalformedURLException mue) {
            throw new OAuthException("Malformed URL", mue);
        }
    }
    
    public Map<String, String> getBodyParams() {
        return this.bodyParams;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public String getSanitizedUrl() {
        return this.url.replaceAll("\\?.*", "").replace("\\:\\d{4}", "");
    }
    
    public String getBodyContents() {
        try {
            return new String(this.getByteBodyContents(), this.getCharset());
        }
        catch (UnsupportedEncodingException uee) {
            throw new OAuthException("Unsupported Charset: " + this.charset, uee);
        }
    }
    
    byte[] getByteBodyContents() {
        if (this.bytePayload != null) {
            return this.bytePayload;
        }
        final String body = (this.payload != null) ? this.payload : URLUtils.formURLEncodeMap(this.bodyParams);
        try {
            return body.getBytes(this.getCharset());
        }
        catch (UnsupportedEncodingException uee) {
            throw new OAuthException("Unsupported Charset: " + this.getCharset(), uee);
        }
    }
    
    public Verb getVerb() {
        return this.verb;
    }
    
    public Map<String, String> getHeaders() {
        return this.headers;
    }
    
    public String getCharset() {
        return (this.charset == null) ? Charset.defaultCharset().name() : this.charset;
    }
    
    public void setConnectTimeout(final int duration, final TimeUnit unit) {
        this.connection.setConnectTimeout((int)unit.toMillis(duration));
    }
    
    public void setReadTimeout(final int duration, final TimeUnit unit) {
        this.connection.setReadTimeout((int)unit.toMillis(duration));
    }
    
    public void setCharset(final String charsetName) {
        this.charset = charsetName;
    }
    
    public void setConnectionKeepAlive(final boolean connectionKeepAlive) {
        this.connectionKeepAlive = connectionKeepAlive;
    }
    
    void setConnection(final HttpURLConnection connection) {
        this.connection = connection;
    }
    
    @Override
    public String toString() {
        return String.format("@Request(%s %s)", this.getVerb(), this.getUrl());
    }
}
