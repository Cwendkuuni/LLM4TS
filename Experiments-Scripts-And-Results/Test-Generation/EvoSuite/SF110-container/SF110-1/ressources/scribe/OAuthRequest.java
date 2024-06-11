package org.scribe.model;

import java.util.*;
import java.util.concurrent.*;

public class OAuthRequest extends Request
{
    private static final String OAUTH_PREFIX = "oauth_";
    private Map<String, String> oauthParameters;
    
    public OAuthRequest(final Verb verb, final String url) {
        super(verb, url);
        this.oauthParameters = new HashMap<String, String>();
    }
    
    public void addOAuthParameter(final String key, final String value) {
        this.oauthParameters.put(this.checkKey(key), value);
    }
    
    private String checkKey(final String key) {
        if (key.startsWith("oauth_") || key.equals("scope")) {
            return key;
        }
        throw new IllegalArgumentException(String.format("OAuth parameters must either be '%s' or start with '%s'", "scope", "oauth_"));
    }
    
    public Map<String, String> getOauthParameters() {
        return this.oauthParameters;
    }
    
    @Override
    public String toString() {
        return String.format("@OAuthRequest(%s, %s)", this.getVerb(), this.getUrl());
    }
}
