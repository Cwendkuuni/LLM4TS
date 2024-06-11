import org.junit.*;

public class TokenTest {

    

    @Test
    public void testConstructorWithTwoArguments() {
        Token token = new Token("testToken", "testSecret");
        Assert.assertEquals("testToken", token.getToken());
        Assert.assertEquals("testSecret", token.getSecret());
        Assert.assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructorWithThreeArguments() {
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        Assert.assertEquals("testToken", token.getToken());
        Assert.assertEquals("testSecret", token.getSecret());
        Assert.assertEquals("testRawResponse", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("testToken", "testSecret");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        String expected = "Token[testToken , testSecret]";
        Assert.assertEquals(expected, token.toString());
    }

    

}