import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OAuthConfigTest {
    private OAuthConfig oauthConfig;

    @Before
    public void setUp() {
        oauthConfig = new OAuthConfig("API_KEY", "API_SECRET", "CALLBACK", SignatureType.Header, "SCOPE");
    }

    @Test
    public void testGetApiKey() {
        assertEquals("API_KEY", oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        assertEquals("API_SECRET", oauthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        assertEquals("CALLBACK", oauthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        assertEquals("SCOPE", oauthConfig.getScope());
    }

    @Test
    public void testHasScopeWithScope() {
        assertTrue(oauthConfig.hasScope());
    }

    @Test
    public void testHasScopeWithoutScope() {
        OAuthConfig oauthConfigWithoutScope = new OAuthConfig("API_KEY", "API_SECRET");
        assertFalse(oauthConfigWithoutScope.hasScope());
    }

    @Test
    public void testConstructorWithMinimalParams() {
        OAuthConfig oauthConfigMinimal = new OAuthConfig("API_KEY", "API_SECRET");
        assertEquals("API_KEY", oauthConfigMinimal.getApiKey());
        assertEquals("API_SECRET", oauthConfigMinimal.getApiSecret());
        assertEquals("oob", oauthConfigMinimal.getCallback());
        assertEquals(SignatureType.Header, oauthConfigMinimal.getSignatureType());
        assertEquals(null, oauthConfigMinimal.getScope());
    }

    @Test
    public void testConstructorWithAllParamsNull() {
        OAuthConfig oauthConfigAllNull = new OAuthConfig(null, null, null, null, null);
        assertEquals(null, oauthConfigAllNull.getApiKey());
        assertEquals(null, oauthConfigAllNull.getApiSecret());
        assertEquals("oob", oauthConfigAllNull.getCallback());
        assertEquals(SignatureType.Header, oauthConfigAllNull.getSignatureType());
        assertEquals(null, oauthConfigAllNull.getScope());
    }
}