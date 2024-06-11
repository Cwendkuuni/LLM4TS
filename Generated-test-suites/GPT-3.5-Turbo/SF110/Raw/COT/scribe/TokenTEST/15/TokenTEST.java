import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("dummy_token", "dummy_secret");
        assertEquals("dummy_token", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("dummy_token", "dummy_secret");
        assertEquals("dummy_secret", token.getSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse() {
        Token token = new Token("dummy_token", "dummy_secret");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("dummy_token", "dummy_secret");
        String expected = "Token[dummy_token , dummy_secret]";
        assertEquals(expected, token.toString());
    }
}