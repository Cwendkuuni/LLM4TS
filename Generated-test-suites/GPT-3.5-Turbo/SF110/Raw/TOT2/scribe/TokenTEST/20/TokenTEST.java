import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testConstructor() {
        Token token = new Token("token", "secret");
        assertNotNull(token);
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
    }
    
    @Test
    public void testConstructorWithRawResponse() {
        Token token = new Token("token", "secret", "rawResponse");
        assertNotNull(token);
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
    }
    
    @Test
    public void testGetRawResponseNull() {
        Token token = new Token("token", "secret");
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testToString() {
        Token token = new Token("token", "secret", "rawResponse");
        String expected = "Token[token , secret]";
        assertEquals(expected, token.toString());
    }

}