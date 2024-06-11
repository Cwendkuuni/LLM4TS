package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""));
    }

    @After
    public void tearDown() {
        tokeniser = null;
    }

    @Test
    public void testRead() {
        // TODO: Write test cases for read() method
    }

    @Test
    public void testEmit() {
        // TODO: Write test cases for emit(Token) method
    }

    @Test
    public void testEmitString() {
        // TODO: Write test cases for emit(String) method
    }

    @Test
    public void testEmitChar() {
        // TODO: Write test cases for emit(char) method
    }

    @Test
    public void testGetState() {
        // TODO: Write test cases for getState() method
    }

    @Test
    public void testTransition() {
        // TODO: Write test cases for transition(TokeniserState) method
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: Write test cases for advanceTransition(TokeniserState) method
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: Write test cases for acknowledgeSelfClosingFlag() method
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: Write test cases for consumeCharacterReference(Character, boolean) method
    }

    @Test
    public void testCreateTagPending() {
        // TODO: Write test cases for createTagPending(boolean) method
    }

    @Test
    public void testEmitTagPending() {
        // TODO: Write test cases for emitTagPending() method
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: Write test cases for createCommentPending() method
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: Write test cases for emitCommentPending() method
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: Write test cases for createDoctypePending() method
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: Write test cases for emitDoctypePending() method
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: Write test cases for createTempBuffer() method
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: Write test cases for isAppropriateEndTagToken() method
    }

    @Test
    public void testIsTrackErrors() {
        // TODO: Write test cases for isTrackErrors() method
    }

    @Test
    public void testSetTrackErrors() {
        // TODO: Write test cases for setTrackErrors(boolean) method
    }

    @Test
    public void testError() {
        // TODO: Write test cases for error(TokeniserState) method
    }

    @Test
    public void testEofError() {
        // TODO: Write test cases for eofError(TokeniserState) method
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: Write test cases for characterReferenceError() method
    }

    @Test
    public void testErrorString() {
        // TODO: Write test cases for error(String) method
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: Write test cases for currentNodeInHtmlNS() method
    }
}