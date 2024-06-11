package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);

        // TODO: Add more test cases for different token types
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(c, tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.TagName;
        tokeniser.transition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.TagName;
        tokeniser.advanceTransition(newState);
        assertEquals(newState, tokeniser.getState());
        assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.selfClosingFlagAcknowledged = false;
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.error(state);
        ParseErrorList errors = tokeniser.errors;
        assertTrue(errors.size() > 0);
        assertEquals("Unexpected character ' ' in input state [TagName]", errors.get(0).toString());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.eofError(state);
        ParseErrorList errors = tokeniser.errors;
        assertTrue(errors.size() > 0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [TagName]", errors.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference: numeric reference with no numerals";
        tokeniser.characterReferenceError(message);
        ParseErrorList errors = tokeniser.errors;
        assertTrue(errors.size() > 0);
        assertEquals("Invalid character reference: numeric reference with no numerals", errors.get(0).toString());
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Error!";
        tokeniser.error(errorMsg);
        ParseErrorList errors = tokeniser.errors;
        assertTrue(errors.size() > 0);
        assertEquals("Error!", errors.get(0).toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertTrue(result);
    }

    // TODO: Add more comprehensive test cases for the remaining methods in the Tokeniser class

}