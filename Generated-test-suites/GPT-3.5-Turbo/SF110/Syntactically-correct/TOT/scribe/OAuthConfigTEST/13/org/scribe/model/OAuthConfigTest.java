package org.scribe.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    private OAuthConfig oauthConfig;
    
    @Before
    public void setUp() {
        oauthConfig = new OAuthConfig("key", "secret");
    }
    
    @Test
    public void testConstructorWithKeyAndSecret() {
        assertEquals("key", oauthConfig.getApiKey());
        assertEquals("secret", oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Query, "scope");
        assertEquals("key", oauthConfig.getApiKey());
        assertEquals("secret", oauthConfig.getApiSecret());
        assertEquals("callback", oauthConfig.getCallback());
        assertEquals(SignatureType.Query, oauthConfig.getSignatureType());
        assertEquals("scope", oauthConfig.getScope());
    }
    
    @Test
    public void testConstructorWithNullCallback() {
        oauthConfig = new OAuthConfig("key", "secret", null, SignatureType.Query, "scope");
        assertEquals("oob", oauthConfig.getCallback());
    }
    
    @Test
    public void testConstructorWithNullSignatureType() {
        oauthConfig = new OAuthConfig("key", "secret", "callback", null, "scope");
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }
    
    @Test
    public void testConstructorWithNullScope() {
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Query, null);
        assertNull(oauthConfig.getScope());
    }
    
    @Test
    public void testGetApiKey() {
        assertEquals("key", oauthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        assertEquals("secret", oauthConfig.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        assertNull(oauthConfig.getCallback());
    
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Query, "scope");
        assertEquals("callback", oauthConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Query, "scope");
        assertEquals(SignatureType.Query, oauthConfig.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        assertNull(oauthConfig.getScope());
    
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Query, "scope");
        assertEquals("scope", oauthConfig.getScope());
    }
    
    @Test
    public void testHasScope() {
        assertFalse(oauthConfig.hasScope());
    
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Query, "scope");
        assertTrue(oauthConfig.hasScope());
    }
}