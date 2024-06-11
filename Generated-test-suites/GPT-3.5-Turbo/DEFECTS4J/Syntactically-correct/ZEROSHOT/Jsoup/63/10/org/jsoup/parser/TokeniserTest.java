
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
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
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        Token token = new Token();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charsString);
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        assertEquals("test", tokeniser.charsString);
    }

    @Test
    public void testEmitInt() {
        int[] codepoints = {116, 101, 115, 116};
        tokeniser.emit(codepoints);
        assertEquals("test", tokeniser.charsString);
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        assertEquals("t", tokeniser.charsString);
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        assertNull(codeRef);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
        assertEquals(start, tag.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.Tag();
        tokeniser.emitTagPending();
        assertEquals(tokeniser.tagPending, tokeniser.emitPending);
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
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
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
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertTrue(tokeniser.dataBuffer.length() == 0);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "test";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "test";
        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorState() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertTrue(tokeniser.errors.hasErrors());
    }

    @Test
    public void testEofErrorState() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertTrue(tokeniser.errors.hasErrors());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "test";
        tokeniser.characterReferenceError(message);
        assertTrue(tokeniser.errors.hasErrors());
    }

    @Test
    public void testError() {
        String errorMsg = "test";
        tokeniser.error(errorMsg);
        assertTrue(tokeniser.errors.hasErrors());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        String str = "test";
        assertFalse(tokeniser.unescapeEntities(false).contains("&amp;"));
        tokeniser.emit("&amp;");
        assertTrue(tokeniser.unescapeEntities(false).contains("&amp;"));
    }
}
