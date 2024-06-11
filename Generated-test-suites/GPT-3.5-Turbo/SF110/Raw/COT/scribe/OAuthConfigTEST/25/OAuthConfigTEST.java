

import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    // Test constructor with only apiKey and apiSecret
    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        
        assertEquals("testKey", config.getApiKey());
        assertEquals("testSecret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    // Test constructor with all parameters
    @Test
    public void testConstructorWithAllParameters() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryString, "testScope");
        
        assertEquals("testKey", config.getApiKey());
        assertEquals("testSecret", config.getApiSecret());
        assertEquals("testCallback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("testScope", config.getScope());
        assertTrue(config.hasScope());
    }

    // Test getApiKey method
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");

        assertEquals("testKey", config.getApiKey());
    }

    // Test getApiSecret method
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");

        assertEquals("testSecret", config.getApiSecret());
    }

    // Test getCallback method
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryString, "testScope");

        assertEquals("testCallback", config.getCallback());
    }

    // Test getSignatureType method
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryString, "testScope");

        assertEquals(SignatureType.QueryString, config.getSignatureType());
    }

    // Test getScope method
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryString, "testScope");

        assertEquals("testScope", config.getScope());
    }

    // Test hasScope method
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryString, "testScope");

        assertTrue(config.hasScope());
    }

}
