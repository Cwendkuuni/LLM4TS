
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testGetToken() {
        Token token = new Token("abc123", "xyz789");
        assertEquals("abc123", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("abc123", "xyz789");
        assertEquals("xyz789", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("abc123", "xyz789", "rawData");
        assertEquals("rawData", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawData() {
        Token token = new Token("abc123", "xyz789");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("abc123", "xyz789");
        assertEquals("Token[abc123 , xyz789]", token.toString());
    }
}
