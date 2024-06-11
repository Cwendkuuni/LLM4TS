
import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {
  
  @Test
  public void testConstructorWithKeyAndSecret() {
    OAuthConfig config = new OAuthConfig("api_key", "api_secret");
    
    Assert.assertEquals("api_key", config.getApiKey());
    Assert.assertEquals("api_secret", config.getApiSecret());
    Assert.assertEquals("oob", config.getCallback());
    Assert.assertEquals(SignatureType.Header, config.getSignatureType());
    Assert.assertNull(config.getScope());
    Assert.assertFalse(config.hasScope());
  }
  
  @Test
  public void testConstructorWithAllParameters() {
    OAuthConfig config = new OAuthConfig("api_key", "api_secret", "callback", SignatureType.QueryString, "scope");
    
    Assert.assertEquals("api_key", config.getApiKey());
    Assert.assertEquals("api_secret", config.getApiSecret());
    Assert.assertEquals("callback", config.getCallback());
    Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
    Assert.assertEquals("scope", config.getScope());
    Assert.assertTrue(config.hasScope());
  }
  
  @Test
  public void testGetApiKey() {
    OAuthConfig config = new OAuthConfig("api_key", "api_secret");
    
    Assert.assertEquals("api_key", config.getApiKey());
  }
  
  @Test
  public void testGetApiSecret() {
    OAuthConfig config = new OAuthConfig("api_key", "api_secret");
    
    Assert.assertEquals("api_secret", config.getApiSecret());
  }
  
  @Test
  public void testGetCallback() {
    OAuthConfig config = new OAuthConfig("api_key", "api_secret", "callback", SignatureType.QueryString, "scope");
    
    Assert.assertEquals("callback", config.getCallback());
  }
  
  @Test
  public void testGetSignatureType() {
    OAuthConfig config = new OAuthConfig("api_key", "api_secret", "callback", SignatureType.QueryString, "scope");
    
    Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
  }
  
  @Test
  public void testGetScope() {
    OAuthConfig config = new OAuthConfig("api_key", "api_secret", "callback", SignatureType.QueryString, "scope");
    
    Assert.assertEquals("scope", config.getScope());
  }
  
  @Test
  public void testHasScope() {
    OAuthConfig config = new OAuthConfig("api_key", "api_secret", "callback", SignatureType.QueryString, "scope");
    
    Assert.assertTrue(config.hasScope());
  }
}
