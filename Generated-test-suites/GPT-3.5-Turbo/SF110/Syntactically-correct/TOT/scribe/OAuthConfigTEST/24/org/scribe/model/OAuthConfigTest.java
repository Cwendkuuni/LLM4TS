import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    private OAuthConfig oauthConfig;

    @Before
    public void setUp() {
        oauthConfig = new OAuthConfig("API_KEY", "API_SECRET");
    }

    @Test
    public void testConstructorWithKeyAndSecret() {
        Assert.assertEquals("API_KEY", oauthConfig.getApiKey());
        Assert.assertEquals("API_SECRET", oauthConfig.getApiSecret());
        Assert.assertEquals("oob", oauthConfig.getCallback());
        Assert.assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        Assert.assertNull(oauthConfig.getScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        oauthConfig = new OAuthConfig("API_KEY", "API_SECRET", "http://callback.com", SignatureType.QueryString, "email");
        Assert.assertEquals("API_KEY", oauthConfig.getApiKey());
        Assert.assertEquals("API_SECRET", oauthConfig.getApiSecret());
        Assert.assertEquals("http://callback.com", oauthConfig.getCallback());
        Assert.assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
        Assert.assertEquals("email", oauthConfig.getScope());
    }

    @Test
    public void testConstructorWithNullCallback() {
        oauthConfig = new OAuthConfig("API_KEY", "API_SECRET", null, SignatureType.Header, "profile");
        Assert.assertEquals("API_KEY", oauthConfig.getApiKey());
        Assert.assertEquals("API_SECRET", oauthConfig.getApiSecret());
        Assert.assertEquals("oob", oauthConfig.getCallback());
        Assert.assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        Assert.assertEquals("profile", oauthConfig.getScope());
    }

    @Test
    public void testConstructorWithNullSignatureType() {
        oauthConfig = new OAuthConfig("API_KEY", "API_SECRET", "http://callback.com", null, "email");
        Assert.assertEquals("API_KEY", oauthConfig.getApiKey());
        Assert.assertEquals("API_SECRET", oauthConfig.getApiSecret());
        Assert.assertEquals("http://callback.com", oauthConfig.getCallback());
        Assert.assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        Assert.assertEquals("email", oauthConfig.getScope());
    }

    @Test
    public void testConstructorWithNullScope() {
        oauthConfig = new OAuthConfig("API_KEY", "API_SECRET", "http://callback.com", SignatureType.QueryString, null);
        Assert.assertEquals("API_KEY", oauthConfig.getApiKey());
        Assert.assertEquals("API_SECRET", oauthConfig.getApiSecret());
        Assert.assertEquals("http://callback.com", oauthConfig.getCallback());
        Assert.assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
        Assert.assertNull(oauthConfig.getScope());
    }

    @Test
    public void testHasScopeWithScope() {
        oauthConfig = new OAuthConfig("API_KEY", "API_SECRET", "http://callback.com", SignatureType.QueryString, "email");
        Assert.assertTrue(oauthConfig.hasScope());
    }

    @Test
    public void testHasScopeWithoutScope() {
        Assert.assertFalse(oauthConfig.hasScope());
    }
}