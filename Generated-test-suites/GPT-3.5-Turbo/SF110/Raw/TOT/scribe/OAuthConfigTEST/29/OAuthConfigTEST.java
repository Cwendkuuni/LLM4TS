import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testConstructorWithTwoArguments() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        Assert.assertEquals("key", config.getApiKey());
        Assert.assertEquals("secret", config.getApiSecret());
        Assert.assertEquals("oob", config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
        Assert.assertNull(config.getScope());
        Assert.assertFalse(config.hasScope());
    }
    
    @Test
    public void testConstructorWithAllArguments() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        
        Assert.assertEquals("key", config.getApiKey());
        Assert.assertEquals("secret", config.getApiSecret());
        Assert.assertEquals("callback", config.getCallback());
        Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
        Assert.assertEquals("scope", config.getScope());
        Assert.assertTrue(config.hasScope());
    }
    
    @Test
    public void testConstructorWithNullCallbackAndScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, SignatureType.Header, null);
        
        Assert.assertEquals("key", config.getApiKey());
        Assert.assertEquals("secret", config.getApiSecret());
        Assert.assertEquals("oob", config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
        Assert.assertNull(config.getScope());
        Assert.assertFalse(config.hasScope());
    }
    
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertEquals("key", config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        Assert.assertEquals("secret", config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig config1 = new OAuthConfig("key", "secret");
        Assert.assertEquals("oob", config1.getCallback());
        
        OAuthConfig config2 = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertEquals("callback", config2.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config1 = new OAuthConfig("key", "secret");
        Assert.assertEquals(SignatureType.Header, config1.getSignatureType());
        
        OAuthConfig config2 = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        Assert.assertEquals(SignatureType.QueryString, config2.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config1 = new OAuthConfig("key", "secret");
        Assert.assertNull(config1.getScope());
        
        OAuthConfig config2 = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertEquals("scope", config2.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config1 = new OAuthConfig("key", "secret");
        Assert.assertFalse(config1.hasScope());
        
        OAuthConfig config2 = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        Assert.assertTrue(config2.hasScope());
    }
}