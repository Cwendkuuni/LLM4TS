import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testConstructorFirst() {
        OAuthConfig oAuthConfig = new OAuthConfig("key1", "secret1");
        
        assertEquals("key1", oAuthConfig.getApiKey());
        assertEquals("secret1", oAuthConfig.getApiSecret());
        assertNull(oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
        assertFalse(oAuthConfig.hasScope());
    }
    
    @Test
    public void testConstructorSecond() {
        OAuthConfig oAuthConfig = new OAuthConfig("key2", "secret2", "callback2", SignatureType.QueryString, "scope2");
        
        assertEquals("key2", oAuthConfig.getApiKey());
        assertEquals("secret2", oAuthConfig.getApiSecret());
        assertEquals("callback2", oAuthConfig.getCallback());
        assertEquals(SignatureType.QueryString, oAuthConfig.getSignatureType());
        assertEquals("scope2", oAuthConfig.getScope());
        assertTrue(oAuthConfig.hasScope());
    }
    
    @Test
    public void testGetApiKey() {
        OAuthConfig oAuthConfig = new OAuthConfig("key3", "secret3");
        
        assertEquals("key3", oAuthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig oAuthConfig = new OAuthConfig("key4", "secret4");
        
        assertEquals("secret4", oAuthConfig.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig oAuthConfig = new OAuthConfig("key5", "secret5", "callback5", SignatureType.Header, "scope5");
        
        assertEquals("callback5", oAuthConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig oAuthConfig = new OAuthConfig("key6", "secret6", "callback6", SignatureType.QueryString, "scope6");
        
        assertEquals(SignatureType.QueryString, oAuthConfig.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("key7", "secret7", "callback7", SignatureType.Header, "scope7");
        
        assertEquals("scope7", oAuthConfig.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("key8", "secret8");
        
        assertFalse(oAuthConfig.hasScope());
        
        OAuthConfig oAuthConfigWithScope = new OAuthConfig("key9", "secret9", "callback9", SignatureType.Header, "scope9");
        
        assertTrue(oAuthConfigWithScope.hasScope());
    }
}