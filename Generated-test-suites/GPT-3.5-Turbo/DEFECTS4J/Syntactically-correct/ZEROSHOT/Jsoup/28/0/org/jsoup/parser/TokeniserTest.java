package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }
    
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(startTag, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertTrue(tokeniser.charBuffer.length() > 0);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'c';
        tokeniser.emit(c);
        assertTrue(tokeniser.charBuffer.length() > 0);
        assertEquals(c, tokeniser.charBuffer.charAt(0));
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
        assertTrue(tokeniser.reader.pos() > 0);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);

        assertNull(tokeniser.consumeCharacterReference(null, true));
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        tokeniser.createTagPending(start);

        assertNotNull(tokeniser.tagPending);
        assertEquals(start, tokeniser.tagPending.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
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
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.StartTag();
        
        assertFalse(tokeniser.isAppropriateEndTagToken());
        
        tokeniser.tagPending.tagName = tokeniser.lastStartTag.tagName;
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag";
        
        assertEquals("tag", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorState() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);

        assertTrue(tokeniser.errors.canAddError());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);

        assertTrue(tokeniser.errors.canAddError());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "invalid reference";
        tokeniser.characterReferenceError(message);

        assertTrue(tokeniser.errors.canAddError());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testError() {
        String errorMsg = "error";
        tokeniser.error(errorMsg);

        assertTrue(tokeniser.errors.canAddError());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

}