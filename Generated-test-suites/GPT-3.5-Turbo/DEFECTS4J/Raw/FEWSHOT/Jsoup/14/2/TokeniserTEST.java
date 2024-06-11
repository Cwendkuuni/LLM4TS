package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token.Character characterToken = new Token.Character("test");
        tokeniser.emit(characterToken);

        Token token = tokeniser.read();
        assertEquals(characterToken, token);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);

        Token.Character characterToken = (Token.Character) tokeniser.read();
        assertEquals(str, characterToken.getData());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);

        Token.Character characterToken = (Token.Character) tokeniser.read();
        assertEquals(String.valueOf(c), characterToken.getData());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.AttributeName);

        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.AttributeName, state);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState nextState = TokeniserState.DoctypeName;
        tokeniser.advanceTransition(nextState);

        TokeniserState state = tokeniser.getState();
        assertEquals(nextState, state);
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
        Token.Tag tagPending = tokeniser.createTagPending(start);
        
        assertEquals(start, tagPending.getStart());
    }
    
    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
    }
    
    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }
    
    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
    }
    
    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }
    
    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        
        assertEquals("", tokeniser.dataBuffer.toString());
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testIsAppropriateEndTagToken_notMatch() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag.tagName = "span";
        
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        tokeniser.setTrackErrors(false);
        
        assertFalse(tokeniser.isTrackErrors());
    }
    
    @Test
    public void testError() {
        TokeniserState state = TokeniserState.StartTagOpen;
        tokeniser.error(state);
        
        assertFalse(tokeniser.errors.isEmpty());
        assertEquals(state, tokeniser.errors.get(0).getState());
    }
    
    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        
        assertFalse(tokeniser.errors.isEmpty());
        assertEquals(state, tokeniser.errors.get(0).getState());
    }
    
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        
        assertFalse(tokeniser.errors.isEmpty());
        assertNotEquals(0, tokeniser.errors.get(0).getPosition());
    }
    
    @Test
    public void testError_withErrorMessage() {
        String errorMsg = "Invalid character";
        tokeniser.error(errorMsg);
        
        assertFalse(tokeniser.errors.isEmpty());
        assertEquals(errorMsg, tokeniser.errors.get(0).getErrorMessage());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        
        assertTrue(result);
    }
    
    @Test
    public void testCurrentNodeInHtmlNS_namespaceMismatch() {
        // todo: implement namespaces correctly
    }
}