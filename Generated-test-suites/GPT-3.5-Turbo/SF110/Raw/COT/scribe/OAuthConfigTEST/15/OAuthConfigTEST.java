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
    public void testGetApiKey() {
        assertEquals("apiKey", oauthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        assertEquals("apiSecret", oauthConfig.getApiSecret());
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
    public void testConstructorWithKeyAndSecret() {
        assertEquals("apiKey", oauthConfig.getApiKey());
        assertEquals("apiSecret", oauthConfig.getApiSecret());
        assertEquals("oob", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Query, "scope");
        assertEquals("apiKey", oauthConfig.getApiKey());
        assertEquals("apiSecret", oauthConfig.getApiSecret());
        assertEquals("callback", oauthConfig.getCallback());
        assertEquals(SignatureType.Query, oauthConfig.getSignatureType());
        assertEquals("scope", oauthConfig.getScope());
    }
}