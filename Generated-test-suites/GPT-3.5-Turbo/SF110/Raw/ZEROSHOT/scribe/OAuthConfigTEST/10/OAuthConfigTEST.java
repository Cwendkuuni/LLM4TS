import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {
    
    private OAuthConfig oAuthConfig;
    
    @Before
    public void setUp() {
        oAuthConfig = new OAuthConfig("apiKey", "apiSecret");
    }
    
    @Test
    public void testConstructorWithKeyAndSecret() {
        assertEquals("apiKey", oAuthConfig.getApiKey());
        assertEquals("apiSecret", oAuthConfig.getApiSecret());
        assertNull(oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        oAuthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.Query, "scope");
        
        assertEquals("apiKey", oAuthConfig.getApiKey());
        assertEquals("apiSecret", oAuthConfig.getApiSecret());
        assertEquals("callbackUrl", oAuthConfig.getCallback());
        assertEquals(SignatureType.Query, oAuthConfig.getSignatureType());
        assertEquals("scope", oAuthConfig.getScope());
    }
    
    @Test
    public void testGetApiKey() {
        assertEquals("apiKey", oAuthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        assertEquals("apiSecret", oAuthConfig.getApiSecret());
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
    public void testHasScopeWithNullScope() {
        assertFalse(oAuthConfig.hasScope());
    }
    
    @Test
    public void testHasScopeWithScope() {
        oAuthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.Header, "scope");
        
        assertTrue(oAuthConfig.hasScope());
    }
}