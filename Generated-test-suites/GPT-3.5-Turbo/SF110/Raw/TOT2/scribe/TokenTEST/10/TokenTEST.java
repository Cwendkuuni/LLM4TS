package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testConstructorWithTwoParams() {
        Token token = new Token("myToken", "mySecret");
        assertNotNull(token);
        assertEquals("myToken", token.getToken());
        assertEquals("mySecret", token.getSecret());
        assertNull(token.getRawResponse());
        assertEquals("Token[myToken , mySecret]", token.toString());
    }
    
    @Test
    public void testConstructorWithThreeParams() {
        Token token = new Token("myToken", "mySecret", "rawResponse");
        assertNotNull(token);
        assertEquals("myToken", token.getToken());
        assertEquals("mySecret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
        assertEquals("Token[myToken , mySecret]", token.toString());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNull() {
        Token token = new Token("myToken", "mySecret");
        token.getRawResponse();
    }

}