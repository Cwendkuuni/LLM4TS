package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""));
    }

    @Test
    public void testRead() {
        // Test the read method to check if a token is properly emitted
        Token token = tokeniser.read();
        assertNull(token); // initial read should return null
    }

    @Test
    public void testTokeniserState() {
        // Test the transition of tokeniser state
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state); // initial state should be Data
    }

    @Test
    public void testEmit() {
        // Test the emit method to ensure it sets the emit pending flag
        Token token = new Token();
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testCharacterReferenceError() {
        // Test the private method characterReferenceError to ensure it adds a parse error
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testError() {
        // Test the private method error to ensure it adds a parse error
        tokeniser.error("Test Error");
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testIsTrackErrors() {
        // Test the isTrackErrors method to check if track errors is correctly set
        assertTrue(tokeniser.isTrackErrors());
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test the currentNodeInHtmlNS method
        assertTrue(tokeniser.currentNodeInHtmlNS()); // dummy implementation, should be changed
    }
}