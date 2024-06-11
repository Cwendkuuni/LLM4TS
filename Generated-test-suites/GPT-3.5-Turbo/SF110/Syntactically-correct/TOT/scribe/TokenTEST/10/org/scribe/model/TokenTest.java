import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token token;

    @Before
    public void setUp() {
        token = new Token("token123", "secret123", "exampleResponse");
    }

    @Test
    public void testGetToken() {
        assertEquals("token123", token.getToken());
    }

    @Test
    public void testGetSecret() {
        assertEquals("secret123", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        assertEquals("exampleResponse", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_ThrowsException() {
        Token invalidToken = new Token("invalidToken", "invalidSecret");
        invalidToken.getRawResponse();
    }

    @Test
    public void testToString() {
        String expected = "Token[token123 , secret123]";
        assertEquals(expected, token.toString());
    }

}