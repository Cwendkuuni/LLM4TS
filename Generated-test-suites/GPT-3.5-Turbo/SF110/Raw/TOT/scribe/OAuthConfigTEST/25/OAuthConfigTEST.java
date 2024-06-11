import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
    
    private OAuthConfig oauthConfig;
    
    @Before
    public void setUp() {
        oauthConfig = new OAuthConfig("api_key", "api_secret");
    }
    
    @Test
    public void testGetApiKey() {
        assertEquals("api_key", oauthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        assertEquals("api_secret", oauthConfig.getApiSecret());
    }
    
    @Test
    public void testGetCallback_Null() {
        assertNull(oauthConfig.getCallback());
    }
    
    @Test
    public void testGetCallback_Custom() {
        OAuthConfig customCallbackConfig = new OAuthConfig("api_key", "api_secret", "custom_callback", null, null);
        assertEquals("custom_callback", customCallbackConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureType_Null() {
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }
    
    @Test
    public void testGetSignatureType_Custom() {
        OAuthConfig customSignatureConfig = new OAuthConfig("api_key", "api_secret", null, SignatureType.Query, null);
        assertEquals(SignatureType.Query, customSignatureConfig.getSignatureType());
    }
    
    @Test
    public void testGetScope_Null() {
        assertNull(oauthConfig.getScope());
    }
    
    @Test
    public void testGetScope_Custom() {
        OAuthConfig customScopeConfig = new OAuthConfig("api_key", "api_secret", null, null, "custom_scope");
        assertEquals("custom_scope", customScopeConfig.getScope());
    }
    
    @Test
    public void testHasScope_False() {
        assertFalse(oauthConfig.hasScope());
    }
    
    @Test
    public void testHasScope_True() {
        OAuthConfig scopedConfig = new OAuthConfig("api_key", "api_secret", null, null, "custom_scope");
        assertTrue(scopedConfig.hasScope());
    }
    
}