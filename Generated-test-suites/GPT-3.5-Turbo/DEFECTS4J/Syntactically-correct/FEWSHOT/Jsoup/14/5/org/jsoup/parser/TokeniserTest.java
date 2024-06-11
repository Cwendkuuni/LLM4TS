package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(null);
    }

    @Test
    public void testRead() {
        tokeniser.read();
        // Add assertions here
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        // Add assertions here
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        // Add assertions here
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        // Add assertions here
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        // Add assertions here
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        // Add assertions here
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        // Add assertions here
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        // Add assertions here
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        // Add assertions here
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tagPending = tokeniser.createTagPending(start);
        // Add assertions here
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.emitTagPending();
        // Add assertions here
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        // Add assertions here
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.emitCommentPending();
        // Add assertions here
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        // Add assertions here
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.emitDoctypePending();
        // Add assertions here
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        // Add assertions here
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        boolean result = tokeniser.isAppropriateEndTagToken();
        // Add assertions here
    }

    @Test
    public void testIsTrackErrors() {
        boolean result = tokeniser.isTrackErrors();
        // Add assertions here
    }

    @Test
    public void testSetTrackErrors() {
        boolean trackErrors = true;
        tokeniser.setTrackErrors(trackErrors);
        // Add assertions here
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        // Add assertions here
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        // Add assertions here
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        // Add assertions here
    }

    @Test
    public void testErrorString() {
        String errorMsg = "Error";
        tokeniser.error(errorMsg);
        // Add assertions here
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        // Add assertions here
    }
}