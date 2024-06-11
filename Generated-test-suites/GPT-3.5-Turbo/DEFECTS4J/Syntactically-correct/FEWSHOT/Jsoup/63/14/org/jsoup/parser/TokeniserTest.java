package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // TODO: add test cases for read() method
    }

    @Test
    public void testEmit() {
        // TODO: add test cases for emit(Token) method
    }

    @Test
    public void testEmitString() {
        // TODO: add test cases for emit(String) method
    }

    @Test
    public void testEmitCharArray() {
        // TODO: add test cases for emit(char[]) method
    }

    @Test
    public void testEmitIntArray() {
        // TODO: add test cases for emit(int[]) method
    }

    @Test
    public void testEmitChar() {
        // TODO: add test cases for emit(char) method
    }

    @Test
    public void testGetState() {
        // TODO: add test cases for getState() method
    }

    @Test
    public void testTransition() {
        // TODO: add test cases for transition(TokeniserState) method
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: add test cases for advanceTransition(TokeniserState) method
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: add test cases for acknowledgeSelfClosingFlag() method
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: add test cases for consumeCharacterReference(Character, boolean) method
    }

    @Test
    public void testCreateTagPending() {
        // TODO: add test cases for createTagPending(boolean) method
    }

    @Test
    public void testEmitTagPending() {
        // TODO: add test cases for emitTagPending() method
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: add test cases for createCommentPending() method
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: add test cases for emitCommentPending() method
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: add test cases for createDoctypePending() method
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: add test cases for emitDoctypePending() method
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: add test cases for createTempBuffer() method
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: add test cases for isAppropriateEndTagToken() method
    }

    @Test
    public void testAppropriateEndTagName() {
        // TODO: add test cases for appropriateEndTagName() method
    }

    @Test
    public void testErrorTokeniserState() {
        // TODO: add test cases for error(TokeniserState) method
    }

    @Test
    public void testEofErrorTokeniserState() {
        // TODO: add test cases for eofError(TokeniserState) method
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: add test cases for characterReferenceError(String) method
    }

    @Test
    public void testErrorString() {
        // TODO: add test cases for error(String) method
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: add test cases for currentNodeInHtmlNS() method
    }

    @Test
    public void testUnescapeEntities() {
        // TODO: add test cases for unescapeEntities(boolean) method
    }
}