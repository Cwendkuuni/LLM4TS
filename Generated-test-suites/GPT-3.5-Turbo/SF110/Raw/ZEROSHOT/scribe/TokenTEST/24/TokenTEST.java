import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("12345", "abcdef");
        assertEquals("12345", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("12345", "abcdef");
        assertEquals("abcdef", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("12345", "abcdef", "raw");
        assertEquals("raw", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseThrowsException() {
        Token token = new Token("12345", "abcdef");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("12345", "abcdef");
        assertEquals("Token[12345 , abcdef]", token.toString());
    }

}