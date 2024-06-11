package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    
    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader("test input");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    // Test read method
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }
    
    // Test emit method
    @Test
    public void testEmit() {
        Token.Character characterToken = new Token.Character("test");
        tokeniser.emit(characterToken);
        assertEquals(characterToken, tokeniser.read());
    }

    // Test emit method with string parameter
    @Test
    public void testEmitWithString() {
        String str = "test";
        tokeniser.emit(str);
        Token.Character characterToken = (Token.Character) tokeniser.read();
        assertEquals(str, characterToken.getData());
    }

    // Test emit method with char parameter
    @Test
    public void testEmitWithChar() {
        char c = 't';
        tokeniser.emit(c);
        Token.Character characterToken = (Token.Character) tokeniser.read();
        assertEquals(Character.toString(c), characterToken.getData());
    }

    // Test getState method
    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }

    // Test transition method
    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.Data;
        tokeniser.transition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    // Test advanceTransition method
    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.Data;
        tokeniser.advanceTransition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    // Test acknowledgeSelfClosingFlag method
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        assertFalse(tokeniser.isEmitPending());
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    // Test consumeCharacterReference method with additional allowed character
    @Test
    public void testConsumeCharacterReferenceWithAdditionalAllowedCharacter() {
        TokeniserState state = tokeniser.getState();
        tokeniser.consumeCharacterReference('c', false);
        assertEquals(state, tokeniser.getState());
    }

    // Test consumeCharacterReference method in attribute
    @Test
    public void testConsumeCharacterReferenceInAttribute() {
        assertNull(tokeniser.consumeCharacterReference(null, true));
    }

    // Test createTagPending method with start value
    @Test
    public void testCreateTagPendingWithStartValue() {
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.isTagPending());
        assertTrue(tokeniser.getTagPending() instanceof Token.StartTag);
    }

    // Test createTagPending method with end value
    @Test
    public void testCreateTagPendingWithEndValue() {
        tokeniser.createTagPending(false);
        assertTrue(tokeniser.isTagPending());
        assertTrue(tokeniser.getTagPending() instanceof Token.EndTag);
    }

    // Test emitTagPending method
    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.StartTag);
    }

    // Test createCommentPending method
    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.getCommentPending());
    }

    // Test emitCommentPending method
    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.Comment);
    }

    // Test createDoctypePending method
    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.getDoctypePending());
    }

    // Test emitDoctypePending method
    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.Doctype);
    }
    
    // Test isAppropriateEndTagToken method
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().setTagName("test");
        tokeniser.emitTagPending();
        
        tokeniser.createTagPending(false);
        tokeniser.getTagPending().setTagName("test");
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
        
        tokeniser.getTagPending().setTagName("different");
        
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
    
    // Test appropriateEndTagName method
    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().setTagName("test");
        tokeniser.emitTagPending();
        
        assertEquals("test", tokeniser.appropriateEndTagName());
    }
    
    // Test error method
    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.getErrors().size());
    }
    
    // Test eofError method
    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.getErrors().size());
    }

    // Test characterReferenceError method
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("error");
        assertEquals(1, tokeniser.getErrors().size());
    }

    // Test error method with error message
    @Test
    public void testErrorWithMessage() {
        tokeniser.error("error");
        assertEquals(1, tokeniser.getErrors().size());
    }

    // Test currentNodeInHtmlNS
    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
    
}