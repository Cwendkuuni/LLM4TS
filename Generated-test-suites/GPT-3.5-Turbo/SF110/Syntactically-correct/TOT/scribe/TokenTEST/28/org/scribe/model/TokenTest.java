import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {

    // Test cases for getToken()
    @Test
    public void testGetToken() {
        Token token = new Token("exampleToken", "exampleSecret");
        assertEquals("exampleToken", token.getToken());
    }

    // Test cases for getSecret()
    @Test
    public void testGetSecret() {
        Token token = new Token("exampleToken", "exampleSecret");
        assertEquals("exampleSecret", token.getSecret());
    }

    // Test cases for getRawResponse()
    @Test
    public void testGetRawResponse() {
        Token token = new Token("exampleToken", "exampleSecret", "exampleRawResponse");
        assertEquals("exampleRawResponse", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseNull() {
        Token token = new Token("exampleToken", "exampleSecret");
        token.getRawResponse();
    }

    // Test cases for toString()
    @Test
    public void testToString() {
        Token token = new Token("exampleToken", "exampleSecret");
        assertEquals("Token[exampleToken , exampleSecret]", token.toString());
    }

}