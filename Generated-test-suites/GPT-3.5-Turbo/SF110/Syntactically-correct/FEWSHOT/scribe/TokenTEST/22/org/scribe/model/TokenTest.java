
package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testConstructorWithTwoParams() {
        Token token = new Token("token", "secret");
        
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testConstructorWithThreeParams() {
        Token token = new Token("token", "secret", "rawResponse");
        
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("token", "secret");
        
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("token", "secret");
        
        assertEquals("Token[token , secret]", token.toString());
    }
    
}
