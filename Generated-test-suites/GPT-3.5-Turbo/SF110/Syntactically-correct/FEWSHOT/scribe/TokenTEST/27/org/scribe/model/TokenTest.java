package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructorWithTokenAndSecret() {
        String tokenStr = "abcd1234";
        String secretStr = "efgh5678";
        Token token = new Token(tokenStr, secretStr);
        
        assertEquals(tokenStr, token.getToken());
        assertEquals(secretStr, token.getSecret());
        assertNull(token.getRawResponse());
        assertEquals("Token[abcd1234 , efgh5678]", token.toString());
    }
    
    @Test
    public void testConstructorWithTokenSecretAndRawResponse() {
        String tokenStr = "abcd1234";
        String secretStr = "efgh5678";
        String rawResponseStr = "response1234";
        Token token = new Token(tokenStr, secretStr, rawResponseStr);
        
        assertEquals(tokenStr, token.getToken());
        assertEquals(secretStr, token.getSecret());
        assertEquals(rawResponseStr, token.getRawResponse());
        assertEquals("Token[abcd1234 , efgh5678]", token.toString());
    }
    
    @Test
    public void testGetRawResponseNotConstructedByScribe() {
        Token token = new Token("abcd1234", "efgh5678");
        
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException not thrown");
        } catch (IllegalStateException e) {
            assertEquals("This token object was not constructed by scribe and does not have a rawResponse", e.getMessage());
        }
    }
}