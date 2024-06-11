package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null);
    }
    
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }
    
    @Test
    public void testEmit() {
        Token.Tag tag = new Token.Tag();
        tokeniser.emit(tag);
        assertEquals(tag, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }
    
    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testEmitChar() {
        char c = 'c';
        tokeniser.emit(c);
        assertEquals(Character.toString(c), tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testGetState() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
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
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(result);
    }
    
    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
        assertEquals(start, tag.start);
    }
    
    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.Tag();
        tokeniser.emitTagPending();
        assertEquals(tokeniser.tagPending, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
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
        assertTrue(tokeniser.isEmitPending);
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
        assertTrue(tokeniser.isEmitPending);
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.Tag();
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending.tagName = "test";
        tokeniser.lastStartTag.tagName = "test";
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        assertFalse(tokeniser.isTrackErrors());
    }
    
    @Test
    public void testErrorState() {
        String errorMsg = "Error";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.errors.size());
        assertEquals(errorMsg, tokeniser.errors.get(0).errorMsg);
    }
    
    @Test
    public void testErrorStateInState() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertEquals(1, tokeniser.errors.size());
        assertEquals(state, tokeniser.errors.get(0).state);
    }
    
    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertEquals(1, tokeniser.errors.size());
        assertEquals(state, tokeniser.errors.get(0).state);
    }
}