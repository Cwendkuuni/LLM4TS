import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenConstructor_WithRawResponse_Null() {
        Token token = new Token("token", "secret");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testTokenConstructor_WithRawResponse_NotNull() {
        Token token = new Token("token", "secret", "rawResponse");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
    }

    @Test
    public void testGetRawResponse_WithNullValue() {
        Token token = new Token("token", "secret");
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("This token object was not constructed by scribe and does not have a rawResponse", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        Token token = new Token("token", "secret", "rawResponse");
        assertEquals("Token[token , secret]", token.toString());
    }
}