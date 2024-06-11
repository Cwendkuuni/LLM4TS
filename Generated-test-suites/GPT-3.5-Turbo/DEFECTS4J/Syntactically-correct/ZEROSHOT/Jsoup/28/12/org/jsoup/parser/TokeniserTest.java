package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        reader = new CharacterReader("test");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        assertNull(tokeniser.emitPending);

        // Test if emit is pending and charBuffer is empty
        tokeniser.isEmitPending = true;
        Token token = tokeniser.read();
        assertNull(token);
        assertFalse(tokeniser.isEmitPending);
        assertTrue(tokeniser.charBuffer.length() == 0);

        // Test if emit is pending and charBuffer is not empty
        tokeniser.charBuffer.append("test");
        token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("test", ((Token.Character) token).getData());
        assertFalse(tokeniser.isEmitPending);
        assertTrue(tokeniser.charBuffer.length() == 0);

        // Test if emit is not pending and charBuffer is empty
        tokeniser.isEmitPending = false;
        token = tokeniser.read();
        assertNull(token);
        assertFalse(tokeniser.isEmitPending);
        assertTrue(tokeniser.charBuffer.length() == 0);
    }

    @Test
    public void testEmitToken() {
        assertNull(tokeniser.emitPending);

        // Test StartTag
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);
        assertEquals(startTag, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);

        // Test EndTag
        Token.EndTag endTag = new Token.EndTag();
        tokeniser.emit(endTag);
        assertEquals(endTag, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);

        // Test Character
        tokeniser.charBuffer.append("test");
        tokeniser.emit("test");
        assertEquals("test", ((Token.Character) tokeniser.emitPending).getData());
        assertTrue(tokeniser.isEmitPending);

        // Test unrecognized token type
        Token token = new Token();
        tokeniser.emit(token);
        assertNull(tokeniser.emitPending);
        assertFalse(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitChar() {
        assertNull(tokeniser.emitPending);

        tokeniser.emit('t');
        assertEquals('t', tokeniser.charBuffer.charAt(0));

        tokeniser.emit('e');
        assertEquals('e', tokeniser.charBuffer.charAt(1));

        tokeniser.emit('s');
        assertEquals('s', tokeniser.charBuffer.charAt(2));

        tokeniser.emit('t');
        assertEquals('t', tokeniser.charBuffer.charAt(3));
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());

        tokeniser.transition(TokeniserState.Rcdata);
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());

        tokeniser.transition(TokeniserState.Cdata);
        assertEquals(TokeniserState.Cdata, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        assertEquals(TokeniserState.Data, tokeniser.getState());

        tokeniser.transition(TokeniserState.Rcdata);
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());

        tokeniser.transition(TokeniserState.Cdata);
        assertEquals(TokeniserState.Cdata, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        assertEquals(TokeniserState.Data, tokeniser.getState());

        tokeniser.advanceTransition(TokeniserState.Rcdata);
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());
        assertEquals(1, reader.pos());

        tokeniser.advanceTransition(TokeniserState.Cdata);
        assertEquals(TokeniserState.Cdata, tokeniser.getState());
        assertEquals(2, reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        assertTrue(tokeniser.selfClosingFlagAcknowledged);

        tokeniser.selfClosingFlagAcknowledged = false;
        assertFalse(tokeniser.selfClosingFlagAcknowledged);

        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test empty reader
        reader.unconsume();
        Character reference = tokeniser.consumeCharacterReference(null, false);
        assertNull(reference);

        // Test additional allowed character is null
        reader.unconsume();
        reader.addFirst("a");
        reference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('a', reference.charValue());
    }

    @Test
    public void testCreateTagPending() {
        assertNull(tokeniser.tagPending);

        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tag instanceof Token.StartTag);
        assertEquals(tag, tokeniser.tagPending);

        tag = tokeniser.createTagPending(false);
        assertTrue(tag instanceof Token.EndTag);
        assertEquals(tag, tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending() {
        assertNull(tokeniser.emitPending);

        Token.StartTag tag = new Token.StartTag();
        tag.tagName = "test";
        tokeniser.tagPending = tag;

        tokeniser.emitTagPending();
        assertEquals(tokeniser.tagPending, tokeniser.emitPending);
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        assertNull(tokeniser.commentPending);

        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        assertNull(tokeniser.emitPending);

        Token.Comment comment = new Token.Comment();
        tokeniser.commentPending = comment;

        tokeniser.emitCommentPending();
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        assertNull(tokeniser.doctypePending);

        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        assertNull(tokeniser.emitPending);

        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;

        tokeniser.emitDoctypePending();
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        assertNull(tokeniser.dataBuffer);

        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        assertFalse(tokeniser.isAppropriateEndTagToken());

        // Test lastStartTag is not null and tagName matches
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        tokeniser.lastStartTag = startTag;

        Token.Tag tag = tokeniser.createTagPending(false);
        tag.tagName = "test";

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEngTagName() {
        assertNull(tokeniser.lastStartTag);

        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        tokeniser.lastStartTag = startTag;

        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.error(TokeniserState.Rcdata);
        assertEquals(1, errors.size());

        tokeniser.error(TokeniserState.Cdata);
        assertEquals(2, errors.size());
    }

    @Test
    public void testEofError() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.eofError(TokeniserState.Rcdata);
        assertEquals(1, errors.size());

        tokeniser.eofError(TokeniserState.Cdata);
        assertEquals(2, errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.characterReferenceError("test");
        assertEquals(1, errors.size());
    }

    @Test
    public void testErrorString() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.error("Test error");
        assertEquals(1, errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}