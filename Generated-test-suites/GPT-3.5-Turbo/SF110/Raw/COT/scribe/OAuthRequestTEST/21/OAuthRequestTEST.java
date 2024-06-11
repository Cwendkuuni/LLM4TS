import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "12345");

        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertEquals("12345", oauthParameters.get("oauth_token"));
    }

    @Test
    public void testCheckKey_ValidKey_ReturnsKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        String key = request.checkKey("oauth_token");

        Assert.assertEquals("oauth_token", key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_InvalidKey_ThrowsIllegalArgumentException() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        request.checkKey("invalid_key");
    }

    @Test
    public void testGetOauthParameters_NoParameters_ReturnsEmptyMap() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        Map<String, String> oauthParameters = request.getOauthParameters();

        Assert.assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testToString_ReturnsFormattedString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        String toString = request.toString();

        Assert.assertEquals("@OAuthRequest(GET, http://example.com)", toString);
    }

}