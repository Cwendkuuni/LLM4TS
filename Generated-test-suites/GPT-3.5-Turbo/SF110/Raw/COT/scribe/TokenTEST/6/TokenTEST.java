
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    private Token token;
    private final String TEST_TOKEN = "testToken";
    private final String TEST_SECRET = "testSecret";
    private final String TEST_RAW_RESPONSE = "testRawResponse";
    
    @Before
    public void setUp() {
        token = new Token(TEST_TOKEN, TEST_SECRET, TEST_RAW_RESPONSE);
    }
    
    @Test
    public void testGetToken() {
        assertEquals(TEST_TOKEN, token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        assertEquals(TEST_SECRET, token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        assertEquals(TEST_RAW_RESPONSE, token.getRawResponse());
    }
    
    @Test
    public void testToString() {
        String expected = String.format("Token[%s , %s]", TEST_TOKEN, TEST_SECRET);
        assertEquals(expected, token.toString());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutResponse() {
        Token tokenWithoutResponse = new Token(TEST_TOKEN, TEST_SECRET);
        tokenWithoutResponse.getRawResponse();
    }
}