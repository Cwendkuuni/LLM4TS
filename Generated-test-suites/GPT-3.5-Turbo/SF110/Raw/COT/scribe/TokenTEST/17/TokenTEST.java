import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testGetToken() {
        Token token = new Token("tokenValue", "secretValue");
        assertEquals("tokenValue", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("tokenValue", "secretValue");
        assertEquals("secretValue", token.getSecret());
    }
    
    @Test
    public void testGetRawResponseWithNullRawResponse() {
        Token token = new Token("tokenValue", "secretValue");
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testGetRawResponseWithNonNullRawResponse() {
        Token token = new Token("tokenValue", "secretValue", "rawResponseValue");
        assertEquals("rawResponseValue", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNullRawResponseException() {
        Token token = new Token("tokenValue", "secretValue");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("tokenValue", "secretValue");
        assertEquals("Token[tokenValue , secretValue]", token.toString());
    }
}