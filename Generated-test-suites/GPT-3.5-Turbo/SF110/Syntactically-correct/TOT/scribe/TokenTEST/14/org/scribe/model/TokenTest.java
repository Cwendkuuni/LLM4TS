import org.junit.*;

public class TokenTest {

    // Test case for constructor with two parameters
    @Test
    public void testConstructorWithTwoParameters() {
        String token = "testToken";
        String secret = "testSecret";
        Token testToken = new Token(token, secret);
        
        Assert.assertEquals(token, testToken.getToken());
        Assert.assertEquals(secret, testToken.getSecret());
        Assert.assertNull(testToken.getRawResponse());
    }
    
    // Test case for constructor with three parameters
    @Test
    public void testConstructorWithThreeParameters() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token testToken = new Token(token, secret, rawResponse);
        
        Assert.assertEquals(token, testToken.getToken());
        Assert.assertEquals(secret, testToken.getSecret());
        Assert.assertEquals(rawResponse, testToken.getRawResponse());
    }
    
    // Test case for getToken()
    @Test
    public void testGetToken() {
        String token = "testToken";
        String secret = "testSecret";
        Token testToken = new Token(token, secret);
        
        Assert.assertEquals(token, testToken.getToken());
    }
    
    // Test case for getSecret()
    @Test
    public void testGetSecret() {
        String token = "testToken";
        String secret = "testSecret";
        Token testToken = new Token(token, secret);
        
        Assert.assertEquals(secret, testToken.getSecret());
    }
    
    // Test case for getRawResponse() when rawResponse is null
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseNull() {
        String token = "testToken";
        String secret = "testSecret";
        Token testToken = new Token(token, secret);
        
        testToken.getRawResponse();
    }
    
    // Test case for getRawResponse() when rawResponse is not null
    @Test
    public void testGetRawResponseNotNull() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token testToken = new Token(token, secret, rawResponse);
        
        Assert.assertEquals(rawResponse, testToken.getRawResponse());
    }
    
    // Test case for toString()
    @Test
    public void testToString() {
        String token = "testToken";
        String secret = "testSecret";
        Token testToken = new Token(token, secret);
        
        Assert.assertEquals("Token[testToken , testSecret]", testToken.toString());
    }
}