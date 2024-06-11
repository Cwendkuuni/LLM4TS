import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token token;

    @Before
    public void setUp() {
        // Create a new Token object for each test case
        token = new Token("testToken", "testSecret");
    }

    @Test
    public void testGetToken() {
        // Test the getToken() method
        String expectedToken = "testToken";
        assertEquals(expectedToken, token.getToken());
    }

    @Test
    public void testGetSecret() {
        // Test the getSecret() method
        String expectedSecret = "testSecret";
        assertEquals(expectedSecret, token.getSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNull() {
        // Test the getRawResponse() method with null value
        assertNull(token.getRawResponse());
    }

    @Test
    public void testGetRawResponse() {
        // Test the getRawResponse() method
        token = new Token("testToken", "testSecret", "testRawResponse");
        String expectedResponse = "testRawResponse";
        assertEquals(expectedResponse, token.getRawResponse());
    }

    @Test
    public void testToString() {
        // Test the toString() method
        String expectedString = "Token[testToken , testSecret]";
        assertEquals(expectedString, token.toString());
    }
}