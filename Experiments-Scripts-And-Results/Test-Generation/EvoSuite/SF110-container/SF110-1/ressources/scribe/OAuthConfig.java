package org.scribe.model;

public class OAuthConfig
{
    private final String apiKey;
    private final String apiSecret;
    private final String callback;
    private final SignatureType signatureType;
    private final String scope;
    
    public OAuthConfig(final String key, final String secret) {
        this(key, secret, null, null, null);
    }
    
    public OAuthConfig(final String key, final String secret, final String callback, final SignatureType type, final String scope) {
        this.apiKey = key;
        this.apiSecret = secret;
        this.callback = ((callback != null) ? callback : "oob");
        this.signatureType = ((type != null) ? type : SignatureType.Header);
        this.scope = scope;
    }
    
    public String getApiKey() {
        return this.apiKey;
    }
    
    public String getApiSecret() {
        return this.apiSecret;
    }
    
    public String getCallback() {
        return this.callback;
    }
    
    public SignatureType getSignatureType() {
        return this.signatureType;
    }
    
    public String getScope() {
        return this.scope;
    }
    
    public boolean hasScope() {
        return this.scope != null;
    }
}
