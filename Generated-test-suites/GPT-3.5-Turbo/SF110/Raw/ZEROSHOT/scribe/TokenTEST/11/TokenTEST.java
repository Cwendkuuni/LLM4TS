import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    Token token;

    @Before
    public void setUp() {
        token = new Token("test_token", "test_secret", "test_rawResponse");
    }

    @Test
    public void testGetToken() {
        assertEquals("test_token", token.getToken());
    }

    @Test
    public void testGetSecret() {
        assertEquals("test_secret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        assertEquals("test_rawResponse", token.getRawResponse());
    }

    @Test
    public void testToString() {
        assertEquals("Token[test_token , test_secret]", token.toString());
    }
}