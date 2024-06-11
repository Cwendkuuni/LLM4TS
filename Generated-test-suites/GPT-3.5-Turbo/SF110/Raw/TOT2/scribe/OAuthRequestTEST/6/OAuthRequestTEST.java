import org.junit.Test;
import org.junit.Assert;
import java.util.Map;

public class OAuthRequestTest {
  
  @Test
  public void testConstructor() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
    Assert.assertEquals(Verb.GET, request.getVerb());
    Assert.assertEquals("http://www.example.com", request.getUrl());
  }

  @Test
  public void testAddOAuthParameter() {
    OAuthRequest request = new OAuthRequest(Verb.POST, "http://www.test.com");
    request.addOAuthParameter("oauth_token", "12345");
    Map<String, String> oauthParams = request.getOauthParameters();
    Assert.assertTrue(oauthParams.containsKey("oauth_token"));
    Assert.assertEquals("12345", oauthParams.get("oauth_token"));
  }

  @Test
  public void testAddOAuthParameterException() {
    OAuthRequest request = new OAuthRequest(Verb.DELETE, "http://www.error.com");
    try {
      request.addOAuthParameter("invalid", "value");
      Assert.fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void testGetOauthParameters() {
    OAuthRequest request = new OAuthRequest(Verb.PUT, "http://www.test.com");
    request.addOAuthParameter("scope", "read");
    Map<String, String> oauthParams = request.getOauthParameters();
    Assert.assertTrue(oauthParams.containsKey("scope"));
    Assert.assertEquals("read", oauthParams.get("scope"));
  }

  @Test
  public void testToString() {
    OAuthRequest request = new OAuthRequest(Verb.POST, "http://www.example.com");
    String expected = "@OAuthRequest(POST, http://www.example.com)";
    Assert.assertEquals(expected, request.toString());
  }
}