import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest
{
    @Test
    public void testTokenConstructor() {
        Token token = new Token("token", "secret");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
    }
    
    @Test
    public void testTokenConstructorWithRawResponse() {
        Token token = new Token("token", "secret", "rawResponse");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
    }
    
    @Test
    public void testGetRawResponseWithNullRawResponse() {
        Token token = new Token("token", "secret");
        try {
            token.getRawResponse();
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // exception expected
        }
    }
    
    @Test
    public void testToString() {
        Token token = new Token("token", "secret");
        assertEquals("Token[token , secret]", token.toString());
    }
}