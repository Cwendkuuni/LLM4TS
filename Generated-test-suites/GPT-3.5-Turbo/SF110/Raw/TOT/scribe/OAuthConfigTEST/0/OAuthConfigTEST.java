import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
  
  private OAuthConfig oAuthConfig;
  private final String apiKey = "testApiKey";
  private final String apiSecret = "testApiSecret";
  private final String callback = "testCallback";
  private final SignatureType signatureType = SignatureType.Query;
  private final String scope = "testScope";
  
  @Before
  public void setUp() {
    oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
  }
  
  @After
  public void tearDown() {
    oAuthConfig = null;
  }
  
  // Test case for the first constructor
  @Test
  public void testConstructor1() {
    OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
    assertEquals(apiKey, config.getApiKey());
    assertEquals(apiSecret, config.getApiSecret());
    assertNull(config.getCallback());
    assertNull(config.getSignatureType());
    assertNull(config.getScope());
  }
  
  // Test case for the second constructor with all parameters provided
  @Test
  public void testConstructor2() {
    assertEquals(apiKey, oAuthConfig.getApiKey());
    assertEquals(apiSecret, oAuthConfig.getApiSecret());
    assertEquals(callback, oAuthConfig.getCallback());
    assertEquals(signatureType, oAuthConfig.getSignatureType());
    assertEquals(scope, oAuthConfig.getScope());
  }
  
  // Test case for the second constructor with null callback and signatureType
  @Test
  public void testConstructor2WithNulls() {
    OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, scope);
    assertEquals(apiKey, config.getApiKey());
    assertEquals(apiSecret, config.getApiSecret());
    assertEquals("oob", config.getCallback());
    assertEquals(SignatureType.Header, config.getSignatureType());
    assertEquals(scope, config.getScope());
  }
  
  // Test case for getApiKey method
  @Test
  public void testGetApiKey() {
    assertEquals(apiKey, oAuthConfig.getApiKey());
  }
  
  // Test case for getApiSecret method
  @Test
  public void testGetApiSecret() {
    assertEquals(apiSecret, oAuthConfig.getApiSecret());
  }
  
  // Test case for getCallback method
  @Test
  public void testGetCallback() {
    assertEquals(callback, oAuthConfig.getCallback());
  }
  
  // Test case for getSignatureType method
  @Test
  public void testGetSignatureType() {
    assertEquals(signatureType, oAuthConfig.getSignatureType());
  }
  
  // Test case for getScope method
  @Test
  public void testGetScope() {
    assertEquals(scope, oAuthConfig.getScope());
  }
  
  // Test case for hasScope method when scope is not null
  @Test
  public void testHasScopeNotNull() {
    assertTrue(oAuthConfig.hasScope());
  }
  
  // Test case for hasScope method when scope is null
  @Test
  public void testHasScopeNull() {
    OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
    assertFalse(config.hasScope());
  }
}