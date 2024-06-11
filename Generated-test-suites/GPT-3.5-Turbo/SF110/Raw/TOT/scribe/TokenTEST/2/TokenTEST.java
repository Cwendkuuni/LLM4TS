import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    private Token token;
    
    @Before
    public void setUp() {
        token = new Token("dummy_token", "dummy_secret");
    }
    
    @After
    public void tearDown() {
        token = null;
    }
    
    @Test
    public void testConstructorWithTokenAndSecret() {
        assertEquals("dummy_token", token.getToken());
        assertEquals("dummy_secret", token.getSecret());
    }
    
    @Test
    public void testConstructorWithTokenSecretAndRawResponse() {
        token = new Token("dummy_token", "dummy_secret", "dummy_raw_response");
        assertEquals("dummy_token", token.getToken());
        assertEquals("dummy_secret", token.getSecret());
        assertEquals("dummy_raw_response", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenRawResponseIsNull() {
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        assertEquals("Token[dummy_token , dummy_secret]", token.toString());
    }
}