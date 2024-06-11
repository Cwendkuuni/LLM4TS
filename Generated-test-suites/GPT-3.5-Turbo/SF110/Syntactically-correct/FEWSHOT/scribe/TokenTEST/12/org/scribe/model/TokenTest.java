package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructor() {
        Token token = new Token("TOKEN", "SECRET");
        assertEquals("TOKEN", token.getToken());
        assertEquals("SECRET", token.getSecret());
        assertNotNull(token.getRawResponse());
        
        Token tokenWithRawResponse = new Token("TOKEN", "SECRET", "RAW_RESPONSE");
        assertEquals("TOKEN", tokenWithRawResponse.getToken());
        assertEquals("SECRET", tokenWithRawResponse.getSecret());
        assertEquals("RAW_RESPONSE", tokenWithRawResponse.getRawResponse());
    }
    
    @Test
    public void testToString() {
        Token token = new Token("TOKEN", "SECRET");
        assertEquals("Token[TOKEN , SECRET]", token.toString());
        
        Token tokenWithRawResponse = new Token("TOKEN", "SECRET", "RAW_RESPONSE");
        assertEquals("Token[TOKEN , SECRET]", tokenWithRawResponse.toString());
    }
}