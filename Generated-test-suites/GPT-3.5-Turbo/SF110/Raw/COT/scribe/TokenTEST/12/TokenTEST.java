import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("tok123", "sec123");
        assertEquals("tok123", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("tok123", "sec123");
        assertEquals("sec123", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token1 = new Token("tok123", "sec123", "response123");
        assertEquals("response123", token1.getRawResponse());
        
        Token token2 = new Token("tok123", "sec123");
        try {
            token2.getRawResponse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testToString() {
        Token token = new Token("tok123", "sec123");
        assertEquals("Token[tok123 , sec123]", token.toString());
    }
}