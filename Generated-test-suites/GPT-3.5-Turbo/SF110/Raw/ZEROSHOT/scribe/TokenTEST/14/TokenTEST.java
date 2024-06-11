import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

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
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        assertEquals("testRawResponse", token.getRawResponse());
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