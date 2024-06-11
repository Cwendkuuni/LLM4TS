import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OAuthConfigTest {
    // Test cases for constructors
    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }

    // Test cases for getApiKey()
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("key", config.getApiKey());
    }

    // Test cases for getApiSecret()
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("secret", config.getApiSecret());
    }

    // Test cases for getCallback()
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("oob", config.getCallback());
    }

    // Test cases for getSignatureType()
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }

    // Test cases for getScope()
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("scope", config.getScope());
    }

    // Test cases for hasScope()
    @Test
    public void testHasScopeWithScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertTrue(config.hasScope());
    }

    @Test
    public void testHasScopeWithoutScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertFalse(config.hasScope());
    }
}