import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructor() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("testToken", token.getToken());
        assertEquals("testSecret", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testTokenWithRawResponseConstructor() {
        Token token = new Token("testToken", "testSecret", "rawResponse");
        assertEquals("testToken", token.getToken());
        assertEquals("testSecret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("testToken", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("testSecret", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("testToken", "testSecret", "rawResponse");
        assertEquals("rawResponse", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        Token token = new Token("testToken", "testSecret");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("Token[testToken , testSecret]", token.toString());
    }
}