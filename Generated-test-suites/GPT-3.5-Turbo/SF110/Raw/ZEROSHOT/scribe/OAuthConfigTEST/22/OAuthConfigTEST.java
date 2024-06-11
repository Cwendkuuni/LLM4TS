import org.junit.*;
import org.scribe.model.*;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        String apiKey = config.getApiKey();
        
        Assert.assertEquals("key", apiKey);
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        String apiSecret = config.getApiSecret();
        
        Assert.assertEquals("secret", apiSecret);
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        
        String callback = config.getCallback();
        
        Assert.assertEquals("callback", callback);
    }
    
    @Test
    public void testGetCallbackWithNull() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        String callback = config.getCallback();
        
        Assert.assertEquals("oob", callback);
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        
        SignatureType signatureType = config.getSignatureType();
        
        Assert.assertEquals(SignatureType.Header, signatureType);
    }
    
    @Test
    public void testGetSignatureTypeWithNull() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", null, "scope");
        
        SignatureType signatureType = config.getSignatureType();
        
        Assert.assertEquals(SignatureType.Header, signatureType);
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        
        String scope = config.getScope();
        
        Assert.assertEquals("scope", scope);
    }
    
    @Test
    public void testGetScopeWithNull() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        String scope = config.getScope();
        
        Assert.assertNull(scope);
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        
        boolean hasScope = config.hasScope();
        
        Assert.assertTrue(hasScope);
    }
    
    @Test
    public void testHasScopeWithNull() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        boolean hasScope = config.hasScope();
        
        Assert.assertFalse(hasScope);
    }   
}