
import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {

    // Test case for the constructor with token and secret parameters
    @Test
    public void testConstructorWithTokenAndSecret() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("testToken", token.getToken());
        assertEquals("testSecret", token.getSecret());
        assertNull(token.getRawResponse());
    }

    // Test case for the constructor with token, secret, and rawResponse parameters
    @Test
    public void testConstructorWithTokenSecretRawResponse() {
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        assertEquals("testToken", token.getToken());
        assertEquals("testSecret", token.getSecret());
        assertEquals("testRawResponse", token.getRawResponse());
    }

    // Test case for getToken() method
    @Test
    public void testGetToken() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("testToken", token.getToken());
    }

    // Test case for getSecret() method
    @Test
    public void testGetSecret() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("testSecret", token.getSecret());
    }

    // Test case for getRawResponse() method with valid rawResponse
    @Test
    public void testGetRawResponseValid() {
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        assertEquals("testRawResponse", token.getRawResponse());
    }

    // Test case for getRawResponse() method with null rawResponse
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseNull() {
        Token token = new Token("testToken", "testSecret");
        token.getRawResponse();
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("Token[testToken , testSecret]", token.toString());
    }
}
