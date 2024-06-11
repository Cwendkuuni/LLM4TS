import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        // Create an instance of OAuthConfig
        OAuthConfig config = new OAuthConfig("key", "secret");

        // Test the getApiKey() method
        assertEquals("key", config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        // Create an instance of OAuthConfig
        OAuthConfig config = new OAuthConfig("key", "secret");

        // Test the getApiSecret() method
        assertEquals("secret", config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        // Create an instance of OAuthConfig
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        // Test the getCallback() method
        assertEquals("callback", config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        // Create an instance of OAuthConfig
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        // Test the getSignatureType() method
        assertEquals(SignatureType.QueryString, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        // Create an instance of OAuthConfig
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        // Test the getScope() method
        assertEquals("scope", config.getScope());
    }

    @Test
    public void testHasScope() {
        // Create an instance of OAuthConfig with scope
        OAuthConfig configWithScope = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        // Test the hasScope() method when there is a scope
        assertTrue(configWithScope.hasScope());

        // Create an instance of OAuthConfig without scope
        OAuthConfig configWithoutScope = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, null);

        // Test the hasScope() method when there is no scope
        assertFalse(configWithoutScope.hasScope());
    }

}