import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("abc", "123");
        assertEquals("abc", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("abc", "123");
        assertEquals("123", token.getSecret());
    }

    @Test
    public void testGetRawResponse_Null() {
        Token token = new Token("abc", "123");
        assertNull(token.getRawResponse());
    }

    @Test
    public void testGetRawResponse_NotNull() {
        Token token = new Token("abc", "123", "response");
        assertEquals("response", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_Exception() {
        Token token = new Token("abc", "123");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("abc", "123");
        assertEquals("Token[abc , 123]", token.toString());
    }
}