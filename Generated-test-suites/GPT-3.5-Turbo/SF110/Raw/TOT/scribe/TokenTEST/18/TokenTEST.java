package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testConstructor1() {
        Token token = new Token("token1", "secret1");
        assertEquals("token1", token.getToken());
        assertEquals("secret1", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructor2() {
        Token token = new Token("token2", "secret2", "rawResponse2");
        assertEquals("token2", token.getToken());
        assertEquals("secret2", token.getSecret());
        assertEquals("rawResponse2", token.getRawResponse());
    }

    @Test
    public void testGetToken() {
        Token token = new Token("token3", "secret3");
        assertEquals("token3", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("token4", "secret4");
        assertEquals("secret4", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("token5", "secret5", "rawResponse5");
        assertEquals("rawResponse5", token.getRawResponse());
    }

    @Test
    public void testGetRawResponseException() {
        Token token = new Token("token6", "secret6");
        assertThrows(IllegalStateException.class, () -> {
            token.getRawResponse();
        });
    }

    @Test
    public void testToString() {
        Token token = new Token("token7", "secret7");
        String expected = "Token[token7 , secret7]";
        assertEquals(expected, token.toString());
    }

}