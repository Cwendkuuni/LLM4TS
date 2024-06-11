import org.junit.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {
  
  private OAuthRequest oauthRequest;
  
  @Before
  public void setUp() {
    oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");
  }
  
  @Test
  public void testAddOAuthParameter() {
    oauthRequest.addOAuthParameter("oauth_key", "oauth_value");
    assertEquals(1, oauthRequest.getOauthParameters().size());
    assertEquals("oauth_value", oauthRequest.getOauthParameters().get("oauth_key"));
  }
  
  @Test
  public void testAddOAuthParameterWithScopeKey() {
    oauthRequest.addOAuthParameter("scope", "scope_value");
    assertEquals(1, oauthRequest.getOauthParameters().size());
    assertEquals("scope_value", oauthRequest.getOauthParameters().get("scope"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddOAuthParameterWithInvalidKey() {
    oauthRequest.addOAuthParameter("invalid_key", "invalid_value");
  }
  
  @Test
  public void testGetOauthParameters() {
    assertTrue(oauthRequest.getOauthParameters().isEmpty());
    
    oauthRequest.addOAuthParameter("oauth_key", "oauth_value");
    assertEquals(1, oauthRequest.getOauthParameters().size());
    assertEquals("oauth_value", oauthRequest.getOauthParameters().get("oauth_key"));
    
    oauthRequest.addOAuthParameter("scope", "scope_value");
    assertEquals(2, oauthRequest.getOauthParameters().size());
    assertEquals("scope_value", oauthRequest.getOauthParameters().get("scope"));
  }
  
  @Test
  public void testToString() {
    assertEquals("@OAuthRequest(GET, http://www.example.com)", oauthRequest.toString());
  }
  
  @After
  public void tearDown() {
    oauthRequest = null;
  }
}