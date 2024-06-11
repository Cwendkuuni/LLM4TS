import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("test_token", "test_secret");
        assertEquals("test_token", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("test_token", "test_secret");
        assertEquals("test_secret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("test_token", "test_secret", "test_raw_response");
        assertEquals("test_raw_response", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseInvalid() {
        Token token = new Token("test_token", "test_secret");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("test_token", "test_secret");
        assertEquals("Token[test_token , test_secret]", token.toString());
    }
    
    // More test cases for edge cases and exceptions can be added here
    
    
    
    // Generated test cases for all public methods
    
    @Test
    public void testConstructorWithTwoParams() {
        Token token = new Token("test_token", "test_secret");
        assertEquals("test_token", token.getToken());
        assertEquals("test_secret", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testConstructorWithThreeParams() {
        Token token = new Token("test_token", "test_secret", "test_raw_response");
        assertEquals("test_token", token.getToken());
        assertEquals("test_secret", token.getSecret());
        assertEquals("test_raw_response", token.getRawResponse());
    }
    
    @Test
    public void testToStringWithNoRawResponse() {
        Token token = new Token("test_token", "test_secret");
        assertEquals("Token[test_token , test_secret]", token.toString());
    }
    
    @Test
    public void testToStringWithRawResponse() {
        Token token = new Token("test_token", "test_secret", "test_raw_response");
        assertEquals("Token[test_token , test_secret]", token.toString());
    }
    
    
}