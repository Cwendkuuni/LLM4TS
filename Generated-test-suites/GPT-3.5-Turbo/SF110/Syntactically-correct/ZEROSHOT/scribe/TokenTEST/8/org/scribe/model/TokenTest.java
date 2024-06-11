import org.junit.*;
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
    public void testConstructorWithRawResponse() {
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        assertEquals("testToken", token.getToken());
        assertEquals("testSecret", token.getSecret());
        assertEquals("testRawResponse", token.getRawResponse());
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

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNull() {
        Token token = new Token("testToken", "testSecret");
        token.getRawResponse();
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        assertEquals("testRawResponse", token.getRawResponse());
    }

    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("Token[testToken , testSecret]", token.toString());
    }
}