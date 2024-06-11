package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // Test if read method returns a token
        Token token = tokeniser.read();
        assertNotNull(token);
    }
    
    @Test
    public void testEmit() {
        // Test if emit method sets isEmitPending to true
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
    }
    
    @Test
    public void testEmitString() {
        // Test if emit method appends the given string to charBuffer
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testEmitChar() {
        // Test if emit method appends the given char to charBuffer
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(c, tokeniser.charBuffer.charAt(0));
    }
    
    @Test
    public void testGetState() {
        // Test if getState method returns the current tokenisation state
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }
    
    @Test
    public void testTransition() {
        // Test if transition method sets the current tokenisation state to the given state
        TokeniserState state = TokeniserState.TagName;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }
    
    @Test
    public void testAdvanceTransition() {
        // Test if advanceTransition method advances the reader and sets the current tokenisation state to the given state
        reader = new CharacterReader("abc");
        tokeniser = new Tokeniser(reader, errors);
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
        assertEquals('b', reader.current());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test if acknowledgeSelfClosingFlag method sets selfClosingFlagAcknowledged to true
        tokeniser.selfClosingFlagAcknowledged = false;
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        // Test if consumeCharacterReference method returns the correct character reference
        reader = new CharacterReader("&#65;");
        tokeniser = new Tokeniser(reader, errors);
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        assertEquals('A', (char) tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute));
    }
    
    @Test
    public void testCreateTagPending() {
        // Test if createTagPending method creates a new tag pending and sets it to tagPending
        boolean start = true;
        tokeniser.createTagPending(start);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
    }
    
    @Test
    public void testEmitTagPending() {
        // Test if emitTagPending method sets the tag pending to finalise tag and emits it
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.tagPending.tagName = "div";
        tokeniser.emitTagPending();
        assertEquals("div", ((Token.StartTag) tokeniser.emitPending).tagName);
    }
    
    @Test
    public void testCreateCommentPending() {
        // Test if createCommentPending method creates a new comment pending and sets it to commentPending
        tokeniser.createCommentPending();
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }
    
    @Test
    public void testEmitCommentPending() {
        // Test if emitCommentPending method emits the comment pending
        tokeniser.commentPending = new Token.Comment();
        tokeniser.commentPending.comment = "test comment";
        tokeniser.emitCommentPending();
        assertEquals("test comment", ((Token.Comment) tokeniser.emitPending).comment);
    }
    
    @Test
    public void testCreateDoctypePending() {
        // Test if createDoctypePending method creates a new doctype pending and sets it to doctypePending
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }
    
    @Test
    public void testEmitDoctypePending() {
        // Test if emitDoctypePending method emits the doctype pending
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.doctypePending.name = "html";
        tokeniser.emitDoctypePending();
        assertEquals("html", ((Token.Doctype) tokeniser.emitPending).name);
    }
    
    @Test
    public void testCreateTempBuffer() {
        // Test if createTempBuffer method creates a new data buffer
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        // Test if isAppropriateEndTagToken method returns true if tagPending is appropriate end tag token, else false
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testAppropriateEndTagName() {
        // Test if appropriateEndTagName method returns the tag name of lastStartTag
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";
        assertEquals("div", tokeniser.appropriateEndTagName());
    }
    
    @Test
    public void testErrorWithTokeniserState() {
        // Test if error method adds the error message to errors list when given a tokeniser state
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testEofErrorWithTokeniserState() {
        // Test if eofError method adds the error message to errors list when given a tokeniser state
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testCharacterReferenceError() {
        // Test if characterReferenceError method adds the error message to errors list
        tokeniser.characterReferenceError("Invalid character reference");
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testErrorWithErrorMessage() {
        // Test if error method adds the error message to errors list
        tokeniser.error("Error occurred");
        assertEquals(1, errors.size());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test if currentNodeInHtmlNS method returns true when currentNode is in HTML namespace
        // Not tested as the implementation is not complete
    }
    
    @Test
    public void testConsumeCharacterReferenceWithNoInput() {
        // Test if consumeCharacterReference method returns null when reader is empty
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }

    // Utility method test is not included as the remaining source code is not provided
}