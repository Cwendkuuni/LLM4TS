import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    // Test case for constructor 1
    @Test
    public void testConstructor1() {
        Token token = new Token("12345", "abcdef");
        Assert.assertEquals("12345", token.getToken());
        Assert.assertEquals("abcdef", token.getSecret());
        Assert.assertNull(token.getRawResponse());
    }
    
    // Test case for constructor 2
    @Test
    public void testConstructor2() {
        Token token = new Token("12345", "abcdef", "rawResponse");
        Assert.assertEquals("12345", token.getToken());
        Assert.assertEquals("abcdef", token.getSecret());
        Assert.assertEquals("rawResponse", token.getRawResponse());
    }
    
    // Test case for getRawResponse with null rawResponse
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNull() {
        Token token = new Token("12345", "abcdef");
        token.getRawResponse();
    }
    
    // Test case for toString
    @Test
    public void testToString() {
        Token token = new Token("12345", "abcdef");
        Assert.assertEquals("Token[12345 , abcdef]", token.toString());
    }
}