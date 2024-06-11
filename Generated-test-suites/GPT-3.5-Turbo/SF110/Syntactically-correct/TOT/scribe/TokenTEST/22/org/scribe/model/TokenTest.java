import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void getToken_validToken_returnToken() {
        String token = "abc";
        String secret = "123";
        Token myToken = new Token(token, secret);
        
        assertEquals(token, myToken.getToken());
    }
    
    @Test
    public void getSecret_validSecret_returnSecret() {
        String token = "abc";
        String secret = "123";
        Token myToken = new Token(token, secret);
        
        assertEquals(secret, myToken.getSecret());
    }
    
    @Test
    public void getRawResponse_rawResponseIsNull_throwException() {
        String token = "abc";
        String secret = "123";
        Token myToken = new Token(token, secret);
        
        try {
            myToken.getRawResponse();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
    
    @Test
    public void getRawResponse_rawResponseIsNotNull_returnRawResponse() {
        String token = "abc";
        String secret = "123";
        String rawResponse = "response";
        Token myToken = new Token(token, secret, rawResponse);
        
        assertEquals(rawResponse, myToken.getRawResponse());
    }
    
    @Test
    public void toString_validTokenAndSecret_returnFormattedString() {
        String token = "abc";
        String secret = "123";
        Token myToken = new Token(token, secret);
        
        assertEquals("Token[abc , 123]", myToken.toString());
    }
    
}