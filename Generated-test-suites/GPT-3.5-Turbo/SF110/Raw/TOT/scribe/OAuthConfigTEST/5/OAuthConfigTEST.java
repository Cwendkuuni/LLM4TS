
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertEquals("key", oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertEquals("secret", oauthConfig.getApiSecret());
    }

    @Test
    public void testGetCallbackWithNull() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertNull(oauthConfig.getCallback());
    }

    @Test
    public void testGetCallbackWithValue() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("callback", oauthConfig.getCallback());
    }

    @Test
    public void testGetSignatureTypeWithNull() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetSignatureTypeWithValue() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScopeWithNull() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testGetScopeWithValue() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("scope", oauthConfig.getScope());
    }

    @Test
    public void testHasScopeWithNull() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testHasScopeWithValue() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertTrue(oauthConfig.hasScope());
    }
}

