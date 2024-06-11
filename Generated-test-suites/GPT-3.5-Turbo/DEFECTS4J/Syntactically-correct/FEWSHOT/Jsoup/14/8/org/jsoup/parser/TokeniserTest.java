package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        assertNull(tokeniser.read());
    }
    
    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        Token tag = new Token.Tag("div");
        tokeniser.emit(tag);
        
        assertEquals(tag, tokeniser.read());
    }
    
    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.emit("test");
        
        assertEquals("test", ((Token.Character) tokeniser.read()).getData());
    }
    
    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.emit('c');
        
        assertEquals("c", ((Token.Character) tokeniser.read()).getData());
    }
    
    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        Token.Tag tagPending = tokeniser.createTagPending(true);
        
        assertEquals("StartTag", tagPending.getClass().getSimpleName());
        assertEquals(true, tagPending instanceof Token.StartTag);
    }
    
    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        
        assertEquals(tag, tokeniser.read());
    }
    
    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.createCommentPending();
        
        assertEquals("Comment", tokeniser.commentPending.getClass().getSimpleName());
    }
    
    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        
        assertEquals(tokeniser.commentPending, tokeniser.read());
    }
    
    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.createDoctypePending();
        
        assertEquals("Doctype", tokeniser.doctypePending.getClass().getSimpleName());
    }
    
    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        
        assertEquals(tokeniser.doctypePending, tokeniser.read());
    }
    
    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.createTempBuffer();
        
        assertEquals("", tokeniser.dataBuffer.toString());
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.tagPending = new Token.StartTag("div");
        tokeniser.lastStartTag = new Token.StartTag("div");
        
        assertEquals(true, tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        assertEquals(true, tokeniser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.setTrackErrors(false);
        
        assertEquals(false, tokeniser.isTrackErrors());
    }
    
    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.error(TokeniserState.Data);
        
        assertEquals(1, tokeniser.errors.size());
    }
    
    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.eofError(TokeniserState.Data);
        
        assertEquals(1, tokeniser.errors.size());
    }
    
    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.characterReferenceError();
        
        assertEquals(1, tokeniser.errors.size());
    }
    
    @Test
    public void testErrorString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.error("Error");
        
        assertEquals(1, tokeniser.errors.size());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        assertEquals(true, tokeniser.currentNodeInHtmlNS());
    }
}