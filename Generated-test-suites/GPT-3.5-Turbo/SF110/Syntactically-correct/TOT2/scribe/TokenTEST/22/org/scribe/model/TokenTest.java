package org.scribe.model;
import org.junit.Test;
import org.junit.Assert;

public class TokenTest {

    @Test
    public void testConstructorWithTwoParameters() {
        Token token = new Token("token123", "secret123");
        Assert.assertEquals("token123", token.getToken());
        Assert.assertEquals("secret123", token.getSecret());
        Assert.assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructorWithThreeParameters() {
        Token token = new Token("token456", "secret456", "rawResponse");
        Assert.assertEquals("token456", token.getToken());
        Assert.assertEquals("secret456", token.getSecret());
        Assert.assertEquals("rawResonse", token.getRawResponse());
    }

    @Test
    public void testGetToken() {
        Token token = new Token("token789", "secret789");
        Assert.assertEquals("token789", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("token999", "secret999");
        Assert.assertEquals("secret999", token.getSecret());
    }

    @Test
    public void testGetRawResponse_WithNull() {
        Token token = new Token("token000", "secret000");
        try {
            token.getRawResponse();
            Assert.fail("Expected an IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        Token token = new Token("token111", "secret111");
        Assert.assertEquals("Token[token111 , secret111]", token.toString());
    }
}