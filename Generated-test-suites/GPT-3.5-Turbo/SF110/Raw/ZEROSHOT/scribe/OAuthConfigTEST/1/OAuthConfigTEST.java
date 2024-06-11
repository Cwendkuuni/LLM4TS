package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        String key = "testKey";
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        
        Assert.assertEquals(key, config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        String key = "testKey";
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        
        Assert.assertEquals(secret, config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        String key = "testKey";
        String secret = "testSecret";
        String callback = "testCallback";
        OAuthConfig config = new OAuthConfig(key, secret, callback, null, null);
        
        Assert.assertEquals(callback, config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        String key = "testKey";
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        String key = "testKey";
        String secret = "testSecret";
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(key, secret, null, null, scope);
        
        Assert.assertEquals(scope, config.getScope());
    }
    
    @Test
    public void testHasScope() {
        String key = "testKey";
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        
        Assert.assertFalse(config.hasScope());
    }
}