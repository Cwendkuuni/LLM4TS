import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("testKey", "testValue");
        assertEquals("testValue", request.getOauthParameters().get("testKey"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("invalidKey", "invalidValue");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("testKey", "testValue");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("testValue", oauthParameters.get("testKey"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String expected = "@OAuthRequest(GET, http://example.com)";
        assertEquals(expected, request.toString());
    }
}