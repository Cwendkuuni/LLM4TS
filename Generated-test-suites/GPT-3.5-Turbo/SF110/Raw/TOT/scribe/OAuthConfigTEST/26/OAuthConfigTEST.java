import org.junit.*;
import static org.junit.Assert.*;

public class OAuthConfigTest {

  private OAuthConfig config;

  @Before
  public void setUp() {
    config = new OAuthConfig("apiKey", "apiSecret");
  }

  @Test
  public void testGetApiKey() {
    assertEquals("apiKey", config.getApiKey());
  }

  @Test
  public void testGetApiSecret() {
    assertEquals("apiSecret", config.getApiSecret());
  }

  @Test
  public void testGetCallbackNull() {
    assertNull(config.getCallback());
  }

  @Test
  public void testGetCallbackNonNull() {
    OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", null, "scope");
    assertEquals("callback", config.getCallback());
  }

  @Test
  public void testGetSignatureTypeNull() {
    assertEquals(SignatureType.Header, config.getSignatureType());
  }

  @Test
  public void testGetSignatureTypeNonNull() {
    OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
    assertEquals(SignatureType.QueryString, config.getSignatureType());
  }

  @Test
  public void testGetScopeNull() {
    assertNull(config.getScope());
  }

  @Test
  public void testGetScopeNonNull() {
    OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
    assertEquals("scope", config.getScope());
  }

  @Test
  public void testHasScopeTrue() {
    OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
    assertTrue(config.hasScope());
  }

  @Test
  public void testHasScopeFalse() {
    assertFalse(config.hasScope());
  }

  @Test
  public void testConstructorTwoParams() {
    assertEquals("apiKey", config.getApiKey());
    assertEquals("apiSecret", config.getApiSecret());
    assertNull(config.getCallback());
    assertEquals(SignatureType.Header, config.getSignatureType());
    assertNull(config.getScope());
  }

  @Test
  public void testConstructorAllParams() {
    OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
    assertEquals("apiKey", config.getApiKey());
    assertEquals("apiSecret", config.getApiSecret());
    assertEquals("callback", config.getCallback());
    assertEquals(SignatureType.QueryString, config.getSignatureType());
    assertEquals("scope", config.getScope());
  }

}