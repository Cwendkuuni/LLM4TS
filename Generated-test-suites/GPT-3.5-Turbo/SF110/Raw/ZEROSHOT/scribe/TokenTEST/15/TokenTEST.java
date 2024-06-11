import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token token;
    private final String testToken = "testToken";
    private final String testSecret = "testSecret";
    private final String testRawResponse = "testRawResponse";
    
    @Before
    public void setUp() {
        token = new Token(testToken, testSecret, testRawResponse);
    }
    
    @Test
    public void testGetToken() {
        assertEquals(testToken, token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        assertEquals(testSecret, token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        assertEquals(testRawResponse, token.getRawResponse());
    }
    
    @Test
    public void testToString() {
        String expected = String.format("Token[%s , %s]", testToken, testSecret);
        assertEquals(expected, token.toString());
    }
    
    @Test
    public void testConstructTokenWithNullRawResponse() {
        token = new Token(testToken, testSecret);
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("This token object was not constructed by scribe and does not have a rawResponse", e.getMessage());
        }
    }
}