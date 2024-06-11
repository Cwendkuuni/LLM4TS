import org.junit.Test;
import static org.junit.Assert.*;

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
        Token token = new Token("abc", "123", "raw");
        assertEquals("raw", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNull() {
        Token token = new Token("abc", "123");
        token.getRawResponse();
    }
    
    @Test 
    public void testToString() {
        Token token = new Token("abc", "123");
        assertEquals("Token[abc , 123]", token.toString());
    }
}