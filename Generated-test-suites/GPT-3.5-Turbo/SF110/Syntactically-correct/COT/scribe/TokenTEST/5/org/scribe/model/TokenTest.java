import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Token;

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
    public void testGetRawResponse() {
        Token token = new Token("testToken", "testSecret", "testResponse");
        Assert.assertEquals("testResponse", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseNull() {
        Token token = new Token("testToken", "testSecret");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        Assert.assertEquals("Token[testToken , testSecret]", token.toString());
    }

}