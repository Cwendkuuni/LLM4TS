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
        assertEquals("oob", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("key", oauthConfig.getApiKey());
        assertEquals("secret", oauthConfig.getApiSecret());
        assertEquals("callback", oauthConfig.getCallback());
        assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
        assertEquals("scope", oauthConfig.getScope());
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
        assertEquals("oob", oauthConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        assertFalse(oauthConfig.hasScope());
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertTrue(oauthConfig.hasScope());
    }
}