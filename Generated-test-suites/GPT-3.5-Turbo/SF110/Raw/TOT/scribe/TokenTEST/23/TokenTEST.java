import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("abc123", "secret");

        assertEquals("abc123", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("abc123", "secret");

        assertEquals("secret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("abc123", "secret", "response");

        assertEquals("response", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("abc123", "secret");

        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("abc123", "secret");

        assertEquals("Token[abc123 , secret]", token.toString());
    }
}