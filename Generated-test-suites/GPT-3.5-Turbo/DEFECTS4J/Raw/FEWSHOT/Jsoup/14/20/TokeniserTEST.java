package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Test cases for read method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test string"));
        
        Token result = tokeniser.read();
        assertEquals("test string", result.getValue());
    }

    @Test
    public void testEmit() {
        // Test cases for emit method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        // Test cases for emit with String parameter method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.emit("test string");
        
        assertEquals("test string", tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        // Test cases for emit with char parameter method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.emit('c');
        
        assertEquals('c', tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testGetState() {
        // Test cases for getState method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        TokeniserState result = tokeniser.getState();
        
        assertEquals(TokeniserState.Data, result);
    }

    @Test
    public void testTransition() {
        // Test cases for transition method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.transition(TokeniserState.Comment);
        
        assertEquals(TokeniserState.Comment, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        // Test cases for advanceTransition method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.advanceTransition(TokeniserState.CdataSection);
        
        assertEquals(TokeniserState.CdataSection, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test cases for acknowledgeSelfClosingFlag method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.acknowledgeSelfClosingFlag();
        
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        // Test cases for consumeCharacterReference method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test string"));
        
        Character result = tokeniser.consumeCharacterReference(null, false);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateTagPending() {
        // Test cases for createTagPending method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        Token.Tag result = tokeniser.createTagPending(true);
        
        assertTrue(result instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        // Test cases for emitTagPending method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        Token.Tag tag = tokeniser.createTagPending(true);
        
        tokeniser.emit(tag);
        
        assertEquals(tag, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        // Test cases for createCommentPending method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.createCommentPending();
        
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        // Test cases for emitCommentPending method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.createCommentPending();
        
        tokeniser.emitCommentPending();
        
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        // Test cases for createDoctypePending method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.createDoctypePending();
        
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        // Test cases for emitDoctypePending method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.createDoctypePending();
        
        tokeniser.emitDoctypePending();
        
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        // Test cases for createTempBuffer method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.createTempBuffer();
        
        assertTrue(tokeniser.dataBuffer instanceof StringBuilder);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        // Test cases for isAppropriateEndTagToken method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }  
 
    @Test
    public void testIsTrackErrors() {
        // Test cases for isTrackErrors method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        boolean result = tokeniser.isTrackErrors();
        
        assertTrue(result);
    }  

    @Test
    public void testSetTrackErrors() {
        // Test cases for setTrackErrors method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.setTrackErrors(false);
        
        assertFalse(tokeniser.isTrackErrors());
    }  
    
    @Test
    public void testError() {
        // Test cases for error method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.error(TokeniserState.Comment);
        
        assertEquals(1, tokeniser.errors.size());
    } 

    @Test
    public void testEofError() {
        // Test cases for eofError method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.eofError(TokeniserState.Comment);
        
        assertEquals(1, tokeniser.errors.size());
    } 

    @Test
    public void testCharacterReferenceError() {
        // Test cases for characterReferenceError method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.characterReferenceError();
        
        assertEquals(1, tokeniser.errors.size());
    } 
    
    @Test
    public void testErrorString() {
        // Test cases for error method with String parameter
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        tokeniser.error("Error message");
        
        assertEquals(1, tokeniser.errors.size());
    } 
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test cases for currentNodeInHtmlNS method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(null));
        
        boolean result = tokeniser.currentNodeInHtmlNS();
        
        assertTrue(result);
    }   
}