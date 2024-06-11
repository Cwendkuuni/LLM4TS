import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TokenTest {
    
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
        Token token = new Token("abc", "123", "response");
        
        assertEquals("response", token.getRawResponse());
    }
    
    @Test
    public void testGetRawResponseWithoutResponse() {
        Token token = new Token("abc", "123");
        
        assertThrows(IllegalStateException.class, () -> token.getRawResponse());
    }
    
    @Test
    public void testToString() {
        Token token = new Token("abc", "123");
        
        assertEquals("Token[abc , 123]", token.toString());
    }
}