
package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token token = tokeniser.read();
        
        assertEquals("test", token.toString());
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testRead_shouldReturnCharacterToken() {
        CharacterReader reader = new CharacterReader("123");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token token = tokeniser.read();
        
        assertEquals("123", token.toString());
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = new Token.Character("test");
        
        tokeniser.emit(token);
        
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }
    
    @Test
    public void testEmit_shouldSetSelfClosingFlagAcknowledgedToTrue() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.selfClosingFlagAcknowledged = false;
        
        tokeniser.emit(new Token.StartTag());
        
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.Tag tag = new Token.StartTag();
        tokeniser.tagPending = tag;
        
        tokeniser.emitTagPending();
        
        assertEquals(tag, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
        assertNull(tokeniser.tagPending);
    }
    
    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        tokeniser.createCommentPending();
        
        assertNotNull(tokeniser.commentPending);
    }
    
    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.Comment comment = new Token.Comment();
        tokeniser.commentPending = comment;
        
        tokeniser.emitCommentPending();
        
        assertEquals(comment, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
        assertNull(tokeniser.commentPending);
    }
    
    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        tokeniser.createDoctypePending();
        
        assertNotNull(tokeniser.doctypePending);
    }
    
    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;
        
        tokeniser.emitDoctypePending();
        
        assertEquals(doctype, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
        assertNull(tokeniser.doctypePending);
    }
    
    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        tokeniser.createTempBuffer();
        
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "test";
        
        boolean result = tokeniser.isAppropriateEndTagToken();
        
        assertTrue(result);
    }
    
    @Test
    public void testIsAppropriateEndTagToken_shouldReturnFalseIfLastStartTagIsNull() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.tagPending = new Token.EndTag();
        
        boolean result = tokeniser.isAppropriateEndTagToken();
        
        assertFalse(result);
    }
    
    @Test
    public void testAppropriateEndTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.lastStartTag = startTag;
        
        String result = tokeniser.appropriateEndTagName();
        
        assertEquals("test", result);
    }
    
    @Test
    public void testError_shouldAddErrorToErrorList() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(null, errors);
        
        tokeniser.error(TokeniserState.Data);
        
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testEofError_shouldAddErrorToErrorList() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(null, errors);
        
        tokeniser.eofError(TokeniserState.Data);
        
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testCharacterReferenceError_shouldAddErrorToErrorList() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(null, errors);
        
        tokeniser.characterReferenceError("Invalid character reference");
        
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testError_shouldNotAddErrorIfErrorListCannotAddError() {
        ParseErrorList errors = new ParseErrorList(0);
        Tokeniser tokeniser = new Tokeniser(null, errors);
        
        tokeniser.error("Invalid character");
        
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        boolean result = tokeniser.currentNodeInHtmlNS();
        
        assertTrue(result);
    }
}
