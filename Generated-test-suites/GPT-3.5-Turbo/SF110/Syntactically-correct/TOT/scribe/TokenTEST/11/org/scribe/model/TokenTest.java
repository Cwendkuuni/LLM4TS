import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        Token token = new Token("token", "secret");

        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertNull(token.getRawResponse());
        assertEquals("Token[token , secret]", token.toString());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        Token token = new Token("token", "secret", "rawResponse");

        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
        assertEquals("Token[token , secret]", token.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenRawResponseIsNull() {
        Token token = new Token("token", "secret");

        token.getRawResponse();
    }

}