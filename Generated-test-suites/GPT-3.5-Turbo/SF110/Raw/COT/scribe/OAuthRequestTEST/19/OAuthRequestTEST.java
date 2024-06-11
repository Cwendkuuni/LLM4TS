
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

  @Test
  public void testAddOAuthParameter() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
    request.addOAuthParameter("oauth_token", "dummyToken");
    assertEquals("dummyToken", request.getOauthParameters().get("oauth_token"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddInvalidOAuthParameter() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
    request.addOAuthParameter("invalid_key", "dummyValue");
  }
  
  @Test
  public void testGetOauthParameters() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
    assertEquals(0, request.getOauthParameters().size());
  }

  @Test
  public void testToString() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
    assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
  }

}
