import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        String apiKey = "key";
        String apiSecret = "secret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        Assert.assertEquals(apiKey, config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        String apiKey = "key";
        String apiSecret = "secret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        Assert.assertEquals(apiSecret, config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        String apiKey = "key";
        String apiSecret = "secret";
        String callback = "callback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, null);
        Assert.assertEquals(callback, config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        String apiKey = "key";
        String apiSecret = "secret";
        SignatureType signatureType = SignatureType.Header;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, null);
        Assert.assertEquals(signatureType, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        String apiKey = "key";
        String apiSecret = "secret";
        String scope = "scope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, scope);
        Assert.assertEquals(scope, config.getScope());
    }
    
    @Test
    public void testHasScope() {
        String apiKey = "key";
        String apiSecret = "secret";
        OAuthConfig configWithoutScope = new OAuthConfig(apiKey, apiSecret);
        Assert.assertFalse(configWithoutScope.hasScope());
        
        String scope = "scope";
        OAuthConfig configWithScope = new OAuthConfig(apiKey, apiSecret, null, null, scope);
        Assert.assertTrue(configWithScope.hasScope());
    }
  
}