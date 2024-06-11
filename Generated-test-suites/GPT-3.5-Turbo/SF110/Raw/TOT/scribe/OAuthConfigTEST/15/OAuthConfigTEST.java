package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");

        assertEquals("key", oauthConfig.getApiKey());
        assertEquals("secret", oauthConfig.getApiSecret());
        assertEquals("oob", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        assertEquals("key", oauthConfig.getApiKey());
        assertEquals("secret", oauthConfig.getApiSecret());
        assertEquals("callback", oauthConfig.getCallback());
        assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
        assertEquals("scope", oauthConfig.getScope());
        assertTrue(oauthConfig.hasScope());
    }
    
    @Test
    public void testGetApiKey() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");

        assertEquals("key", oauthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");

        assertEquals("secret", oauthConfig.getApiSecret());
    }
    
    @Test
    public void testGetCallbackWithNullCallback() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");

        assertEquals("oob", oauthConfig.getCallback());
    }
    
    @Test
    public void testGetCallbackWithNonNullCallback() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        assertEquals("callback", oauthConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureTypeWithNullType() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");

        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }
    
    @Test
    public void testGetSignatureTypeWithNonNullType() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
    }
    
    @Test
    public void testGetScopeWithNullScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");

        assertNull(oauthConfig.getScope());
    }
    
    @Test
    public void testGetScopeWithNonNullScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        assertEquals("scope", oauthConfig.getScope());
    }
    
    @Test
    public void testHasScopeWithNullScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");

        assertFalse(oauthConfig.hasScope());
    }
    
    @Test
    public void testHasScopeWithNonNullScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        assertTrue(oauthConfig.hasScope());
    }
}