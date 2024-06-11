import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;

    @Before
    public void setup() {
        oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("key1", "value1");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();

        assertNotNull(oauthParameters);
        assertEquals(1, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("key1"));
        assertEquals("value1", oauthParameters.get("key1"));
    }

    @Test
    public void testAddOAuthParameterThrowsExceptionForInvalidKey() {
        try {
            oauthRequest.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }

        try {
            oauthRequest.addOAuthParameter("scope", "value");
            // No exception should be thrown for "scope"
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();

        assertNotNull(oauthParameters);
        assertEquals(0, oauthParameters.size());
    }

    @Test
    public void testToString() {
        String toStringResult = oauthRequest.toString();
        assertEquals("@OAuthRequest(GET, http://www.example.com)", toStringResult);
    }

}