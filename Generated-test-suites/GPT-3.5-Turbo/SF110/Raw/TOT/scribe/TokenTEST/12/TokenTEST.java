import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testConstructor_withTokenAndSecret_validTokenAndSecret() {
        // Arrange
        String tokenValue = "abcd1234";
        String secretValue = "efgh5678";
        
        // Act
        Token token = new Token(tokenValue, secretValue);
        
        // Assert
        Assert.assertEquals(tokenValue, token.getToken());
        Assert.assertEquals(secretValue, token.getSecret());
        Assert.assertNull(token.getRawResponse());
    }
    
    @Test
    public void testConstructor_withTokenSecretAndRawResponse_validTokenSecretAndRawResponse() {
        // Arrange
        String tokenValue = "abcd1234";
        String secretValue = "efgh5678";
        String rawResponseValue = "Test Raw Response";
        
        // Act
        Token token = new Token(tokenValue, secretValue, rawResponseValue);
        
        // Assert
        Assert.assertEquals(tokenValue, token.getToken());
        Assert.assertEquals(secretValue, token.getSecret());
        Assert.assertEquals(rawResponseValue, token.getRawResponse());
    }
    
    @Test
    public void testGetToken_withTokenSet_returnToken() {
        // Arrange
        String tokenValue = "abcd1234";
        Token token = new Token(tokenValue, "efgh5678");
        
        // Act
        String returnedToken = token.getToken();
        
        // Assert
        Assert.assertEquals(tokenValue, returnedToken);
    }
    
    @Test
    public void testGetSecret_withSecretSet_returnSecret() {
        // Arrange
        String secretValue = "efgh5678";
        Token token = new Token("abcd1234", secretValue);
        
        // Act
        String returnedSecret = token.getSecret();
        
        // Assert
        Assert.assertEquals(secretValue, returnedSecret);
    }
    
    @Test
    public void testGetRawResponse_withRawResponseSet_returnRawResponse() {
        // Arrange
        String rawResponseValue = "Test Raw Response";
        Token token = new Token("abcd1234", "efgh5678", rawResponseValue);
        
        // Act
        String returnedRawResponse = token.getRawResponse();
        
        // Assert
        Assert.assertEquals(rawResponseValue, returnedRawResponse);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_withNullRawResponse_throwIllegalStateException() {
        // Arrange
        Token token = new Token("abcd1234", "efgh5678");
        
        // Act
        token.getRawResponse();
        
        // Assert
        // Expects IllegalStateException to be thrown
    }
    
    @Test
    public void testToString_withTokenAndSecret_returnFormattedString() {
        // Arrange
        String tokenValue = "abcd1234";
        String secretValue = "efgh5678";
        Token token = new Token(tokenValue, secretValue);
        
        // Act
        String toStringResult = token.toString();
        
        // Assert
        String expectedToString = String.format("Token[%s , %s]", tokenValue, secretValue);
        Assert.assertEquals(expectedToString, toStringResult);
    }
}