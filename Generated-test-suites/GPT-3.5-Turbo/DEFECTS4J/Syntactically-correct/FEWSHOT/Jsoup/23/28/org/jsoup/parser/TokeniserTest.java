package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;
    
    @Before
    public void setUp() {
        errors = new ParseErrorList();
        reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader, errors);
    }
    
    @Test
    public void testRead() {
        // Test for empty input
        assertNull(tokeniser.read());
        
        // Test for non-character tokens
        reader = new CharacterReader("<!-- Comment -->");
        tokeniser = new Tokeniser(reader, errors);
        assertTrue(tokeniser.read() instanceof Token.Comment);
        
        reader = new CharacterReader("<!DOCTYPE html>");
        tokeniser = new Tokeniser(reader, errors);
        assertTrue(tokeniser.read() instanceof Token.Doctype);
        
        reader = new CharacterReader("<tag>");
        tokeniser = new Tokeniser(reader, errors);
        assertTrue(tokeniser.read() instanceof Token.Tag);
        
        // Test for character tokens
        reader = new CharacterReader("Hello World");
        tokeniser = new Tokeniser(reader, errors);
        assertTrue(tokeniser.read() instanceof Token.Character);
    }
    
    @Test
    public void testEmit() {
        // Test for emitting token
        Token token = new Token.Character("Hello");
        tokeniser.emit(token);
        assertTrue(tokeniser.read() == token);
        
        // Test for emitting string
        String str = "World";
        tokeniser.emit(str);
        assertTrue(tokeniser.read() instanceof Token.Character);
    }
    
    @Test
    public void testEmitChar() {
        // Test for emitting char
        char c = '!';
        tokeniser.emit(c);
        assertTrue(tokeniser.read() instanceof Token.Character);
    }
    
    @Test
    public void testGetState() {
        // Test for getting state
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }
    
    @Test
    public void testTransition() {
        // Test for transitioning state
        TokeniserState state = TokeniserState.AfterAttributeName;
        tokeniser.transition(state);
        assertTrue(tokeniser.getState() == state);
    }
    
    @Test
    public void testAdvanceTransition() {
        // Test for advancing and transitioning state
        TokeniserState state = TokeniserState.BeforeAttributeName;
        tokeniser.advanceTransition(state);
        assertTrue(tokeniser.getState() == state);
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test for acknowledging self closing flag
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        // Test for consuming character reference
        assertNull(tokeniser.consumeCharacterReference(null, false));
        
        // Test for consuming character reference with additional allowed character
        reader = new CharacterReader("&#121;");
        tokeniser = new Tokeniser(reader, errors);
        assertNull(tokeniser.consumeCharacterReference(null, false));
        
        // Test for consuming character reference in attribute
        reader = new CharacterReader("&amp;");
        tokeniser = new Tokeniser(reader, errors);
        assertNull(tokeniser.consumeCharacterReference(null, true));
        
        // Test for consuming character reference with numeric reference
        reader = new CharacterReader("&#100;");
        tokeniser = new Tokeniser(reader, errors);
        assertTrue(tokeniser.consumeCharacterReference(null, false) instanceof Character);
        
        // Test for consuming character reference with named reference
        reader = new CharacterReader("&lt;");
        tokeniser = new Tokeniser(reader, errors);
        assertTrue(tokeniser.consumeCharacterReference(null, false) instanceof Character);
        
        // Test for consuming character reference with invalid named reference
        reader = new CharacterReader("&invalid;");
        tokeniser = new Tokeniser(reader, errors);
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }
    
    @Test
    public void testCreateTagPending() {
        // Test for creating tag pending
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
        
        tokeniser.createTagPending(false);
        assertTrue(tokeniser.tagPending instanceof Token.EndTag);
    }
    
    @Test
    public void testEmitTagPending() {
        // Test for emitting tag pending
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertTrue(tokeniser.read() instanceof Token.StartTag);
    }
    
    @Test
    public void testCreateCommentPending() {
        // Test for creating comment pending
        tokeniser.createCommentPending();
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }
    
    @Test
    public void testEmitCommentPending() {
        // Test for emitting comment pending
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.read() instanceof Token.Comment);
    }
    
    @Test
    public void testCreateDoctypePending() {
        // Test for creating doctype pending
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }
    
    @Test
    public void testEmitDoctypePending() {
        // Test for emitting doctype pending
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.read() instanceof Token.Doctype);
    }
    
    @Test
    public void testCreateTempBuffer() {
        // Test for creating temporary buffer
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        // Test for checking if appropriate end tag token
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testAppropriateEndTagName() {
        // Test for getting appropriate end tag name
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;
        assertNotNull(tokeniser.appropriateEndTagName());
    }
    
    @Test
    public void testError() {
        // Test for throwing error
        tokeniser.error(TokeniserState.Data);
        assertFalse(errors.isEmpty());
    }
    
    @Test
    public void testEofError() {
        // Test for throwing EOF error
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(errors.isEmpty());
    }
    
    @Test
    public void testCharacterReferenceError() {
        // Test for throwing character reference error
        tokeniser.characterReferenceError("Invalid character reference");
        assertFalse(errors.isEmpty());
    }
    
    @Test
    public void testErrorWithMessage() {
        // Test for throwing error with specific message
        tokeniser.error("Error");
        assertFalse(errors.isEmpty());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test for checking current node in HTML namespace
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}