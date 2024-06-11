package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private ParseErrorList errors;
    
    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }
    
    @Test
    public void testRead_shouldReturnTokenCharacter() {
        // typical use case
        tokeniser.emit('<');
        tokeniser.emit('a');
        tokeniser.emit('>');
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("<a>", ((Token.Character) token).getData());
    }
    
    @Test
    public void testRead_shouldReturnTokenStartTag() {
        // typical use case
        tokeniser.emit('<');
        tokeniser.emit("p");
        tokeniser.emit('>');
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals("p", ((Token.StartTag) token).tagName);
    }
    
    @Test
    public void testRead_shouldReturnTokenEndTag() {
        // typical use case
        tokeniser.emit('<');
        tokeniser.emit("/");
        tokeniser.emit("p");
        tokeniser.emit('>');
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.EndTag);
        assertEquals("p", ((Token.EndTag) token).tagName);
    }
    
    @Test
    public void testEmit_shouldCreateTokenCharacter() {
        // typical use case
        tokeniser.emit("Hello");
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("Hello", ((Token.Character) token).getData());
    }
    
    @Test
    public void testEmit_shouldCreateTokenStartTag() {
        // typical use case
        tokeniser.emit("<h1>");
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals("h1", ((Token.StartTag) token).tagName);
    }
    
    @Test
    public void testEmit_shouldCreateTokenEndTag() {
        // typical use case
        tokeniser.emit("</h1>");
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.EndTag);
        assertEquals("h1", ((Token.EndTag) token).tagName);
    }
    
    @Test
    public void testEmit_shouldCreateTokenWithAttributes() {
        tokeniser.emit("<div class=\"container\">");
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) token).tagName);
        assertTrue(((Token.StartTag) token).attributes.containsKey("class"));
        assertEquals("container", ((Token.StartTag) token).attributes.get("class"));
    }
    
    @Test
    public void testEmit_shouldCreateCommentToken() {
        tokeniser.emit("<!-- This is a comment -->");
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
        assertEquals(" This is a comment ", ((Token.Comment) token).getData());
    }
    
    @Test
    public void testEmit_shouldCreateDoctypeToken() {
        tokeniser.emit("<!DOCTYPE html>");
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
        assertEquals("html", ((Token.Doctype) token).getName());
    }
    
    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertNotNull(tokeniser.tagPending);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
    }
    
    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.tagPending.tagName = "p";
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals("p", ((Token.StartTag) token).tagName);
    }
    
    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }
    
    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment();
        tokeniser.commentPending.append('-');
        tokeniser.commentPending.append('-');
        tokeniser.commentPending.append(" This is a comment ");
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
        assertEquals("-This is a comment-", ((Token.Comment) token).getData());
    }
    
    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }
    
    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.doctypePending.append("html");
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
        assertEquals("html", ((Token.Doctype) token).getName());
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending.tagName = "p";
        tokeniser.lastStartTag.tagName = "p";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "p";
        assertEquals("p", tokeniser.appropriateEndTagName());
    }
    
    @Test
    public void testError_shouldAddParseError() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpected character '' in input state [Data]", errors.get(0).getErrorMessage());
    }
    
    @Test
    public void testEofError_shouldAddParseError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getErrorMessage());
    }
    
    @Test
    public void testCharacterReferenceError_shouldAddParseError() {
        tokeniser.characterReferenceError("invalid named reference");
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: invalid named reference", errors.get(0).getErrorMessage());
    }
    
    @Test
    public void testError_shouldNotAddParseErrorIfErrorAddingIsNotAllowed() {
        errors.setMaxErrors(0);
        tokeniser.error("Invalid input");
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS_shouldReturnTrue() {
        // This test assumes that implementing namespaces correctly is pending.
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}