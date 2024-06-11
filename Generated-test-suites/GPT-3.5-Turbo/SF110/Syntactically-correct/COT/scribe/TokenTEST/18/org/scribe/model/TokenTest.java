import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testGetToken() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        
        assertEquals("testToken", tokenObj.getToken());
    }
    
    @Test
    public void testGetSecret() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        
        assertEquals("testSecret", tokenObj.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token tokenObj = new Token(token, secret, rawResponse);
        
        assertEquals("testRawResponse", tokenObj.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        
        tokenObj.getRawResponse();
    }
    
    @Test
    public void testToString() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        
        assertEquals("Token[testToken , testSecret]", tokenObj.toString());
    }
}