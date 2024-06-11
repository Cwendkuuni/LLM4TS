import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    // Test case for constructor without rawResponse parameter
    @Test
    public void testConstructorWithoutRawResponse() {
        Token token = new Token("token1", "secret1");
        assertEquals("token1", token.getToken());
        assertEquals("secret1", token.getSecret());
        assertNull(token.getRawResponse());
    }

    // Test case for constructor with rawResponse parameter
    @Test
    public void testConstructorWithRawResponse() {
        Token token = new Token("token2", "secret2", "raw");
        assertEquals("token2", token.getToken());
        assertEquals("secret2", token.getSecret());
        assertEquals("raw", token.getRawResponse());
    }

    // Test case for getRawResponse() when rawResponse is null
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNull() {
        Token token = new Token("token3", "secret3");
        token.getRawResponse();
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        Token token = new Token("token4", "secret4", "raw4");
        assertEquals("Token[token4 , secret4]", token.toString());
    }
}