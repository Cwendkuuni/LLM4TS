import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import static org.junit.Assert.*;

public class OAuthConfigTest {

    private OAuthConfig oAuthConfig;

    @Before
    public void setUp() {
        oAuthConfig = new OAuthConfig("key", "secret");
    }

    @Test
    public void testGetApiKey() {
        assertEquals("key", oAuthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        assertEquals("secret", oAuthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback_DefaultValue() {
        assertEquals("oob", oAuthConfig.getCallback());
    }

    @Test
    public void testGetCallback_CustomValue() {
        oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("callback", oAuthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType_DefaultValue() {
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
    }

    @Test
    public void testGetSignatureType_CustomValue() {
        oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals(SignatureType.QueryString, oAuthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        assertEquals(null, oAuthConfig.getScope());
    }

    @Test
    public void testHasScope_NoScope() {
        assertFalse(oAuthConfig.hasScope());
    }

    @Test
    public void testHasScope_WithScope() {
        oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertTrue(oAuthConfig.hasScope());
    }
}