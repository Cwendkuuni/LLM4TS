package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.emit(null);
        assertNull(tokeniser.read());
    }
    
    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.Tag tag = tokeniser.createTagPending(true);
        assertEquals(Token.TokenType.StartTag, tag.type);
    }
    
    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertNull(tokeniser.read());
    }
    
    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createCommentPending();
        assertNull(tokeniser.read());
    }
    
    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertNull(tokeniser.read());
    }
    
    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();
        assertNull(tokeniser.read());
    }
    
    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertNull(tokeniser.read());
    }
    
    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTempBuffer();
        assertNull(tokeniser.read());
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertNull(tokeniser.appropriateEndTagName());
    }
    
    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null, new ParseErrorList());
        tokeniser.error(null);
        assertEquals(1, tokeniser.getErrors().size());
    }
    
    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(null, new ParseErrorList());
        tokeniser.eofError(null);
        assertEquals(1, tokeniser.getErrors().size());
    }
    
    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(null, new ParseErrorList());
        tokeniser.characterReferenceError(null);
        assertEquals(1, tokeniser.getErrors().size());
    }
    
    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null, new ParseErrorList());
        tokeniser.error(null);
        assertEquals(1, tokeniser.getErrors().size());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}