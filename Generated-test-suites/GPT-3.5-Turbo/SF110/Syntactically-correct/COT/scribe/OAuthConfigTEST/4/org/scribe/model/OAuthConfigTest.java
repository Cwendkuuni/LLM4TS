import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    private OAuthConfig oauthConfig;

    @Before
    public void setUp() {
        oauthConfig = new OAuthConfig("apiKey", "apiSecret");
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
    public void testHasScope() {
        assertFalse(oauthConfig.hasScope());
    }
}