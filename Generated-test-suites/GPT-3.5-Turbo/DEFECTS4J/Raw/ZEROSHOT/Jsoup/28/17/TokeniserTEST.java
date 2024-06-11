package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertEquals("Expected null token when reader is empty", null, token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertTrue("Expected emitPending to be true", tokeniser.isEmitPending);
        assertEquals("Expected emitPending to be set to token", token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals("Expected str to be added to charBuffer", str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        assertEquals("Expected c to be added to charBuffer", c, tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertNotNull("Expected non-null state", state);
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.TagName;
        tokeniser.transition(newState);
        assertEquals("Expected state to be set to newState", newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.TagName;
        tokeniser.advanceTransition(newState);
        assertEquals("Expected state to be set to newState", newState, tokeniser.getState());
        assertEquals("Expected reader to be advanced", 1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue("Expected selfClosingFlagAcknowledged to be true", tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        tokeniser.reader.append("&#65;");
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, false);
        assertNotNull("Expected non-null result", result);
        assertEquals("Expected 'A' as result", 'A', (char) result);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull("Expected non-null tag", tag);
        assertTrue("Expected tag to be of type StartTag", tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = new Token.StartTag();
        tokeniser.tagPending = tag;
        tokeniser.emitTagPending();
        assertTrue("Expected isEmitPending to be true", tokeniser.isEmitPending);
        assertEquals("Expected emitPending to be set to tag", tag, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull("Expected non-null commentPending", tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment comment = new Token.Comment();
        tokeniser.commentPending = comment;
        tokeniser.emitCommentPending();
        assertTrue("Expected isEmitPending to be true", tokeniser.isEmitPending);
        assertEquals("Expected emitPending to be set to comment", comment, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull("Expected non-null doctypePending", tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;
        tokeniser.emitDoctypePending();
        assertTrue("Expected isEmitPending to be true", tokeniser.isEmitPending);
        assertEquals("Expected emitPending to be set to doctype", doctype, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull("Expected non-null dataBuffer", tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = null;
        assertFalse("Expected false when lastStartTag is null", tokeniser.isAppropriateEndTagToken());
        
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.tagPending.tagName = "p";
        assertFalse("Expected false when tag names don't match", tokeniser.isAppropriateEndTagToken());
        
        tokeniser.tagPending.tagName = "div";
        assertTrue("Expected true when tag names match", tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = null;
        assertNull("Expected null when lastStartTag is null", tokeniser.appropriateEndTagName());
        
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";
        assertEquals("Expected tagName of lastStartTag", "div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorTokeniserState() {
        tokeniser.error(TokeniserState.Data);
        assertEquals("Expected 1 error to be added", 1, tokeniser.errors.size());
        assertEquals("Unexpected error message", "Unexpected character 'null' in input state [Data]", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofErrorTokeniserState() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals("Expected 1 error to be added", 1, tokeniser.errors.size());
        assertEquals("Unexpected error message", "Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference: invalid";
        tokeniser.characterReferenceError(message);
        assertEquals("Expected 1 error to be added", 1, tokeniser.errors.size());
        assertEquals("Unexpected error message", "Invalid character reference: invalid", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testErrorString() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        assertEquals("Expected 1 error to be added", 1, tokeniser.errors.size());
        assertEquals("Unexpected error message", "Error message", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue("Expected currentNodeInHtmlNS to always return true", tokeniser.currentNodeInHtmlNS());
    }
}