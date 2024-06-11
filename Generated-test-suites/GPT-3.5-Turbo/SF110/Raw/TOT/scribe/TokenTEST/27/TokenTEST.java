import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testConstructor1() {
        Token token = new Token("token", "secret");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructor2() {
        Token token = new Token("token", "secret", "rawResponse");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
    }

    @Test
    public void testGetToken() {
        Token token = new Token("token", "secret");
        assertEquals("token", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("token", "secret");
        assertEquals("secret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("token", "secret", "rawResponse");
        assertEquals("rawResponse", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutResponse() {
        Token token = new Token("token", "secret");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("token", "secret");
        assertEquals("Token[token , secret]", token.toString());
    }
}