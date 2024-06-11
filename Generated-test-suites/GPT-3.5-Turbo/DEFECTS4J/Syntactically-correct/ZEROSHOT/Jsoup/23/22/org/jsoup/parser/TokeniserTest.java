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
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        assertNull(tokeniser.read());
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit("test");
        assertEquals(new Token.Character("test"), tokeniser.read());
    }

    @Test
    public void testEmitToken() {
        Token tagPending = new Token.StartTag();
        tokeniser.emit(tagPending);
        assertEquals(tagPending, tokeniser.read());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.AttrName);
        assertEquals(TokeniserState.AttrName, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader("test");
        tokeniser.advanceTransition(TokeniserState.Comment);
        assertEquals(TokeniserState.Comment, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testConsumeCharacterReference() {
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertNotNull(tagPending);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tagPending = new Token.StartTag();
        tokeniser.tagPending = tagPending;
        tokeniser.emitTagPending();
        assertEquals(tagPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment commentPending = new Token.Comment();
        tokeniser.commentPending = commentPending;
        tokeniser.emitCommentPending();
        assertEquals(commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctypePending = new Token.Doctype();
        tokeniser.doctypePending = doctypePending;
        tokeniser.emitDoctypePending();
        assertEquals(doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        assertEquals(tokeniser.lastStartTag.tagName, tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.AttrName);
        assertEquals(1, tokeniser.errors.size());
        assertEquals(new ParseError(0, "Unexpected character '' in input state [AttrName]"), tokeniser.errors.get(0));
    }

    @Test
    public void testEOFError() {
        tokeniser.eofError(TokeniserState.AttrName);
        assertEquals(1, tokeniser.errors.size());
        assertEquals(new ParseError(0, "Unexpectedly reached end of file (EOF) in input state [AttrName]"), tokeniser.errors.get(0));
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid reference");
        assertEquals(1, tokeniser.errors.size());
        assertEquals(new ParseError(0, "Invalid character reference: Invalid reference"), tokeniser.errors.get(0));
    }

    @Test
    public void testErrorString() {
        tokeniser.error("Invalid error");
        assertEquals(1, tokeniser.errors.size());
        assertEquals(new ParseError(0, "Invalid error"), tokeniser.errors.get(0));
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
    
    @Test
    public void testReadWithPendingEmit() {
        tokeniser.isEmitPending = true;
        assertNotNull(tokeniser.read());
    }
    
    @Test
    public void testEmitWithPendingCharBuffer() {
        tokeniser.charBuffer.append("test");
        assertEquals(new Token.Character("test"), tokeniser.emitPending);
    }
    
    @Test
    public void testReadWithEmptyCharBuffer() {
        String str = "test";
        tokeniser.charBuffer.append(str);
        tokeniser.charBuffer.delete(0, tokeniser.charBuffer.length());
        
        Token.Character character = new Token.Character(str);
        tokeniser.emit(character);
        
        assertEquals(character, tokeniser.read());
    }

}