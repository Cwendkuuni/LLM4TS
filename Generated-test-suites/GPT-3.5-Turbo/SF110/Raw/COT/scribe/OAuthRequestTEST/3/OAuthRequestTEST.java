
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "12345");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertEquals("12345", oauthParameters.get("oauth_token"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String expectedString = "@OAuthRequest(GET, http://example.com)";
        assertEquals(expectedString, request.toString());
    }

}
