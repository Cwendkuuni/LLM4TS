
import org.junit.*;
import org.scribe.model.*;

public class OAuthRequestTest {

  @Test
  public void testConstructor() {
    Verb verb = Verb.GET;
    String url = "https://www.example.com";
    OAuthRequest request = new OAuthRequest(verb, url);

    Assert.assertEquals(verb, request.getVerb());
    Assert.assertEquals(url, request.getUrl());
    Assert.assertNotNull(request.getOauthParameters());
  }

  @Test
  public void testAddOAuthParameter() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
    String key = "oauth_consumer_key";
    String value = "consumer123";
    request.addOAuthParameter(key, value);

    Assert.assertEquals(value, request.getOauthParameters().get(key));
  }

  @Test
  public void testAddOAuthParameterKeyCheck() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");

    try {
      request.addOAuthParameter("invalid_key", "value");
      Assert.fail("Expected IllegalArgumentException was not thrown.");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
    }
  }

  @Test
  public void testGetOauthParameters() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
    request.addOAuthParameter("oauth_consumer_key", "consumer123");
    request.addOAuthParameter("scope", "read");

    Map<String, String> oauthParameters = request.getOauthParameters();

    Assert.assertEquals("consumer123", oauthParameters.get("oauth_consumer_key"));
    Assert.assertEquals("read", oauthParameters.get("scope"));
  }

  @Test
  public void testToString() {
    Verb verb = Verb.POST;
    String url = "https://www.example.com";
    OAuthRequest request = new OAuthRequest(verb, url);
    String expectedString = "@OAuthRequest(POST, https://www.example.com)";

    Assert.assertEquals(expectedString, request.toString());
  }
}

