import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    // Test case for constructor without rawResponse parameter
    @Test
    public void testConstructorWithoutRawResponse() {
        Token token = new Token("token123", "secret123");
        assertEquals("token123", token.getToken());
        assertEquals("secret123", token.getSecret());
        assertNull(token.getRawResponse());
    }

    // Test case for constructor with rawResponse parameter
    @Test
    public void testConstructorWithRawResponse() {
        Token token = new Token("token123", "secret123", "response123");
        assertEquals("token123", token.getToken());
        assertEquals("secret123", token.getSecret());
        assertEquals("response123", token.getRawResponse());
    }

    // Test case for getToken() method
    @Test
    public void testGetToken() {
        Token token = new Token("token123", "secret123");
        assertEquals("token123", token.getToken());
    }

    // Test case for getSecret() method
    @Test
    public void testGetSecret() {
        Token token = new Token("token123", "secret123");
        assertEquals("secret123", token.getSecret());
    }

    // Test case for getRawResponse() method with rawResponse present
    @Test
    public void testGetRawResponseWithRawResponse() {
        Token token = new Token("token123", "secret123", "response123");
        assertEquals("response123", token.getRawResponse());
    }

    // Test case for getRawResponse() method without rawResponse present
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("token123", "secret123");
        token.getRawResponse();
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        Token token = new Token("token123", "secret123");
        assertEquals("Token[token123 , secret123]", token.toString());
    }
}