import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    private OAuthConfig oAuthConfig;

    @Before
    public void setUp() {
        oAuthConfig = new OAuthConfig("key", "secret");
    }

    @Test
    public void testConstructorWithKeyAndSecret() {
        assertEquals("key", oAuthConfig.getApiKey());
        assertEquals("secret", oAuthConfig.getApiSecret());
        assertNull(oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
    }

    @Test
    public void testGetApiKey() {
        assertEquals("key", oAuthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        assertEquals("secret", oAuthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        assertNull(oAuthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        assertNull(oAuthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        assertFalse(oAuthConfig.hasScope());
    }
}