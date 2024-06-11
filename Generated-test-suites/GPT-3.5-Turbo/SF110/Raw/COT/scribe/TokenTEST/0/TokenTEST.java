import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    private final String token = "TOKEN";
    private final String secret = "SECRET";
    private final String rawResponse = "RAW_RESPONSE";
    
    @Test
    public void testGetToken() {
        Token t = new Token(token, secret);
        assertEquals(token, t.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token t = new Token(token, secret);
        assertEquals(secret, t.getSecret());
    }
    
    @Test
    public void testGetRawResponseWithRawResponse() {
        Token t = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, t.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token t = new Token(token, secret);
        t.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token t = new Token(token, secret);
        String expected = "Token[TOKEN , SECRET]";
        assertEquals(expected, t.toString());
    }
}

