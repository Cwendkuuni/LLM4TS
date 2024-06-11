import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    private OAuthConfig oauthConfig;

    @Before
    public void setUp() {
        oauthConfig = new OAuthConfig("api_key", "api_secret");
    }

    @Test
    public void testGetApiKey() {
        assertEquals("api_key", oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        assertEquals("api_secret", oauthConfig.getApiSecret());
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
    }

    @Test
    public void testConstructorWithCallback() {
        OAuthConfig oauthConfig = new OAuthConfig("api_key", "api_secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("callback", oauthConfig.getCallback());
        assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
        assertEquals("scope", oauthConfig.getScope());
        assertTrue(oauthConfig.hasScope());
    }
}