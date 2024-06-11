import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testGetToken() {
        Token token = new Token("token", "secret");
        String expectedToken = "token";
        assertEquals(expectedToken, token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("token", "secret");
        String expectedSecret = "secret";
        assertEquals(expectedSecret, token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("token", "secret", "rawResponse");
        String expectedRawResponse = "rawResponse";
        assertEquals(expectedRawResponse, token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseThrowsException() {
        Token token = new Token("token", "secret");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("token", "secret");
        String expectedString = "Token[token , secret]";
        assertEquals(expectedString, token.toString());
    }
}