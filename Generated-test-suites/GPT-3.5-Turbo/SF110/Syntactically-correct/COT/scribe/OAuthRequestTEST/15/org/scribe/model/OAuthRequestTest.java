import org.junit.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://www.example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        request.addOAuthParameter("param1", "value1");
        
        assertEquals(1, request.getOauthParameters().size());
        assertTrue(request.getOauthParameters().containsKey("param1"));
        assertEquals("value1", request.getOauthParameters().get("param1"));
    }
    
    @Test
    public void testAddOAuthParameter_IllegalKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException expected
        }
    }
    
    @Test
    public void testGetOauthParameters_EmptyMap() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        assertEquals("@OAuthRequest(GET, https://www.example.com)", request.toString());
    }
}