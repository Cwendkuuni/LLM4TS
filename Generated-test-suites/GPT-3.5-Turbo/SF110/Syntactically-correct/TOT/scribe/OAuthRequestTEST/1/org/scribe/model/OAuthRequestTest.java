import org.junit.*;
import static org.junit.Assert.*;
import java.util.Map;

public class OAuthRequestTest {

  @Test
  public void testAddOAuthParameter() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    request.addOAuthParameter("oauth_token", "abcdefg");

    assertEquals("abcdefg", request.getOauthParameters().get("oauth_token"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddOAuthParameterInvalidKey() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    request.addOAuthParameter("invalid_key", "value");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddOAuthParameterInvalidKeyScope() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    request.addOAuthParameter("scope", "value");
  }

  @Test
  public void testAddOAuthParameterStartsWithOAuth() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    request.addOAuthParameter("oauth_test", "value");

    assertEquals("value", request.getOauthParameters().get("oauth_test"));
  }

  @Test
  public void testCheckKeyStartsWithOAuth() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    String validKey = request.checkKey("oauth_consumer_key");

    assertEquals("oauth_consumer_key", validKey);
  }

  @Test
  public void testCheckKeyIsScope() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    String validKey = request.checkKey("scope");

    assertEquals("scope", validKey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckKeyInvalidKey() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    request.checkKey("invalid_key");
  }

  @Test
  public void testToString() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

    assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
  }
}