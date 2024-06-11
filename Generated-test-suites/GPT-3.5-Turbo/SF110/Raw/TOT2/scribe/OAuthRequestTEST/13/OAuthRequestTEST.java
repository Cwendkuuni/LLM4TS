import org.junit.Test;
import org.scribe.model.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("testKey", "testValue");
        assertEquals("testValue", request.getOauthParameters().get("testKey"));
    }
    
    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        try {
            OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
            request.addOAuthParameter("invalidKey", "invalidValue");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.PUT, "http://example.com");
        assertTrue(request.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, "http://example.com");
        assertEquals("@OAuthRequest(DELETE, http://example.com)", request.toString());
    }
}