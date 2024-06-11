import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import static org.junit.Assert.*;

public class OAuthRequestTest {

  @Test
  public void testConstructor() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    assertEquals(Verb.GET, request.getVerb());
    assertEquals("http://example.com", request.getUrl());
  }

  @Test
  public void testAddOAuthParameter() {
    OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
    request.addOAuthParameter("oauth_token", "12345");
    assertEquals("12345", request.getOauthParameters().get("oauth_token"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddOAuthParameterWithInvalidKey() {
    OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
    request.addOAuthParameter("invalid_key", "value");
  }

  @Test
  public void testGetOauthParameters() {
    OAuthRequest request = new OAuthRequest(Verb.PUT, "http://example.com");
    request.addOAuthParameter("oauth_consumer_key", "abcdef");
    request.addOAuthParameter("oauth_scope", "read");
    assertEquals("abcdef", request.getOauthParameters().get("oauth_consumer_key"));
    assertEquals("read", request.getOauthParameters().get("oauth_scope"));
  }

  @Test
  public void testToString() {
    OAuthRequest request = new OAuthRequest(Verb.DELETE, "http://example.com");
    assertEquals("@OAuthRequest(DELETE, http://example.com)", request.toString());
  }

}