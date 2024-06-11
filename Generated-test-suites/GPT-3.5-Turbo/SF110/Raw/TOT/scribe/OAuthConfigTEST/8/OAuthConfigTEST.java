import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    private OAuthConfig oauthConfig;

    @Before
    public void setUp() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.Body;
        String scope = "testScope";
        
        oauthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
    }

    @Test
    public void testGetApiKey() {
        String expected = "testKey";
        assertEquals(expected, oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        String expected = "testSecret";
        assertEquals(expected, oauthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        String expected = "testCallback";
        assertEquals(expected, oauthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        SignatureType expected = SignatureType.Body;
        assertEquals(expected, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        String expected = "testScope";
        assertEquals(expected, oauthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        assertTrue(oauthConfig.hasScope());
    }

    @Test
    public void testDefaultConstructor() {
        OAuthConfig defaultConfig = new OAuthConfig("defaultKey", "defaultSecret");
        assertNull(defaultConfig.getCallback());
        assertEquals(SignatureType.Header, defaultConfig.getSignatureType());
        assertNull(defaultConfig.getScope());
    }
}