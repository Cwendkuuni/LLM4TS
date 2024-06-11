import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void getToken_shouldReturnCorrectToken() {
        Token token = new Token("access_token", "secret_token");
        Assert.assertEquals("access_token", token.getToken());
    }
    
    @Test
    public void getSecret_shouldReturnCorrectSecret() {
        Token token = new Token("access_token", "secret_token");
        Assert.assertEquals("secret_token", token.getSecret());
    }
    
    @Test
    public void getRawResponse_shouldThrowExceptionWhenRawResponseIsNull() {
        Token token = new Token("access_token", "secret_token");
        try {
            token.getRawResponse();
            Assert.fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            Assert.assertEquals("This token object was not constructed by scribe and does not have a rawResponse", e.getMessage());
        }
    }
    
    @Test
    public void getRawResponse_shouldReturnCorrectRawResponse() {
        Token token = new Token("access_token", "secret_token", "raw_response");
        Assert.assertEquals("raw_response", token.getRawResponse());
    }
    
    @Test
    public void toString_shouldReturnCorrectStringRepresentation() {
        Token token = new Token("access_token", "secret_token");
        Assert.assertEquals("Token[access_token , secret_token]", token.toString());
    }
}