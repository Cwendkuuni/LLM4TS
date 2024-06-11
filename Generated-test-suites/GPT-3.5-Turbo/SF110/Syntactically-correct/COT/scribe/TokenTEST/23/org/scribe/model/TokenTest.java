
package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testGetToken() {
        Token token = new Token("abc123", "secret");
        Assert.assertEquals("abc123", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("abc123", "secret");
        Assert.assertEquals("secret", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("abc123", "secret", "response");
        Assert.assertEquals("response", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        Token token = new Token("abc123", "secret");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("abc123", "secret");
        Assert.assertEquals("Token[abc123 , secret]", token.toString());
    }
    
}
