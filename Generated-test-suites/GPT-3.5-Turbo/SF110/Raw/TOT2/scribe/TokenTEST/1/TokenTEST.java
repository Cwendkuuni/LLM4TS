import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testConstructor() {
        Token token = new Token("abc", "123");
        assertEquals("abc", token.getToken());
        assertEquals("123", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testCustomConstructor() {
        Token token = new Token("def", "456", "raw");
        assertEquals("def", token.getToken());
        assertEquals("456", token.getSecret());
        assertEquals("raw", token.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        Token token = new Token("abc", "123");
        assertEquals("abc", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("abc", "123");
        assertEquals("123", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("abc", "123", "raw");
        assertEquals("raw", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseNull() {
        Token token = new Token("abc", "123");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("abc", "123");
        assertEquals("Token[abc , 123]", token.toString());
    }
}