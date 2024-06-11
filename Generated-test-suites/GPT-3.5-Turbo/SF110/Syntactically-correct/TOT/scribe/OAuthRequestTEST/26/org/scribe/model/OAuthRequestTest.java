
import static org.junit.Assert.*;
import org.junit.*;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

public class OAuthRequestTest {
  
  // Test case for the constructor
  @Test
  public void testConstructor() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/");
    
    assertEquals(Verb.GET, request.getVerb());
    assertEquals("http://example.com/", request.getUrl());
    assertNotNull(request.getOauthParameters());
    assertTrue(request.getOauthParameters().isEmpty());
  }
  
  // Test case for addOAuthParameter method - typical use case
  @Test
  public void testAddOAuthParameter_Typical() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/");
    request.addOAuthParameter("oauth_consumer_key", "consumer_key");
    
    assertEquals(1, request.getOauthParameters().size());
    assertTrue(request.getOauthParameters().containsKey("oauth_consumer_key"));
    assertEquals("consumer_key", request.getOauthParameters().get("oauth_consumer_key"));
  }
  
  // Test case for addOAuthParameter method - edge case (empty key)
  @Test(expected = IllegalArgumentException.class)
  public void testAddOAuthParameter_EmptyKey() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/");
    request.addOAuthParameter("", "value");
  }
    
  // Test case for addOAuthParameter method - error scenario (invalid key)
  @Test(expected = IllegalArgumentException.class)
  public void testAddOAuthParameter_InvalidKey() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/");
    request.addOAuthParameter("invalid_key", "value");
  }
  
  // Test case for checkKey method - typical use case (key starts with "oauth_")
  @Test
  public void testCheckKey_Typical() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/");
    
    String result = request.checkKey("oauth_consumer_key");
    assertEquals("oauth_consumer_key", result);
  }
  
  // Test case for checkKey method - typical use case (key equals "scope")
  @Test
  public void testCheckKey_EqualScope() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/");
    
    String result = request.checkKey("scope");
    assertEquals("scope", result);
  }
  
  // Test case for checkKey method - error scenario (key does not start with "oauth_" or equal "scope")
  @Test(expected = IllegalArgumentException.class)
  public void testCheckKey_Error() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/");
    
    request.checkKey("invalid_key");
  }
  
  // Test case for toString method
  @Test
  public void testToString() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/");
    
    String result = request.toString();
    assertEquals("@OAuthRequest(GET, http://example.com/)", result);
  }
}

