import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testGetToken() {
        Token token = new Token("12345", "67890");
        assertEquals("12345", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("12345", "67890");
        assertEquals("67890", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("12345", "67890", "raw");
        assertEquals("raw", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNull() {
        Token token = new Token("12345", "67890");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("12345", "67890");
        assertEquals("Token[12345 , 67890]", token.toString());
    }
    
}