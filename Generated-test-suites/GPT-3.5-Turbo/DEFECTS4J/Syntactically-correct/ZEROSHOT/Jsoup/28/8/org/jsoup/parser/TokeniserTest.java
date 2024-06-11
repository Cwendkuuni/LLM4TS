
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null, null);
    }

    @Test
    public void testRead() {
        // TODO: Test read method
    }

    @Test
    public void testEmit() {
        // TODO: Test emit method
    }

    @Test
    public void testEmitString() {
        // TODO: Test emit(String str) method
    }

    @Test
    public void testEmitChar() {
        // TODO: Test emit(char c) method
    }

    @Test
    public void testGetState() {
        // TODO: Test getState method
    }

    @Test
    public void testTransition() {
        // TODO: Test transition method
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: Test advanceTransition method
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: Test acknowledgeSelfClosingFlag method
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: Test consumeCharacterReference method
    }

    @Test
    public void testCreateTagPending() {
        // TODO: Test createTagPending method
    }

    @Test
    public void testEmitTagPending() {
        // TODO: Test emitTagPending method
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: Test createCommentPending method
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: Test emitCommentPending method
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: Test createDoctypePending method
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: Test emitDoctypePending method
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: Test createTempBuffer method
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: Test isAppropriateEndTagToken method
    }

    @Test
    public void testAppropriateEndTagName() {
        // TODO: Test appropriateEndTagName method
    }

    @Test
    public void testError() {
        // TODO: Test error method
    }

    @Test
    public void testEofError() {
        // TODO: Test eofError method
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: Test characterReferenceError method
    }

    @Test
    public void testError1() {
        // TODO: Test error method
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: Test currentNodeInHtmlNS method
    }
}
