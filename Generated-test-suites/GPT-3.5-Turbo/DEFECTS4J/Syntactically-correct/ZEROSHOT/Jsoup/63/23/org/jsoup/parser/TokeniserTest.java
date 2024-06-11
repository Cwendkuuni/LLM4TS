package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        Token token = tokeniser.read();
        assertNull(token);
    }
    
    @Test
    public void testEmit() {
        Token token = new Token.Comment();
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }
    
    @Test
    public void testCreateTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertTrue(tagPending instanceof Token.StartTag);
    }
    
    @Test
    public void testEmitTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(tagPending, emittedToken);
    }
    
    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Comment);
    }
    
    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Comment);
    }
    
    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Doctype);
    }
    
    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Doctype);
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testAppropriateEndTagName() {
        assertNull(tokeniser.appropriateEndTagName());
    }
    
    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        ParseError error = errors.get(0);
        assertEquals("Unexpected character 'null' in input state [Data]", error.toString());
    }
    
    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.toString());
    }
    
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference: Invalid character reference", error.toString());
    }
    
    @Test
    public void testErrorString() {
        tokeniser.error("Error message");
        ParseError error = errors.get(0);
        assertEquals("Error message", error.toString());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
    
    @Test
    public void testUnescapeEntities() {
        String unescapedString = tokeniser.unescapeEntities(false);
        assertEquals("", unescapedString);
    }
}