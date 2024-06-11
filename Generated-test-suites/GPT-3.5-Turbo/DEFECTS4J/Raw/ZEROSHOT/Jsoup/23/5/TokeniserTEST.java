package org.jsoup.parser.junit;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // TODO: Add test cases for read() method
    }

    @Test
    public void testEmit() {
        // TODO: Add test cases for emit() method
    }

    @Test
    public void testEmitString() {
        // TODO: Add test cases for emit(String) method
    }

    @Test
    public void testEmitChar() {
        // TODO: Add test cases for emit(char) method
    }

    @Test
    public void testGetState() {
        // TODO: Add test cases for getState() method
    }

    @Test
    public void testTransition() {
        // TODO: Add test cases for transition() method
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: Add test cases for advanceTransition() method
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: Add test cases for acknowledgeSelfClosingFlag() method
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: Add test cases for consumeCharacterReference() method
    }

    @Test
    public void testCreateTagPending() {
        // TODO: Add test cases for createTagPending() method
    }

    @Test
    public void testEmitTagPending() {
        // TODO: Add test cases for emitTagPending() method
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: Add test cases for createCommentPending() method
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: Add test cases for emitCommentPending() method
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: Add test cases for createDoctypePending() method
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: Add test cases for emitDoctypePending() method
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: Add test cases for createTempBuffer() method
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: Add test cases for isAppropriateEndTagToken() method
    }

    @Test
    public void testAppropriateEndTagName() {
        // TODO: Add test cases for appropriateEndTagName() method
    }

    @Test
    public void testError() {
        // TODO: Add test cases for error(TokeniserState state) method
    }

    @Test
    public void testEofError() {
        // TODO: Add test cases for eofError(TokeniserState state) method
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: Add test cases for characterReferenceError(String message) method
    }

    @Test
    public void testErrorString() {
        // TODO: Add test cases for error(String errorMsg) method
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: Add test cases for currentNodeInHtmlNS() method
    }

}