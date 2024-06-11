import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        Verb verb = Verb.GET;
        String url = "http://www.example.com";
        OAuthRequest request = new OAuthRequest(verb, url);
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertNotNull(request.getOauthParameters());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("key", "value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.containsKey("key"));
        assertEquals("value", oauthParameters.get("key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("invalid", "value");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals(0, oauthParameters.size());
    }
    
    @Test
    public void testToString() {
        Verb verb = Verb.POST;
        String url = "http://www.example.com";
        OAuthRequest request = new OAuthRequest(verb, url);
        String expectedString = "@OAuthRequest(POST, http://www.example.com)";
        assertEquals(expectedString, request.toString());
    }
}