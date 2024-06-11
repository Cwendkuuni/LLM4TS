import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthConfigTest {
  
  @Test
  public void testConstructor1() {
    OAuthConfig config = new OAuthConfig("key", "secret");
    
    assertEquals("key", config.getApiKey());
    assertEquals("secret", config.getApiSecret());
    assertEquals("oob", config.getCallback());
    assertEquals(SignatureType.Header, config.getSignatureType());
    assertNull(config.getScope());
    assertFalse(config.hasScope());
  }
  
  @Test
  public void testConstructor2() {
    OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
    
    assertEquals("key", config.getApiKey());
    assertEquals("secret", config.getApiSecret());
    assertEquals("callback", config.getCallback());
    assertEquals(SignatureType.QueryString, config.getSignatureType());
    assertEquals("scope", config.getScope());
    assertTrue(config.hasScope());
  }
  
  @Test
  public void testConstructor2WithNullCallback() {
    OAuthConfig config = new OAuthConfig("key", "secret", null, SignatureType.QueryString, "scope");
    
    assertEquals("key", config.getApiKey());
    assertEquals("secret", config.getApiSecret());
    assertEquals("oob", config.getCallback());
    assertEquals(SignatureType.QueryString, config.getSignatureType());
    assertEquals("scope", config.getScope());
    assertTrue(config.hasScope());
  }
  
  @Test
  public void testConstructor2WithNullSignatureType() {
    OAuthConfig config = new OAuthConfig("key", "secret", "callback", null, "scope");
    
    assertEquals("key", config.getApiKey());
    assertEquals("secret", config.getApiSecret());
    assertEquals("callback", config.getCallback());
    assertEquals(SignatureType.Header, config.getSignatureType());
    assertEquals("scope", config.getScope());
    assertTrue(config.hasScope());
  }
  
  @Test
  public void testGetApiKey() {
    OAuthConfig config = new OAuthConfig("key", "secret");
    
    assertEquals("key", config.getApiKey());
  }
  
  @Test
  public void testGetApiSecret() {
    OAuthConfig config = new OAuthConfig("key", "secret");
    
    assertEquals("secret", config.getApiSecret());
  }
  
  @Test
  public void testGetCallback() {
    OAuthConfig config1 = new OAuthConfig("key", "secret");
    OAuthConfig config2 = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
    
    assertEquals("oob", config1.getCallback());
    assertEquals("callback", config2.getCallback());
  }
  
  @Test
  public void testGetSignatureType() {
    OAuthConfig config1 = new OAuthConfig("key", "secret");
    OAuthConfig config2 = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
    
    assertEquals(SignatureType.Header, config1.getSignatureType());
    assertEquals(SignatureType.QueryString, config2.getSignatureType());
  }
  
  @Test
  public void testGetScope() {
    OAuthConfig config1 = new OAuthConfig("key", "secret");
    OAuthConfig config2 = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
    
    assertNull(config1.getScope());
    assertEquals("scope", config2.getScope());
  }
  
  @Test
  public void testHasScope() {
    OAuthConfig config1 = new OAuthConfig("key", "secret");
    OAuthConfig config2 = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
    
    assertFalse(config1.hasScope());
    assertTrue(config2.hasScope());
  }
}