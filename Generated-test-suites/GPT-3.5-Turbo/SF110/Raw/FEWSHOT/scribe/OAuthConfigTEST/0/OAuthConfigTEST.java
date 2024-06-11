//Please note that the Scope parameter has a default value of null. So, all the tests will be performed with null value:

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OAuthConfigTest {
    
    @Test
    public void testConstructorWithTwoParameters() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertNull(config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
    }
    
    @Test
    public void testConstructorWithFiveParameters() {
        SignatureType type = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", type, null);
        assertEquals("callback", config.getCallback());
        assertEquals(type, config.getSignatureType());
        assertNull(config.getScope());
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
    }
    
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
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", null, null);
        assertEquals("callback", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        SignatureType type = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig("key", "secret", null, type, null);
        assertEquals(type, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, null, null);
        assertNull(config.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, null, null);
        assertEquals(false, config.hasScope());
    }
}