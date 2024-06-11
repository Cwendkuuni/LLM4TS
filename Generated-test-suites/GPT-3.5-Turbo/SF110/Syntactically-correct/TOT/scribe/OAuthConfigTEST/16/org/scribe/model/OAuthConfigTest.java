import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    private OAuthConfig oauthConfig;

    @Before
    public void setup() {
        oauthConfig = new OAuthConfig("testKey", "testSecret");
    }

    @Test
    public void testConstructorWithTwoParameters() {
        assertEquals("testKey", oauthConfig.getApiKey());
        assertEquals("testSecret", oauthConfig.getApiSecret());
        assertEquals("oob", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        oauthConfig = new OAuthConfig("testKey", "testSecret", "http://example.com/callback", SignatureType.QueryString, "read write");
        assertEquals("testKey", oauthConfig.getApiKey());
        assertEquals("testSecret", oauthConfig.getApiSecret());
        assertEquals("http://example.com/callback", oauthConfig.getCallback());
        assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
        assertEquals("read write", oauthConfig.getScope());
    }

    @Test
    public void testGetApiKey() {
        assertEquals("testKey", oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        assertEquals("testSecret", oauthConfig.getApiSecret());
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
    public void testHasScopeWithScope() {
        oauthConfig = new OAuthConfig("testKey", "testSecret", "http://example.com/callback", SignatureType.QueryString, "read write");
        assertTrue(oauthConfig.hasScope());
    }

}