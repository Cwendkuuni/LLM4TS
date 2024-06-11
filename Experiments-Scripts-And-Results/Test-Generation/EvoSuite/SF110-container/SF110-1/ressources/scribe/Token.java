package org.scribe.model;

import java.io.*;

public class Token implements Serializable
{
    private static final long serialVersionUID = 715000866082812683L;
    private final String token;
    private final String secret;
    private final String rawResponse;
    
    public Token(final String token, final String secret) {
        this(token, secret, null);
    }
    
    public Token(final String token, final String secret, final String rawResponse) {
        this.token = token;
        this.secret = secret;
        this.rawResponse = rawResponse;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public String getSecret() {
        return this.secret;
    }
    
    public String getRawResponse() {
        if (this.rawResponse == null) {
            throw new IllegalStateException("This token object was not constructed by scribe and does not have a rawResponse");
        }
        return this.rawResponse;
    }
    
    @Override
    public String toString() {
        return String.format("Token[%s , %s]", this.token, this.secret);
    }
}
