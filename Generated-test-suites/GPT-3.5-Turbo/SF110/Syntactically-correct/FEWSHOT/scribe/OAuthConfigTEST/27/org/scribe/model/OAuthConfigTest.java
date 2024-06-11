
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {
    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret");
        assertEquals("key", oAuthConfig.getApiKey());
        assertEquals("secret", oAuthConfig.getApiSecret());
        assertEquals("oob", oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
        assertFalse(oAuthConfig.hasScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Query, "scope");
        assertEquals("key", oAuthConfig.getApiKey());
        assertEquals("secret", oAuthConfig.getApiSecret());
        assertEquals("callback", oAuthConfig.getCallback());
        assertEquals(SignatureType.Query, oAuthConfig.getSignatureType());
        assertEquals("scope", oAuthConfig.getScope());
        assertTrue(oAuthConfig.hasScope());
    }
}
