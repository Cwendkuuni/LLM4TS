package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull("Token should be null", token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Comment();
        tokeniser.emit(token);
        assertEquals("Emit pending token should be equal to emitted token", token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals("Char buffer should be equal to emitted string", str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'c';
        tokeniser.emit(c);
        assertEquals("Char buffer should be equal to emitted char", String.valueOf(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals("Current state should be equal to initial state", TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Rcdata);
        TokeniserState state = tokeniser.getState();
        assertEquals("New state should be equal to transition state", TokeniserState.Rcdata, state);
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.Rcdata);
        TokeniserState state = tokeniser.getState();
        assertEquals("New state should be equal to transition state", TokeniserState.Rcdata, state);
        tokeniser.advanceTransition(TokeniserState.Cdata);
        state = tokeniser.getState();
        assertEquals("New state should be equal to transition state", TokeniserState.Cdata, state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue("Self closing flag acknowledged should be true", tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull("Result should be null", result);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull("Tag pending should not be null", tag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag";
        tokeniser.emitTagPending();
        assertTrue("Tag pending should be finalized", tokeniser.tagPending.isFinalized);
        assertNotNull("Emit pending tag should not be null", tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull("Comment pending should not be null", tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertNotNull("Emit pending comment should not be null", tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull("Doctype pending should not be null", tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertNotNull("Emit pending doctype should not be null", tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull("Data buffer should not be null", tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag";
        tokeniser.lastStartTag = tokeniser.tagPending;
        assertTrue("Appropriate end tag token should be true", tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag";
        tokeniser.lastStartTag = tokeniser.tagPending;
        String tagName = tokeniser.appropriateEndTagName();
        assertEquals("Appropriate end tag name should be equal to tag name of last start tag emitted", "tag", tagName);
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertTrue("Parse errors should not be empty", errors.hasErrors());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertTrue("Parse errors should not be empty", errors.hasErrors());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        assertTrue("Parse errors should not be empty", errors.hasErrors());
    }

    @Test
    public void testErrorWithMessage() {
        tokeniser.error("Error message");
        assertTrue("Parse errors should not be empty", errors.hasErrors());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue("Current node is in HTML namespace", tokeniser.currentNodeInHtmlNS());
    }
}