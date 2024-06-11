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
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        Token token = new Token();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
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
        TokeniserState state = TokeniserState.TagName;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        assertNotNull(tokeniser.createTagPending(start));
        assertEquals(start, tokeniser.tagPending.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.Tag();
        tokeniser.emitTagPending();
        assertTrue(tokeniser.tagPending.isFinalised());
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
        assertFalse(tokeniser.commentPending.isBlank());
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
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        assertFalse(tokeniser.isAppropriateEndTagToken());
        tokeniser.lastStartTag = "test";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        assertNull(tokeniser.appropriateEndTagName());
        tokeniser.lastStartTag = "test";
        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorTokeniserState() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testError() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.errors.size());
        assertEquals(errorMsg, tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        String str = "test &lt; entity &gt;";
        String unescapedStr = tokeniser.unescapeEntities(false);
        assertEquals("test < entity >", unescapedStr);
    }
}