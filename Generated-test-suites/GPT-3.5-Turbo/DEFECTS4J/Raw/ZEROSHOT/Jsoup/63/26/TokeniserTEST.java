package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "Test String";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.read().toString());
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = "Test".toCharArray();
        tokeniser.emit(chars);
        assertEquals("Test", tokeniser.read().toString());
    }

    @Test
    public void testEmitInt() {
        int[] codepoints = {'T', 'e', 's', 't'};
        tokeniser.emit(codepoints);
        assertEquals("Test", tokeniser.read().toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'T';
        tokeniser.emit(c);
        assertEquals("T", tokeniser.read().toString());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Rcdata);
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.Rawtext);
        assertEquals(TokeniserState.Rawtext, tokeniser.getState());
        assertTrue(tokeniser.reader.isEmpty());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        tokeniser.reader.add("&amp;");
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        int[] expected = {'&'};
        assertArrayEquals(expected, codeRef);
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.tagPending.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertTrue(tokeniser.read().isStartTag());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.read().isComment());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.read().isDoctype());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "start";
        tokeniser.createTagPending(false);
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "start";
        tokeniser.createTagPending(false);
        assertEquals("start", tokeniser.appropriateEndTagName());
    }
}