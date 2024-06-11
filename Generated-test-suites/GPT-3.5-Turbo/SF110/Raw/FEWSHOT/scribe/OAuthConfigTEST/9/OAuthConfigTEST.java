
package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OAuthConfigTest {

    private final String apiKey = "API_KEY";
    private final String apiSecret = "API_SECRET";
    private final String callback = "CALLBACK";
    private final SignatureType signatureType = SignatureType.Header;
    private final String scope = "SCOPE";

    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);

        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
        assertEquals("oob", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
        assertEquals(callback, oauthConfig.getCallback());
        assertEquals(signatureType, oauthConfig.getSignatureType());
        assertEquals(scope, oauthConfig.getScope());
        assertTrue(oauthConfig.hasScope());
    }

    @Test
    public void testGetApiKey() {
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);

        assertEquals(apiKey, oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);

        assertEquals(apiSecret, oauthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);

        assertEquals("oob", oauthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);

        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(scope, oauthConfig.getScope());
    }

    @Test
    public void testHasScopeWithNullScope() {
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);

        assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testHasScopeWithNonNullScope() {
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertTrue(oauthConfig.hasScope());
    }
}
