import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("testToken", "testSecret");
        Assert.assertEquals("testToken", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("testToken", "testSecret");
        Assert.assertEquals("testSecret", token.getSecret());
    }

    @Test
    public void testGetRawResponse_Exception() {
        Token token = new Token("testToken", "testSecret");
        try {
            token.getRawResponse();
            Assert.fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            Assert.assertEquals("This token object was not constructed by scribe and does not have a rawResponse", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        Assert.assertEquals("Token[testToken , testSecret]", token.toString());
    }

}