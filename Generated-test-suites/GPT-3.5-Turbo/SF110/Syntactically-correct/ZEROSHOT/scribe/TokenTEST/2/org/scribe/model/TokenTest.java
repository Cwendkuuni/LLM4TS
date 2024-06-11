import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("test_token", "test_secret");
        assertEquals("test_token", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("test_token", "test_secret");
        assertEquals("test_secret", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("test_token", "test_secret", "raw_response");
        assertEquals("raw_response", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseThrowsException() {
        Token token = new Token("test_token", "test_secret");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("test_token", "test_secret");
        assertEquals("Token[test_token , test_secret]", token.toString());
    }
}