import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {
  
    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
    }
  
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("key1", "value1");
        assertEquals("value1", request.getOauthParameters().get("key1"));
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("invalid", "value");
    }
  
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        Map<String, String> parameters = request.getOauthParameters();
        assertNotNull(parameters);
        assertTrue(parameters.isEmpty());
    }
  
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String expected = "@OAuthRequest(GET, https://example.com)";
        assertEquals(expected, request.toString());
    }
}