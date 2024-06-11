package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;
    
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
        Token token = new Token.Character("foo");
        tokeniser.emit(token);
        
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Character);
        assertEquals("foo", ((Token.Character) emittedToken).getData());
    }
    
    @Test
    public void testEmitString() {
        tokeniser.emit("foo");
        tokeniser.emit("bar");
        
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Character);
        assertEquals("foobar", ((Token.Character) emittedToken).getData());
    }
    
    @Test
    public void testEmitChar() {
        tokeniser.emit('f');
        tokeniser.emit('o');
        tokeniser.emit('o');
        
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Character);
        assertEquals("foo", ((Token.Character) emittedToken).getData());
    }
    
    @Test
    public void testCreateTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertTrue(tagPending instanceof Token.StartTag);
    }
    
    @Test
    public void testEmitTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tagPending.setName("div");
        
        tokeniser.emitTagPending();
        
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) emittedToken).tagName());
    }
    
    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token.Comment commentPending = tokeniser.commentPending;
        assertNotNull(commentPending);
    }
    
    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.setData("This is a comment");
        
        tokeniser.emitCommentPending();
        
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Comment);
        assertEquals("This is a comment", ((Token.Comment) emittedToken).getData());
    }
    
    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token.Doctype doctypePending = tokeniser.doctypePending;
        assertNotNull(doctypePending);
    }
    
    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.setName("html");
        
        tokeniser.emitDoctypePending();
        
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Doctype);
        assertEquals("html", ((Token.Doctype) emittedToken).getName());
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.setName("div");
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.setName("div");
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
        
        tokeniser.tagPending.setName("p");
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.setName("div");
        
        assertEquals("div", tokeniser.appropriateEndTagName());
    }
    
    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        
        ParseError parseError = errors.get(0);
        assertNotNull(parseError);
        assertEquals(0, parseError.getPosition());
        assertEquals("Unexpected character '' in input state [Data]", parseError.getMessage());
    }
    
    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        
        ParseError parseError = errors.get(0);
        assertNotNull(parseError);
        assertEquals(0, parseError.getPosition());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", parseError.getMessage());
    }
    
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");
        
        ParseError parseError = errors.get(0);
        assertNotNull(parseError);
        assertEquals(0, parseError.getPosition());
        assertEquals("Invalid character reference: Invalid character reference", parseError.getMessage());
    }
    
    @Test
    public void testErrorString() {
        tokeniser.error("Invalid attribute");
        
        ParseError parseError = errors.get(0);
        assertNotNull(parseError);
        assertEquals(0, parseError.getPosition());
        assertEquals("Invalid attribute", parseError.getMessage());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
    
    // Add more test cases here...
}