import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("testKey", "testValue");
        assertTrue(request.getOauthParameters().containsKey("testKey"));
        assertEquals("testValue", request.getOauthParameters().get("testKey"));
    }

    @Test
    public void testCheckKeyValid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertEquals("oauth_test", request.checkKey("oauth_test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.checkKey("invalidKey");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertTrue(request.getOauthParameters().isEmpty());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertEquals("@OAuthRequest(GET, https://www.example.com)", request.toString());
    }
}