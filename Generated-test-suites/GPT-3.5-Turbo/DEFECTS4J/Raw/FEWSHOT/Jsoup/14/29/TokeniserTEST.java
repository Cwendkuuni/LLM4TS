package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        reader.advance();
        tokeniser.read();
        assertEquals("i", tokeniser.charBuffer.toString());
        
        reader.advance();
        tokeniser.read();
        assertEquals("in", tokeniser.charBuffer.toString());
    
        reader.advance();
        tokeniser.read();
        assertEquals("inp", tokeniser.charBuffer.toString());
    
        reader.advance();
        tokeniser.read();
        assertEquals("inpu", tokeniser.charBuffer.toString());
    
        reader.advance();
        tokeniser.read();
        assertEquals("input", tokeniser.charBuffer.toString());
    
        reader.advance();
        tokeniser.read();
        assertEquals("input", tokeniser.emitPending.toString());
    }
  
    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.emit("test");
        assertEquals("test", tokeniser.charBuffer.toString());
        
        tokeniser.emit('c');
        assertEquals("testc", tokeniser.charBuffer.toString());
    }
  
    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        
        Token.Tag expected = new Token.StartTag();
        assertEquals(expected.getClass(), tokeniser.emitPending.getClass());
    }
  
    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        
        Token.Comment expected = new Token.Comment();
        assertEquals(expected.getClass(), tokeniser.emitPending.getClass());
    }
  
    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        
        Token.Doctype expected = new Token.Doctype();
        assertEquals(expected.getClass(), tokeniser.emitPending.getClass());
    }
  
    @Test
    public void testCreateTempBuffer() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.createTempBuffer();
        
        assertEquals("", tokeniser.dataBuffer.toString());
    }
  
    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending.tagName = "input";
      
        assertTrue(tokeniser.isAppropriateEndTagToken());
        
        tokeniser.lastStartTag.tagName = "div";
      
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
  
    @Test
    public void testIsTrackErrors() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        assertTrue(tokeniser.isTrackErrors());
    }
  
    @Test
    public void testSetTrackErrors() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.setTrackErrors(false);
        
        assertFalse(tokeniser.isTrackErrors());
    }
  
    @Test
    public void testError() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.error(TokeniserState.Data);
        
        ParseError error = new ParseError("Unexpected character in input", 'i', TokeniserState.Data, 1);
        assertEquals(error.getMessage(), tokeniser.errors.get(0).getMessage());
        assertEquals(error.getUnexpectedChar(), tokeniser.errors.get(0).getUnexpectedChar());
        assertEquals(error.getState(), tokeniser.errors.get(0).getState());
        assertEquals(error.getPosition(), tokeniser.errors.get(0).getPosition());
    }
  
    @Test
    public void testEofError() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.eofError(TokeniserState.Data);
        
        ParseError error = new ParseError("Unexpectedly reached end of file (EOF)", TokeniserState.Data, 0);
        assertEquals(error.getMessage(), tokeniser.errors.get(0).getMessage());
        assertEquals(error.getState(), tokeniser.errors.get(0).getState());
        assertEquals(error.getPosition(), tokeniser.errors.get(0).getPosition());
    }
  
    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.characterReferenceError();
        
        ParseError error = new ParseError("Invalid character reference", 0);
        assertEquals(error.getMessage(), tokeniser.errors.get(0).getMessage());
        assertEquals(error.getPosition(), tokeniser.errors.get(0).getPosition());
    }
  
    @Test
    public void testErrorWithString() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.error("Test error");
        
        ParseError error = new ParseError("Test error", 0);
        assertEquals(error.getMessage(), tokeniser.errors.get(0).getMessage());
        assertEquals(error.getPosition(), tokeniser.errors.get(0).getPosition());
    }
  
    @Test
    public void testCurrentNodeInHtmlNS() {
        CharacterReader reader = new CharacterReader("input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}