package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertNotNull(config);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertFalse(config.hasScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertNotNull(config);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("key", config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("secret", config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("callback", config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals(SignatureType.QueryString, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("scope", config.getScope());
    }

    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertTrue(config.hasScope());
    }

}