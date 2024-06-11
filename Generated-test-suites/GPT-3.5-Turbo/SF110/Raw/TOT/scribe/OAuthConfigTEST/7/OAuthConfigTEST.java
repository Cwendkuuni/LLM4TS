import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    private OAuthConfig oauthConfig;

    @Before
    public void setUp() {
        // Initial setup with valid key and secret
        oauthConfig = new OAuthConfig("key", "secret");
    }

    @Test
    public void testConstructorWithKeyAndSecret() {
        Assert.assertEquals("key", oauthConfig.getApiKey());
        Assert.assertEquals("secret", oauthConfig.getApiSecret());
        Assert.assertNull(oauthConfig.getCallback());
        Assert.assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        Assert.assertNull(oauthConfig.getScope());
        Assert.assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        OAuthConfig fullConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        Assert.assertEquals("key", fullConfig.getApiKey());
        Assert.assertEquals("secret", fullConfig.getApiSecret());
        Assert.assertEquals("callback", fullConfig.getCallback());
        Assert.assertEquals(SignatureType.QueryString, fullConfig.getSignatureType());
        Assert.assertEquals("scope", fullConfig.getScope());
        Assert.assertTrue(fullConfig.hasScope());
    }

    @Test
    public void testConstructorWithNullCallbackAndSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, null, "scope");

        Assert.assertEquals("key", config.getApiKey());
        Assert.assertEquals("secret", config.getApiSecret());
        Assert.assertEquals("oob", config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
        Assert.assertEquals("scope", config.getScope());
        Assert.assertTrue(config.hasScope());
    }

    @Test
    public void testGetApiKey() {
        Assert.assertEquals("key", oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        Assert.assertEquals("secret", oauthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        Assert.assertNull(oauthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        Assert.assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        Assert.assertNull(oauthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        Assert.assertFalse(oauthConfig.hasScope());
    }
}