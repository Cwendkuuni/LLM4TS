import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testConstructor() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("testToken", token.getToken());
        assertEquals("testSecret", token.getSecret());
        assertNull(token.getRawResponse());
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
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("testToken", "testSecret");
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetRawResponseWithRawResponse() {
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        assertEquals("testRawResponse", token.getRawResponse());
    }

    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("Token[testToken , testSecret]", token.toString());
    }
}