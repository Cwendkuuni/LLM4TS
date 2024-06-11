package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testConstructor_withTokenAndSecret() {
        Token token = new Token("token_value", "secret_value");
        assertEquals("token_value", token.getToken());
        assertEquals("secret_value", token.getSecret());
        assertNull(token.getRawResponse());
        assertEquals("Token[token_value , secret_value]", token.toString());
    }
    
    @Test
    public void testConstructor_withTokenSecretAndRawResponse() {
        Token token = new Token("token_value", "secret_value", "raw_response");
        assertEquals("token_value", token.getToken());
        assertEquals("secret_value", token.getSecret());
        assertEquals("raw_response", token.getRawResponse());
        assertEquals("Token[token_value , secret_value]", token.toString());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_withNullRawResponse() {
        Token token = new Token("token", "secret");
        token.getRawResponse();
    }
}