import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {
    Token token;

    @Before
    public void setUp() {
        token = new Token("token", "secret");
    }

    @Test
    public void testGetToken() {
        String expected = "token";
        String actual = token.getToken();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSecret() {
        String expected = "secret";
        String actual = token.getSecret();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRawResponse() {
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            String expected = "This token object was not constructed by scribe and does not have a rawResponse";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testToString() {
        String expected = "Token[token , secret]";
        String actual = token.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTokenConstructorWithRawResponse() {
        String expectedToken = "token";
        String expectedSecret = "secret";
        String expectedRawResponse = "response";
        token = new Token(expectedToken, expectedSecret, expectedRawResponse);

        String actualToken = token.getToken();
        String actualSecret = token.getSecret();
        String actualRawResponse = token.getRawResponse();

        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
        assertEquals(expectedRawResponse, actualRawResponse);
    }
}