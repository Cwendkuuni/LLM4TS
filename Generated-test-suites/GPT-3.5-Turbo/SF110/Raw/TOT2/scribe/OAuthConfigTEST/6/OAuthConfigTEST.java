import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testConstructor1() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret");
        assertEquals("apiKey", oauthConfig.getApiKey());
        assertEquals("apiSecret", oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }
    
    @Test
    public void testConstructor2() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Querystring, "scope");
        assertEquals("apiKey", oauthConfig.getApiKey());
        assertEquals("apiSecret", oauthConfig.getApiSecret());
        assertEquals("callback", oauthConfig.getCallback());
        assertEquals(SignatureType.Querystring, oauthConfig.getSignatureType());
        assertEquals("scope", oauthConfig.getScope());
        assertTrue(oauthConfig.hasScope());
    }
    
    @Test
    public void testGetApiKey() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret");
        assertEquals("apiKey", oauthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret");
        assertEquals("apiSecret", oauthConfig.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Querystring, "scope");
        assertEquals("callback", oauthConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Querystring, "scope");
        assertEquals(SignatureType.Querystring, oauthConfig.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Querystring, "scope");
        assertEquals("scope", oauthConfig.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Querystring, "scope");
        assertTrue(oauthConfig.hasScope());
        
        OAuthConfig oauthConfig2 = new OAuthConfig("apiKey", "apiSecret");
        assertFalse(oauthConfig2.hasScope());
    }
}