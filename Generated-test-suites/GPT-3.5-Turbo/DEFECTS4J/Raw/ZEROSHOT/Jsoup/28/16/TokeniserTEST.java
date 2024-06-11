package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Entities;

import java.io.StringReader;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();

        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // TODO: Add test cases to ensure proper reading of tokens
    }

    @Test
    public void testEmit() {
        // TODO: Add test cases to ensure proper emitting of tokens
    }

    @Test
    public void testEmitString() {
        // TODO: Add test cases to ensure proper emitting of strings
    }

    @Test
    public void testEmitChar() {
        // TODO: Add test cases to ensure proper emitting of characters
    }

    @Test
    public void testGetState() {
        // TODO: Add test cases to ensure proper retrieval of tokeniser state
    }

    @Test
    public void testTransition() {
        // TODO: Add test cases to ensure proper transition between tokeniser states
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: Add test cases to ensure proper advancing of tokeniser state
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: Add test cases to ensure proper acknowledgement of self closing flag
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: Add test cases to ensure proper consumption of character references
    }

    @Test
    public void testCreateTagPending() {
        // TODO: Add test cases to ensure proper creation of tag pending
    }

    @Test
    public void testEmitTagPending() {
        // TODO: Add test cases to ensure proper emitting of tag pending
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: Add test cases to ensure proper creation of comment pending
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: Add test cases to ensure proper emitting of comment pending
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: Add test cases to ensure proper creation of doctype pending
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: Add test cases to ensure proper emitting of doctype pending
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: Add test cases to ensure proper creation of temporary buffer
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: Add test cases to ensure proper checking of appropriate end tag token
    }

    @Test
    public void testAppropriateEndTagName() {
        // TODO: Add test cases to ensure proper retrieval of appropriate end tag name
    }

    @Test
    public void testErrorState() {
        // TODO: Add test cases to ensure proper handling of error states
    }

    @Test
    public void testEofErrorState() {
        // TODO: Add test cases to ensure proper handling of EOF error states
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: Add test cases to ensure proper handling of character reference errors
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: Add test cases to ensure proper checking of current node in HTML namespace
    }
}