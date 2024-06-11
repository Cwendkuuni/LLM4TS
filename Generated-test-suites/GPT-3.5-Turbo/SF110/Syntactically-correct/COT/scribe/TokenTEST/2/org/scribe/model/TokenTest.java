import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("token123", "secret123");
        Assert.assertEquals("token123", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("token123", "secret123");
        Assert.assertEquals("secret123", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("token123", "secret123", "rawResponse123");
        Assert.assertEquals("rawResponse123", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("token123", "secret123");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("token123", "secret123");
        Assert.assertEquals("Token[token123 , secret123]", token.toString());
    }

}