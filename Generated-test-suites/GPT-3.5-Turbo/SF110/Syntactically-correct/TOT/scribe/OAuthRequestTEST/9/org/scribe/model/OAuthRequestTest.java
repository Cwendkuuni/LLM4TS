import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter_typical() {
        // Arrange
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
        
        // Act
        oauthRequest.addOAuthParameter("oauth_token", "token123");
        
        // Assert
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertTrue(oauthParameters.containsKey("oauth_token"));
        assertEquals("token123", oauthParameters.get("oauth_token"));
    }

    @Test
    public void testAddOAuthParameter_edge() {
        // Arrange
        OAuthRequest oauthRequest = new OAuthRequest(Verb.POST, "https://example.com");
        
        // Act
        oauthRequest.addOAuthParameter("scope", "read write");
        
        // Assert
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertTrue(oauthParameters.containsKey("scope"));
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_error() {
        // Arrange
        OAuthRequest oauthRequest = new OAuthRequest(Verb.POST, "https://example.com");
        
        // Act
        oauthRequest.addOAuthParameter("invalid_key", "value");
        
        // Expecting an exception to be thrown
    }

    @Test
    public void testCheckKey_typical() {
        // Arrange
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
        
        // Act
        String result = oauthRequest.checkKey("oauth_consumer_key");
        
        // Assert
        assertEquals("oauth_consumer_key", result);
    }

    @Test
    public void testCheckKey_edge() {
        // Arrange
        OAuthRequest oauthRequest = new OAuthRequest(Verb.POST, "https://example.com");
        
        // Act
        String result = oauthRequest.checkKey("scope");
        
        // Assert
        assertEquals("scope", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_error() {
        // Arrange
        OAuthRequest oauthRequest = new OAuthRequest(Verb.POST, "https://example.com");
        
        // Act
        oauthRequest.checkKey("invalid_key");
        
        // Expecting an exception to be thrown
    }
    
    @Test
    public void testToString_typical() {
        // Arrange
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
        
        // Act
        String result = oauthRequest.toString();
        
        // Assert
        assertEquals("@OAuthRequest(GET, https://example.com)", result);
    }
}