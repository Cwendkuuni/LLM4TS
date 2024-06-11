package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "Hello, world!";
        tokeniser.emit(str);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(str, tokeniser.charsString);
    }

    @Test
    public void testEmitChar() {
        char c = 'A';
        tokeniser.emit(c);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(Character.toString(c), tokeniser.charsString);
    }

    @Test
    public void testEmitCharArr() {
        char[] chars = {'H', 'e', 'l', 'l', 'o'};
        tokeniser.emit(chars);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(new String(chars), tokeniser.charsString);
    }

    @Test
    public void testEmitIntArr() {
        int[] codepoints = {72, 101, 108, 108, 111};
        tokeniser.emit(codepoints);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(new String(codepoints, 0, codepoints.length), tokeniser.charsString);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertTrue(tokeniser.reader.isEmpty());
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertNull(codepoints);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertEquals(start, tag.start());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.Tag();
        tokeniser.tagPending.tagName = "div";
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending);
        Token token = tokeniser.emitPending;
        assertEquals("div", ((Token.StartTag)token).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.isEmitPending);
        Token token = tokeniser.emitPending;
        assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.isEmitPending);
        Token token = tokeniser.emitPending;
        assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        assertFalse(tokeniser.isAppropriateEndTagToken());
        tokeniser.lastStartTag = "div";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = null;
        assertNull(tokeniser.appropriateEndTagName());
        tokeniser.lastStartTag = "div";
        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorTokeniserState() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpected character '' in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofErrorTokeniserState() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: Invalid character reference", errors.get(0).getErrorMessage());
    }

    @Test
    public void testErrorString() {
        tokeniser.error("Error in Tokeniser");
        assertEquals(1, errors.size());
        assertEquals("Error in Tokeniser", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        boolean inAttribute = false;
        String unescaped = tokeniser.unescapeEntities(inAttribute);
        assertNotNull(unescaped);
        assertEquals("", unescaped);
    }
}