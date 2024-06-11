import org.junit.*;
import java.util.*;

public class OAuthRequestTest {
    private OAuthRequest oauthRequest;
    
    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://api.example.com");
    }
    
    @Test
    public void testAddOAuthParameter_validKeyAndValue_success() {
        oauthRequest.addOAuthParameter("oauth_token", "abcdef123456");
        Map<String, String> oauthParams = oauthRequest.getOauthParameters();
        assertEquals(1, oauthParams.size());
        assertEquals("abcdef123456", oauthParams.get("oauth_token"));
    }
    
    @Test
    public void testAddOAuthParameter_invalidKey_throwIllegalArgumentException() {
        try {
            oauthRequest.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGetOauthParameters_emptyParameters_returnEmptyMap() {
        Map<String, String> oauthParams = oauthRequest.getOauthParameters();
        assertNotNull(oauthParams);
        assertTrue(oauthParams.isEmpty());
    }
    
    @Test
    public void testToString_validRequest_returnExpectedString() {
        String expectedString = "@OAuthRequest(GET, https://api.example.com)";
        assertEquals(expectedString, oauthRequest.toString());
    }
}