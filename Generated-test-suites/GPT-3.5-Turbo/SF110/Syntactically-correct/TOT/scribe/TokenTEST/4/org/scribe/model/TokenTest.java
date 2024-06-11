import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testGetToken() {
        Token token = new Token("abc", "def");
        assertEquals("abc", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("abc", "def");
        assertEquals("def", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("abc", "def", "123");
        assertEquals("123", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_Exception() {
        Token token = new Token("abc", "def");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("abc", "def");
        assertEquals("Token[abc , def]", token.toString());
    }
    
}