package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OAuthConfigTest {

    private OAuthConfig oauthConfig;

    @Test
    public void testGetApiKey() {
        oauthConfig = new OAuthConfig("key", "secret");
        assertEquals("key", oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        oauthConfig = new OAuthConfig("key", "secret");
        assertEquals("secret", oauthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("callback", oauthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("scope", oauthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals(true, oauthConfig.hasScope());
    }
}