import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Verb;
import java.util.*;

public class OAuthRequestTest {

	@Test
	public void testConstructor() {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
		assertEquals(Verb.GET, request.getVerb());
		assertEquals("https://example.com", request.getUrl());
		assertEquals(new HashMap<String, String>(), request.getOauthParameters());
	}

	@Test
	public void testAddOAuthParameter() {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

		request.addOAuthParameter("oauth_token", "123456");
		request.addOAuthParameter("scope", "read");
		request.addOAuthParameter("custom_param", "custom_value");

		Map<String, String> expectedParameters = new HashMap<String, String>();
		expectedParameters.put("oauth_token", "123456");
		expectedParameters.put("scope", "read");
		expectedParameters.put("custom_param", "custom_value");

		assertEquals(expectedParameters, request.getOauthParameters());
	}

	@Test
	public void testCheckKeyValidKey() {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

		String validKey = "oauth_consumer_key";
		assertEquals(validKey, request.checkKey(validKey));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckKeyInvalidKey() {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

		String invalidKey = "invalid_key";
		request.checkKey(invalidKey);
	}

	@Test
	public void testToString() {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
		assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
	}

}