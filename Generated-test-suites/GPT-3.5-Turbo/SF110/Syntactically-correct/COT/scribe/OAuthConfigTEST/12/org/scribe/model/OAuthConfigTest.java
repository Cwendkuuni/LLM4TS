
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OAuthConfigTest {

    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret");
        assertNotNull(oAuthConfig);
        assertEquals("key", oAuthConfig.getApiKey());
        assertEquals("secret", oAuthConfig.getApiSecret());
        assertEquals("oob", oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
        assertFalse(oAuthConfig.hasScope());
    }
    
    @Test
    public void testConstructorWithAllParams() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertNotNull(oAuthConfig);
        assertEquals("key", oAuthConfig.getApiKey());
        assertEquals("secret", oAuthConfig.getApiSecret());
        assertEquals("callback", oAuthConfig.getCallback());
        assertEquals(SignatureType.QueryString, oAuthConfig.getSignatureType());
        assertEquals("scope", oAuthConfig.getScope());
        assertTrue(oAuthConfig.hasScope());
    }
    
    @Test
    public void testGetApiKey() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret");
        assertEquals("key", oAuthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret");
        assertEquals("secret", oAuthConfig.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("callback", oAuthConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals(SignatureType.QueryString, oAuthConfig.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("scope", oAuthConfig.getScope());
    }
    
    @Test
    public void testHasScopeWithScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertTrue(oAuthConfig.hasScope());
    }
    
    @Test
    public void testHasScopeWithoutScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret");
        assertFalse(oAuthConfig.hasScope());
    }
}
