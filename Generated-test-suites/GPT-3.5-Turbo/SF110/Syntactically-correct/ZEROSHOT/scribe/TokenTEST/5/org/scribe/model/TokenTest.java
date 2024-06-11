import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("abc123", "123abc");
        assertEquals("abc123", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("abc123", "123abc");
        assertEquals("123abc", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("abc123", "123abc", "rawResponse");
        assertEquals("rawResponse", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseNull() {
        Token token = new Token("abc123", "123abc");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("abc123", "123abc");
        assertEquals("Token[abc123 , 123abc]", token.toString());
    }
}