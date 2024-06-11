import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;

    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://example.com/api");
    }

    @Test
    public void testConstructorAndGetters() {
        Assert.assertEquals(Verb.GET, oauthRequest.getVerb());
        Assert.assertEquals("https://example.com/api", oauthRequest.getUrl());
    }

    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("oauth_token", "ABC123");
        oauthRequest.addOAuthParameter("scope", "read_write");

        Assert.assertEquals(2, oauthRequest.getOauthParameters().size());
        Assert.assertEquals("ABC123", oauthRequest.getOauthParameters().get("oauth_token"));
        Assert.assertEquals("read_write", oauthRequest.getOauthParameters().get("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        oauthRequest.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testToString() {
        Assert.assertEquals("@OAuthRequest(GET, https://example.com/api)", oauthRequest.toString());
    }

}