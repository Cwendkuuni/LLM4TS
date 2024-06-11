import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    // Test getToken() method
    @Test
    public void testGetToken() {
        Token token = new Token("abc123", "xyz456");
        assertEquals("abc123", token.getToken());
    }

    // Test getSecret() method
    @Test
    public void testGetSecret() {
        Token token = new Token("abc123", "xyz456");
        assertEquals("xyz456", token.getSecret());
    }

    // Test getRawResponse() with rawResponse set
    @Test
    public void testGetRawResponseWithRawResponseSet() {
        Token token = new Token("abc123", "xyz456", "Sample raw response");
        assertEquals("Sample raw response", token.getRawResponse());
    }

    // Test getRawResponse() without rawResponse set
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponseSet() {
        Token token = new Token("abc123", "xyz456");
        token.getRawResponse();
    }

    // Test toString() method
    @Test
    public void testToString() {
        Token token = new Token("abc123", "xyz456");
        assertEquals("Token[abc123 , xyz456]", token.toString());
    }

    // Test constructor with only token and secret
    @Test
    public void testConstructorWithTokenAndSecret() {
        Token token = new Token("abc123", "xyz456");
        assertEquals("abc123", token.getToken());
        assertEquals("xyz456", token.getSecret());
    }

    // Test constructor with token, secret, and rawResponse
    @Test
    public void testConstructorWithTokenSecretAndRawResponse() {
        Token token = new Token("abc123", "xyz456", "Sample raw response");
        assertEquals("abc123", token.getToken());
        assertEquals("xyz456", token.getSecret());
        assertEquals("Sample raw response", token.getRawResponse());
    }
}