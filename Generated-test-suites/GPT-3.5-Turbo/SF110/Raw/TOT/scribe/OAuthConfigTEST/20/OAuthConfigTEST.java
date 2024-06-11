
package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;

public class OAuthConfigTest {
    
    // Test cases for constructor with two parameters
    @Test
    public void testConstructor_TwoParameters() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertEquals("key", config.getApiKey());
        Assert.assertEquals("secret", config.getApiSecret());
        Assert.assertNull(config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
        Assert.assertNull(config.getScope());
        Assert.assertFalse(config.hasScope());
    }
    
    // Test cases for constructor with five parameters
    @Test
    public void testConstructor_FiveParameters() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        Assert.assertEquals("key", config.getApiKey());
        Assert.assertEquals("secret", config.getApiSecret());
        Assert.assertEquals("callback", config.getCallback());
        Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
        Assert.assertEquals("scope", config.getScope());
        Assert.assertTrue(config.hasScope());
    }
    
    // Test case for getApiKey()
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertEquals("key", config.getApiKey());
    }
    
    // Test case for getApiSecret()
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertEquals("secret", config.getApiSecret());
    }
    
    // Test case for getCallback() with null callback
    @Test
    public void testGetCallback_NullCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertNull(config.getCallback());
    }
    
    // Test case for getCallback() with non-null callback
    @Test
    public void testGetCallback_NonNullCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertEquals("callback", config.getCallback());
    }
    
    // Test case for getSignatureType() with null signature type
    @Test
    public void testGetSignatureType_NullSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    // Test case for getSignatureType() with non-null signature type
    @Test
    public void testGetSignatureType_NonNullSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
    }
    
    // Test case for getScope() with null scope
    @Test
    public void testGetScope_NullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertNull(config.getScope());
    }
    
    // Test case for getScope() with non-null scope
    @Test
    public void testGetScope_NonNullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertEquals("scope", config.getScope());
    }
    
    // Test case for hasScope() with null scope
    @Test
    public void testHasScope_NullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertFalse(config.hasScope());
    }
    
    // Test case for hasScope() with non-null scope
    @Test
    public void testHasScope_NonNullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertTrue(config.hasScope());
    }
}
