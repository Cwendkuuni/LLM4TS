import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;
    
    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("key1", "value1");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals("value1", oauthParameters.get("key1"));
    }

    @Test
    public void testAddOAuthParameterInvalidKey() {
        try {
            oauthRequest.addOAuthParameter("key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testToString() {
        String expected = "@OAuthRequest(GET, https://example.com)";
        String actual = oauthRequest.toString();
        assertEquals(expected, actual);
    }

}