package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenConstructorWithoutRawResponse() {
        Token token = new Token("tokenValue", "secretValue");
        
        assertEquals("tokenValue", token.getToken());
        assertEquals("secretValue", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testTokenConstructorWithRawResponse() {
        Token token = new Token("tokenValue", "secretValue", "rawResponseValue");
        
        assertEquals("tokenValue", token.getToken());
        assertEquals("secretValue", token.getSecret());
        assertEquals("rawResponseValue", token.getRawResponse());
    }
    
    @Test
    public void testGetRawResponseWithNullRawResponse() {
        Token token = new Token("tokenValue", "secretValue");
        
        try {
            token.getRawResponse();
            fail("getRawResponse() did not throw an exception when rawResponse is null");
        } catch (IllegalStateException e) {
            assertEquals("This token object was not constructed by scribe and does not have a rawResponse", e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        Token token = new Token("tokenValue", "secretValue", "rawResponseValue");
        
        assertEquals("Token[tokenValue , secretValue]", token.toString());
    }
}