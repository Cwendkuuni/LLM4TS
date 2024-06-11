import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testGetToken() {
        Token token = new Token("test", "123");
        assertEquals("test", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("test", "123");
        assertEquals("123", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("test", "123", "response");
        assertEquals("response", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("test", "123");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("test", "123");
        assertEquals("Token[test , 123]", token.toString());
    }
}