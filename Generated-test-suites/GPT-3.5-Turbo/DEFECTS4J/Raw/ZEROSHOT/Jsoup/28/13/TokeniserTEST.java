package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testEmit() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testTransition() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testCreateTagPending() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testEmitTagPending() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testAppropriateEndTagName() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testError() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testEofError() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testErrorString() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: Add test cases to cover different scenarios
    }

    @Test
    public void testUnescapeEntities() {
        // TODO: Add test cases to cover different scenarios
    }
}