package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;

public class OAuthConfigTest {
    
    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertEquals("key", config.getApiKey());
        Assert.assertEquals("secret", config.getApiSecret());
        Assert.assertEquals("oob", config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
        Assert.assertNull(config.getScope());
        Assert.assertFalse(config.hasScope());
    }
    
    @Test
    public void testConstructorWithKeySecretCallbackTypeAndScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertEquals("key", config.getApiKey());
        Assert.assertEquals("secret", config.getApiSecret());
        Assert.assertEquals("callback", config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
        Assert.assertEquals("scope", config.getScope());
        Assert.assertTrue(config.hasScope());
    }
    
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertEquals("key", config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertEquals("secret", config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertEquals("callback", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertEquals("scope", config.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig configWithoutScope = new OAuthConfig("key", "secret");
        Assert.assertFalse(configWithoutScope.hasScope());
        
        OAuthConfig configWithScope = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertTrue(configWithScope.hasScope());
    }
}