import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.Map;

import static org.junit.Assert.*;

public class OAuthRequestTest {

    private OAuthRequest oAuthRequest;

    @Before
    public void setUp() {
        oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
    }

    @Test
    public void testAddOAuthParameter_typicalCase() {
        oAuthRequest.addOAuthParameter("oauth_token", "12345");

        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertTrue(oauthParameters.containsKey("oauth_token"));
        assertEquals("12345", oauthParameters.get("oauth_token"));
    }

    @Test
    public void testAddOAuthParameter_edgeCase_KeyWithSamePrefix() {
        oAuthRequest.addOAuthParameter("oauth_consumer_key", "KEY1");
        oAuthRequest.addOAuthParameter("oauth_consumer_key_2", "KEY2");

        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
        assertTrue(oauthParameters.containsKey("oauth_consumer_key_2"));
        assertEquals("KEY1", oauthParameters.get("oauth_consumer_key"));
        assertEquals("KEY2", oauthParameters.get("oauth_consumer_key_2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_errorCase_InvalidKey() {
        oAuthRequest.addOAuthParameter("invalid_key", "VALUE");

        // Exception is expected to be thrown
    }

    @Test
    public void testGetOauthParameters_typicalCase() {
        oAuthRequest.addOAuthParameter("oauth_token", "12345");

        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_token"));
        assertEquals("12345", oauthParameters.get("oauth_token"));
    }

    @Test
    public void testToString_typicalCase() {
        String expected = "@OAuthRequest(GET, http://example.com)";

        String result = oAuthRequest.toString();

        assertEquals(expected, result);
    }
}