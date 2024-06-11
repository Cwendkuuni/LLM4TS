package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;

public class OAuthRequestTest {

  @Test
  public void testAddOAuthParameter_typicalCases() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
    
    request.addOAuthParameter("oauth_token", "token123");
    request.addOAuthParameter("oauth_consumer_key", "consumerKey");
    
    Map<String, String> oauthParameters = request.getOauthParameters();
    assertEquals("token123", oauthParameters.get("oauth_token"));
    assertEquals("consumerKey", oauthParameters.get("oauth_consumer_key"));
  }
  
  @Test
  public void testAddOAuthParameter_edgeCases() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
    
    request.addOAuthParameter("oauth_token", "");
    request.addOAuthParameter("oauth_consumer_key", null);
    
    Map<String, String> oauthParameters = request.getOauthParameters();
    assertEquals("", oauthParameters.get("oauth_token"));
    assertNull(oauthParameters.get("oauth_consumer_key"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCheckKey_errorCases() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
    
    request.addOAuthParameter("invalid_key", "value");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCheckKey_edgeCases() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
    
    request.addOAuthParameter("scope", "value");
  }
  
  @Test
  public void testToString_typicalCase() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
    
    String expected = "@OAuthRequest(GET, http://www.example.com)";
    assertEquals(expected, request.toString());
  }
  
  @Test
  public void testToString_edgeCases() {
    OAuthRequest request = new OAuthRequest(Verb.GET, "");
    
    String expected = "@OAuthRequest(GET, )";
    assertEquals(expected, request.toString());
  }
}