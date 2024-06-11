import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import static org.junit.Assert.*;

public class OAuthConfigTest {

    OAuthConfig oauthConfig;

    @Before
    public void setup() {
        oauthConfig = new OAuthConfig("apiKey", "apiSecret");
    }

    @Test
    public void testConstructorWithKeyAndSecret() {
        assertEquals("apiKey", oauthConfig.getApiKey());
        assertEquals("apiSecret", oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
        assertEquals("apiKey", oauthConfig.getApiKey());
        assertEquals("apiSecret", oauthConfig.getApiSecret());
        assertEquals("callback", oauthConfig.getCallback());
        assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
        assertEquals("scope", oauthConfig.getScope());
    }

    @Test
    public void testGetApiKey() {
        assertEquals("apiKey", oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        assertEquals("apiSecret", oauthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        assertNull(oauthConfig.getCallback());
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
    public void testHasScopeWithScope() {
        assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testHasScopeWithoutScope() {
        oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, null);
        assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testHasScopeWithEmptyScope() {
        oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "");
        assertTrue(oauthConfig.hasScope());
    }
}