import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
    
    private OAuthConfig oauthConfig;
    
    @Before
    public void setup() {
        oauthConfig = new OAuthConfig("myKey", "mySecret");
    }
    
    @Test
    public void testConstructorWithKeyAndSecret() {
        assertEquals("myKey", oauthConfig.getApiKey());
        assertEquals("mySecret", oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        OAuthConfig oauthConfig = new OAuthConfig("myKey", "mySecret", "myCallback", SignatureType.Query, "myScope");
        
        assertEquals("myKey", oauthConfig.getApiKey());
        assertEquals("mySecret", oauthConfig.getApiSecret());
        assertEquals("myCallback", oauthConfig.getCallback());
        assertEquals(SignatureType.Query, oauthConfig.getSignatureType());
        assertEquals("myScope", oauthConfig.getScope());
        assertTrue(oauthConfig.hasScope());
    }
    
    @Test
    public void testConstructorWithNullCallback() {
        OAuthConfig oauthConfig = new OAuthConfig("myKey", "mySecret", null, SignatureType.Header, "myScope");
        
        assertEquals("myKey", oauthConfig.getApiKey());
        assertEquals("mySecret", oauthConfig.getApiSecret());
        assertEquals("oob", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertEquals("myScope", oauthConfig.getScope());
        assertTrue(oauthConfig.hasScope());
    }
    
    @Test
    public void testConstructorWithNullType() {
        OAuthConfig oauthConfig = new OAuthConfig("myKey", "mySecret", "myCallback", null, "myScope");
        
        assertEquals("myKey", oauthConfig.getApiKey());
        assertEquals("mySecret", oauthConfig.getApiSecret());
        assertEquals("myCallback", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertEquals("myScope", oauthConfig.getScope());
        assertTrue(oauthConfig.hasScope());
    }
    
    @Test
    public void testConstructorWithNullScope() {
        OAuthConfig oauthConfig = new OAuthConfig("myKey", "mySecret", "myCallback", SignatureType.Header, null);
        
        assertEquals("myKey", oauthConfig.getApiKey());
        assertEquals("mySecret", oauthConfig.getApiSecret());
        assertEquals("myCallback", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }
    
    @Test
    public void testGetApiKey() {
        assertEquals("myKey", oauthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        assertEquals("mySecret", oauthConfig.getApiSecret());
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
    public void testHasScopeWithScope() {
        OAuthConfig oauthConfig = new OAuthConfig("myKey", "mySecret", "myCallback", SignatureType.Header, "myScope");
        
        assertTrue(oauthConfig.hasScope());
    }
    
    @Test
    public void testHasScopeWithoutScope() {
        assertFalse(oauthConfig.hasScope());
    }
}