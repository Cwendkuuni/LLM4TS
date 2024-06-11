import org.junit.Before;
import org.junit.Test;
import org.scribe.model.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;

    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
    }

    @Test
    public void testAddOAuthParameter_typicalCase() {
        oauthRequest.addOAuthParameter("oauth_token", "example_token");
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("oauth_token", "example_token");
        assertEquals(expectedParameters, oauthRequest.getOauthParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_invalidKey() {
        oauthRequest.addOAuthParameter("invalid_key", "example_value");
    }

    @Test
    public void testCheckKey_typicalCase() {
        String validKey = "oauth_token";
        assertEquals(validKey, oauthRequest.checkKey(validKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        oauthRequest.checkKey("invalid_key");
    }

    @Test
    public void testCheckKey_scopeKey() {
        String scopeKey = "scope";
        assertEquals(scopeKey, oauthRequest.checkKey(scopeKey));
    }

    @Test
    public void testGetOauthParameters_emptyParameters() {
        Map<String, String> expectedParameters = new HashMap<>();
        assertEquals(expectedParameters, oauthRequest.getOauthParameters());
    }

    @Test
    public void testToString_typicalCase() {
        String expectedString = "@OAuthRequest(GET, https://www.example.com)";
        assertEquals(expectedString, oauthRequest.toString());
    }

    @Test
    public void testToString_postMethod() {
        oauthRequest = new OAuthRequest(Verb.POST, "https://www.example.com");
        String expectedString = "@OAuthRequest(POST, https://www.example.com)";
        assertEquals(expectedString, oauthRequest.toString());
    }
}