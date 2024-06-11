import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("access_token", "secret");
        Assert.assertEquals("access_token", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("access_token", "secret");
        Assert.assertEquals("secret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("access_token", "secret", "raw_response");
        Assert.assertEquals("raw_response", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNull() {
        Token token = new Token("access_token", "secret");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("access_token", "secret");
        String expectedString = "Token[access_token , secret]";
        Assert.assertEquals(expectedString, token.toString());
    }
}