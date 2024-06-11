import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.*;

import java.util.Map;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter_ValidKeyAndValue_Success() {
        // Arrange
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        
        // Act
        request.addOAuthParameter("oauth_token", "my_token");
        
        // Assert
        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertEquals("my_token", oauthParameters.get("oauth_token"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_InvalidKey_ExceptionThrown() {
        // Arrange
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        
        // Act
        request.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testCheckKey_ValidKey_ReturnsSameKey() {
        // Arrange
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        
        // Act
        String result = request.checkKey("scope");
        
        // Assert
        Assert.assertEquals("scope", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_InvalidKey_ExceptionThrown() {
        // Arrange
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        
        // Act
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testToString_ValidRequest_ReturnsCorrectStringRepresentation() {
        // Arrange
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        
        // Act
        String result = request.toString();
        
        // Assert
        Assert.assertEquals("@OAuthRequest(GET, http://www.example.com)", result);
    }
}