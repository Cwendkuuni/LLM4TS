import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testGetToken() {
        Token token = new Token("access_token", "secret");
        assertEquals("access_token", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("access_token", "secret");
        assertEquals("secret", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("access_token", "secret", "response");
        assertEquals("response", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("access_token", "secret");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("access_token", "secret");
        String expected = "Token[access_token , secret]";
        assertEquals(expected, token.toString());
    }
}