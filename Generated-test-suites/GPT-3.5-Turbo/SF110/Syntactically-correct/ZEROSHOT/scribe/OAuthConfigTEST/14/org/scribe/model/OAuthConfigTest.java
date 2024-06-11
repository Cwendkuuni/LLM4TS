package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        String key = "apiKey";
        OAuthConfig config = new OAuthConfig(key, "apiSecret");
        Assert.assertEquals(key, config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        String secret = "apiSecret";
        OAuthConfig config = new OAuthConfig("apiKey", secret);
        Assert.assertEquals(secret, config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        String callback = "callback";
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", callback, SignatureType.Header, "scope");
        Assert.assertEquals(callback, config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        SignatureType signatureType = SignatureType.Body;
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", signatureType, "scope");
        Assert.assertEquals(signatureType, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        String scope = "scope";
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, scope);
        Assert.assertEquals(scope, config.getScope());
    }

    @Test
    public void testHasScope_WithScope() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "scope");
        Assert.assertTrue(config.hasScope());
    }

    @Test
    public void testHasScope_WithoutScope() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
        Assert.assertFalse(config.hasScope());
    }
}