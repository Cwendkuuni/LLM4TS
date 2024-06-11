import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testConstructorWithTokenAndSecret() {
        Token token = new Token("dummyToken", "dummySecret");
        assertEquals("dummyToken", token.getToken());
        assertEquals("dummySecret", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructorWithTokenSecretAndRawResponse() {
        Token token = new Token("dummyToken", "dummySecret", "dummyRawResponse");
        assertEquals("dummyToken", token.getToken());
        assertEquals("dummySecret", token.getSecret());
        assertEquals("dummyRawResponse", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseThrowsExceptionWhenRawResponseIsNull() {
        Token token = new Token("dummyToken", "dummySecret");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("dummyToken", "dummySecret");
        assertEquals("Token[dummyToken , dummySecret]", token.toString());
    }
}