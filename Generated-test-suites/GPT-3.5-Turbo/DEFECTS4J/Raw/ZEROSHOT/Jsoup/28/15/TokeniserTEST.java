package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        reader = new CharacterReader();
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // TODO: write test case for the read() method
    }

    @Test
    public void testEmit() {
        // TODO: write test case for the emit(Token) method
    }

    @Test
    public void testEmitString() {
        // TODO: write test case for the emit(String) method
    }

    @Test
    public void testEmitChar() {
        // TODO: write test case for the emit(char) method
    }

    @Test
    public void testGetState() {
        // TODO: write test case for the getState() method
    }

    @Test
    public void testTransition() {
        // TODO: write test case for the transition(TokeniserState) method
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: write test case for the advanceTransition(TokeniserState) method
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: write test case for the acknowledgeSelfClosingFlag() method
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: write test case for the consumeCharacterReference(Character, boolean) method
    }

    @Test
    public void testCreateTagPending() {
        // TODO: write test case for the createTagPending(boolean) method
    }

    @Test
    public void testEmitTagPending() {
        // TODO: write test case for the emitTagPending() method
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: write test case for the createCommentPending() method
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: write test case for the emitCommentPending() method
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: write test case for the createDoctypePending() method
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: write test case for the emitDoctypePending() method
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: write test case for the createTempBuffer() method
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: write test case for the isAppropriateEndTagToken() method
    }

    @Test
    public void testAppropriateEndTagName() {
        // TODO: write test case for the appropriateEndTagName() method
    }

    @Test
    public void testErrorState() {
        // TODO: write test case for the error(TokeniserState) method
    }

    @Test
    public void testEofErrorState() {
        // TODO: write test case for the eofError(TokeniserState) method
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: write test case for the characterReferenceError(String) method
    }

    @Test
    public void testError() {
        // TODO: write test case for the error(String) method
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: write test case for the currentNodeInHtmlNS() method
    }
}