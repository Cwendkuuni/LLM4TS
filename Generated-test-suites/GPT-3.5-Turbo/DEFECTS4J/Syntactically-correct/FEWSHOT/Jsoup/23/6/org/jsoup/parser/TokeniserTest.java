package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token token = tokeniser.read(); // Test read method
        
        assertEquals(null, token); // Test that the initial token is null
    }
    
    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token token = new Token();
        tokeniser.emit(token); // Test emit method
        
        assertEquals(token, tokeniser.read()); // Test that the emitted token is returned by read
    }
    
    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Character reference = tokeniser.consumeCharacterReference('a', false); // Test consumeCharacterReference method
        
        assertEquals(null, reference); // Test that the consumed character reference is null
    }
    
    @Test
    public void testCreateTagPending() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token.Tag tagPending = tokeniser.createTagPending(true); // Test createTagPending method
        
        assertTrue(tagPending instanceof Token.StartTag); // Test that the tagPending instance is a StartTag instance
    }
    
    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token.Tag tagPending = new Token.StartTag();
        tokeniser.emit(tagPending); // Test emitTagPending method
        
        assertEquals(tagPending, tokeniser.read()); // Test that the emitted tagPending is returned by read
    }
    
    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createCommentPending(); // Test createCommentPending method
        
        assertTrue(tokeniser.commentPending instanceof Token.Comment); // Test that the commentPending instance is a Comment instance
    }
    
    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending(); // Test emitCommentPending method
        
        assertTrue(tokeniser.read() instanceof Token.Comment); // Test that the emitted commentPending is returned by read
    }
    
    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createDoctypePending(); // Test createDoctypePending method
        
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype); // Test that the doctypePending instance is a Doctype instance
    }
    
    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending(); // Test emitDoctypePending method
        
        assertTrue(tokeniser.read() instanceof Token.Doctype); // Test that the emitted doctypePending is returned by read
    }
    
    @Test
    public void testCreateTempBuffer() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createTempBuffer(); // Test createTempBuffer method
        
        assertNotNull(tokeniser.dataBuffer); // Test that the dataBuffer instance is not null
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.tagPending = new Token.StartTag("tag");
        tokeniser.lastStartTag = new Token.StartTag("tag"); // Test isAppropriateEndTagToken method
        
        assertTrue(tokeniser.isAppropriateEndTagToken()); // Test that isAppropriateEndTagToken returns true when the lastStartTag is equal to the tagPending
    }
    
    @Test
    public void testAppropriateEndTagName() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.lastStartTag = new Token.StartTag("tag"); // Test appropriateEndTagName method
        
        assertEquals("tag", tokeniser.appropriateEndTagName()); // Test that the appropriate end tag name is returned
    }
    
    @Test
    public void testError() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        TokeniserState state = TokeniserState.Data;
        
        tokeniser.error(state); // Test error method
        
        assertEquals(1, errors.size()); // Test that an error is added to the error list
    }
    
    @Test
    public void testEofError() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        TokeniserState state = TokeniserState.Data;
        
        tokeniser.eofError(state); // Test eofError method
        
        assertEquals(1, errors.size()); // Test that an error is added to the error list
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        CharacterReader reader = new CharacterReader("input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS(); // Test currentNodeInHtmlNS method
        
        assertTrue(inHtmlNS); // Test that currentNodeInHtmlNS returns true
    }
}