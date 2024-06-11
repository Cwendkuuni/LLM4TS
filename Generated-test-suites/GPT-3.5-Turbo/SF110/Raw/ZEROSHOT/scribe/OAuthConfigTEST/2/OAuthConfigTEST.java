import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    private OAuthConfig oauthConfig;

    @Before
    public void setup() {
        oauthConfig = new OAuthConfig("apiKey", "apiSecret");
    }

    @Test
    public void testConstructorWithTwoArguments() {
        assertEquals("apiKey", oauthConfig.getApiKey());
        assertEquals("apiSecret", oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertNull(oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testConstructorWithAllArguments() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "scope");
        assertEquals("apiKey", oauthConfig.getApiKey());
        assertEquals("apiSecret", oauthConfig.getApiSecret());
        assertEquals("callback", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
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
        assertNull(oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        assertFalse(oauthConfig.hasScope());
    }
}