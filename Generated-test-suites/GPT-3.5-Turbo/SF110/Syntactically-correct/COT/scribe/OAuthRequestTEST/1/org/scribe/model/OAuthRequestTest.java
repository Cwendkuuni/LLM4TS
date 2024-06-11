
import org.junit.*;
import java.util.*;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;

    @Before
    public void setup() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("key", "value");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        Assert.assertTrue(oauthParameters.containsKey("key"));
        Assert.assertEquals("value", oauthParameters.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        oauthRequest.addOAuthParameter("invalid_key", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKeyScope() {
        oauthRequest.addOAuthParameter("scope", "value");
    }
    
    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        Assert.assertNotNull(oauthParameters);
        Assert.assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testToString() {
        String toString = oauthRequest.toString();
        Assert.assertEquals("@OAuthRequest(GET, https://example.com)", toString);
    }

}
