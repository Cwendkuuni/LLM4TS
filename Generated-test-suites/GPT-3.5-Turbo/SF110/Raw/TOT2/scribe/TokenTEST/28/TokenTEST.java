import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testConstructor() {
        Token token = new Token("token1", "secret1");
        assertEquals("token1", token.getToken());
        assertEquals("secret1", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructorWithRawResponse() {
        Token token = new Token("token1", "secret1", "rawResponse");
        assertEquals("token1", token.getToken());
        assertEquals("secret1", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
    }

    @Test
    public void testGetToken() {
        Token token = new Token("token1", "secret1");
        assertEquals("token1", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("token1", "secret1");
        assertEquals("secret1", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("token1", "secret1", "rawResponse");
        assertEquals("rawResponse", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        Token token = new Token("token1", "secret1");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("token1", "secret1");
        assertEquals("Token[token1 , secret1]", token.toString());
    }
}