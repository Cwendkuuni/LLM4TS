import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("key", config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("secret", config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("oob", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertNull(config.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertFalse(config.hasScope());
    }

}