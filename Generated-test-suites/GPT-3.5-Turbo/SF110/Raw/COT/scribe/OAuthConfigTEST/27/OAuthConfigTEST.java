
import org.junit.Assert;
import org.junit.Test;

public class OAuthConfigTest {
    
    @Test
    public void testConstructor1() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        Assert.assertEquals("key", oauthConfig.getApiKey());
        Assert.assertEquals("secret", oauthConfig.getApiSecret());
        Assert.assertEquals("oob", oauthConfig.getCallback());
        Assert.assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        Assert.assertNull(oauthConfig.getScope());
    }
    
    @Test
    public void testConstructor2() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Body, "scope");
        Assert.assertEquals("key", oauthConfig.getApiKey());
        Assert.assertEquals("secret", oauthConfig.getApiSecret());
        Assert.assertEquals("callback", oauthConfig.getCallback());
        Assert.assertEquals(SignatureType.Body, oauthConfig.getSignatureType());
        Assert.assertEquals("scope", oauthConfig.getScope());
    }
    
    @Test
    public void testGetApiKey() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        Assert.assertEquals("key", oauthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        Assert.assertEquals("secret", oauthConfig.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Body, "scope");
        Assert.assertEquals("callback", oauthConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Body, "scope");
        Assert.assertEquals(SignatureType.Body, oauthConfig.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Body, "scope");
        Assert.assertEquals("scope", oauthConfig.getScope());
    }
    
    @Test
    public void testHasScopeWithScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Body, "scope");
        Assert.assertTrue(oauthConfig.hasScope());
    }
    
    @Test
    public void testHasScopeWithoutScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        Assert.assertFalse(oauthConfig.hasScope());
    }
}
