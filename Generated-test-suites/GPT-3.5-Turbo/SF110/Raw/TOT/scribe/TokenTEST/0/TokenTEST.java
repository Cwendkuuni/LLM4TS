
package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TokenTest {
    
    private Token token;
    
    @Before
    public void setUp() {
        token = new Token("dummyToken", "dummySecret", "dummyResponse");
    }
    
    @Test
    public void testConstructor1() {
        assertEquals("dummyToken", token.getToken());
        assertEquals("dummySecret", token.getSecret());
        assertEquals("dummyResponse", token.getRawResponse());
    }
    
    @Test
    public void testConstructor2() {
        Token token2 = new Token("dummyToken2", "dummySecret2");
        assertEquals("dummyToken2", token2.getToken());
        assertEquals("dummySecret2", token2.getSecret());
        assertNull(token2.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        assertEquals("dummyToken", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        assertEquals("dummySecret", token.getSecret());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_Null() {
        Token token2 = new Token("dummyToken2", "dummySecret2");
        token2.getRawResponse();
    }
    
    @Test
    public void testGetRawResponse_NotNull() {
        assertEquals("dummyResponse", token.getRawResponse());
    }
    
    @Test
    public void testToString() {
        assertEquals("Token[dummyToken , dummySecret]", token.toString());
    }
    
}

