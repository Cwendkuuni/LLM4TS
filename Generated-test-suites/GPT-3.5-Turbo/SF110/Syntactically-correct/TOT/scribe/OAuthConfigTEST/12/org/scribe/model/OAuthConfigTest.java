import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {
  
  private OAuthConfig oauthConfig;
  
  @Before
  public void setup() {
    oauthConfig = new OAuthConfig("apiKey", "apiSecret");
  }
  
  @After
  public void tearDown() {
    oauthConfig = null;
  }
  
  @Test
  public void testOAuthConfigConstructor1() {
    Assert.assertEquals("apiKey", oauthConfig.getApiKey());
    Assert.assertEquals("apiSecret", oauthConfig.getApiSecret());
    Assert.assertEquals("oob", oauthConfig.getCallback());
    Assert.assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    Assert.assertNull(oauthConfig.getScope());
  }
  
  @Test
  public void testOAuthConfigConstructor2() {
    oauthConfig = new OAuthConfig("apiKey2", "apiSecret2", "callback", SignatureType.QueryString, "scope");
    Assert.assertEquals("apiKey2", oauthConfig.getApiKey());
    Assert.assertEquals("apiSecret2", oauthConfig.getApiSecret());
    Assert.assertEquals("callback", oauthConfig.getCallback());
    Assert.assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
    Assert.assertEquals("scope", oauthConfig.getScope());
  }
  
  @Test
  public void testGetApiKey() {
    Assert.assertEquals("apiKey", oauthConfig.getApiKey());
  }
  
  @Test
  public void testGetApiSecret() {
    Assert.assertEquals("apiSecret", oauthConfig.getApiSecret());
  }
  
  @Test
  public void testGetCallback() {
    Assert.assertEquals("oob", oauthConfig.getCallback());
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
  public void testHasScopeWithScope() {
    oauthConfig = new OAuthConfig("apiKey2", "apiSecret2", "callback", SignatureType.QueryString, "scope");
    Assert.assertTrue(oauthConfig.hasScope());
  }
  
  @Test
  public void testHasScopeWithoutScope() {
    Assert.assertFalse(oauthConfig.hasScope());
  }
}