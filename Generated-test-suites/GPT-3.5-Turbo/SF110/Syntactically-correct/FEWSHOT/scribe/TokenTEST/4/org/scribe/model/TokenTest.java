package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TokenTest {
    
    private Token token;
    
    @Before
    public void setup() {
        token = new Token("testToken", "testSecret", "testRawResponse");
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(token);
        assertEquals("testToken", token.getToken());
        assertEquals("testSecret", token.getSecret());
        assertEquals("testRawResponse", token.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        assertEquals("testToken", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        assertEquals("testSecret", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        assertEquals("testRawResponse", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token tokenWithoutRawResponse = new Token("testToken", "testSecret");
        tokenWithoutRawResponse.getRawResponse();
    }
    
    @Test
    public void testToString() {
        assertEquals("Token[testToken , testSecret]", token.toString());
    }
}