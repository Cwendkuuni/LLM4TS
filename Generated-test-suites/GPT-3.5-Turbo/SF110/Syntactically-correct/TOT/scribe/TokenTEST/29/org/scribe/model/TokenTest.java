import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {

    private Token token;

    @Before
    public void setUp() {
        token = new Token("abcd1234", "5678efgh");
    }

    @Test
    public void testGetToken() {
        assertEquals("abcd1234", token.getToken());
    }

    @Test
    public void testGetSecret() {
        assertEquals("5678efgh", token.getSecret());
    }

    @Test
    public void testGetRawResponse_Null() {
        assertNull(token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_NotConstructedByScribe() {
        Token token = new Token("ijkl5678", "9012mnop", null);
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        String expected = "Token[abcd1234 , 5678efgh]";
        assertEquals(expected, token.toString());
    }
}