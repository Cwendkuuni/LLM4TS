import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

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
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "customCallback", SignatureType.QueryString, "scope");
        assertEquals("key", oauthConfig.getApiKey());
        assertEquals("secret", oauthConfig.getApiSecret());
        assertEquals("customCallback", oauthConfig.getCallback());
        assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
        assertEquals("scope", oauthConfig.getScope());
    }

    @Test
    public void testGetCallbackWithCustomCallback() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "customCallback", SignatureType.QueryString, "scope");
        assertEquals("customCallback", oauthConfig.getCallback());
    }

    @Test
    public void testGetCallbackWithDefaultCallback() {
        assertEquals("oob", oauthConfig.getCallback());
    }

    @Test
    public void testGetSignatureTypeWithCustomSignatureType() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "customCallback", SignatureType.Path, "scope");
        assertEquals(SignatureType.Path, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetSignatureTypeWithDefaultSignatureType() {
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScopeWithCustomScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "customCallback", SignatureType.Path, "customScope");
        assertEquals("customScope", oauthConfig.getScope());
    }

    @Test
    public void testGetScopeWithNullScope() {
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testHasScopeWithCustomScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "customCallback", SignatureType.Path, "customScope");
        assertTrue(oauthConfig.hasScope());
    }

    @Test
    public void testHasScopeWithNullScope() {
        assertFalse(oauthConfig.hasScope());
    }
}