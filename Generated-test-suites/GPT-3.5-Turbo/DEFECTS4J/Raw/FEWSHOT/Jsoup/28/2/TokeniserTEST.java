package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setup() {
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(new CharacterReader(""), errors);
    }

    @Test
    public void testRead() {
        // TODO: Implement test cases for read() method
    }

    @Test
    public void testEmit() {
        // TODO: Implement test cases for emit(Token) method
    }

    @Test
    public void testEmitString() {
        // TODO: Implement test cases for emit(String) method
    }

    @Test
    public void testEmitChar() {
        // TODO: Implement test cases for emit(char) method
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Rcdata);
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.Rcdata);
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());
        // TODO: Add more test cases for advanceTransition(TokeniserState) method
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: Implement test cases for consumeCharacterReference(Character, boolean) method
    }

    @Test
    public void testCreateTagPending() {
        // TODO: Implement test cases for createTagPending(boolean) method
    }

    @Test
    public void testEmitTagPending() {
        // TODO: Implement test cases for emitTagPending() method
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: Implement test cases for createCommentPending() method
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: Implement test cases for emitCommentPending() method
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: Implement test cases for createDoctypePending() method
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: Implement test cases for emitDoctypePending() method
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: Implement test cases for createTempBuffer() method
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: Implement test cases for isAppropriateEndTagToken() method
    }

    @Test
    public void testAppropriateEndTagName() {
        // TODO: Implement test cases for appropriateEndTagName() method
    }

    @Test
    public void testErrorTokeniserState() {
        // TODO: Implement test cases for error(TokeniserState) method
    }

    @Test
    public void testEofErrorTokeniserState() {
        // TODO: Implement test cases for eofError(TokeniserState) method
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: Implement test cases for characterReferenceError(String) method
    }
}