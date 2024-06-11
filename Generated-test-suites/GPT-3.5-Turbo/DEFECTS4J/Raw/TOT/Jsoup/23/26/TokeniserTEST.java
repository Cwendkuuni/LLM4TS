package org.jsoup.parser;

import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_NoToken() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testRead_CharacterToken() {
        tokeniser.emit('a');
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.Character);
        assertEquals("a", token.toString());
    }

    @Test
    public void testRead_StartTagToken() {
        tokeniser.createTagPending(true);
        tagPending.attributes.put("attribute", "value");
        tokeniser.emitTagPending();
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.StartTag);
        assertEquals("<tag attribute=\"value\">", token.toString());
    }

    @Test
    public void testRead_EndTagToken() {
        tokeniser.createTagPending(false);
        tokeniser.tagPending.tagName = "tag";
        tokeniser.emitTagPending();
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.EndTag);
        assertEquals("</tag>", token.toString());
    }

    @Test
    public void testEmit_CharacterToken() {
        tokeniser.emit("abc");
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.Character);
        assertEquals("abc", token.toString());
    }

    @Test
    public void testEmit_StartTagToken() {
        tokeniser.createTagPending(true);
        tagPending.tagName = "tag";
        tokeniser.emitTagPending();
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.StartTag);
        assertEquals("<tag>", token.toString());
    }

    @Test
    public void testEmit_EndTagToken() {
        tokeniser.createTagPending(false);
        tagPending.tagName = "tag";
        tokeniser.emitTagPending();
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.EndTag);
        assertEquals("</tag>", token.toString());
    }

    @Test
    public void testCreateTagPending_StartTag() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_EndTag() {
        Token.Tag tag = tokeniser.createTagPending(false);
        assertTrue(tag instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_StartTag() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.tagName = "tag";
        tokeniser.emitTagPending();
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.StartTag);
        assertEquals("<tag>", token.toString());
    }

    @Test
    public void testEmitTagPending_EndTag() {
        Token.Tag tag = tokeniser.createTagPending(false);
        tag.tagName = "tag";
        tokeniser.emitTagPending();
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.EndTag);
        assertEquals("</tag>", token.toString());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "comment";
        tokeniser.emitCommentPending();
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.Comment);
        assertEquals("<!--comment-->", token.toString());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.emitDoctypePending();
        
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.Doctype);
        assertEquals("<!doctype html>", token.toString());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag";
        tokeniser.lastStartTag = tokeniser.createTagPending(true);
        tokeniser.lastStartTag.tagName = "tag";
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = tokeniser.createTagPending(true);
        tokeniser.lastStartTag.tagName = "tag";
        
        assertEquals("tag", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        // Specify the test case
    }

    @Test
    public void testEofError() {
        // Specify the test case
    }

    @Test
    public void testCharacterReferenceError() {
        // Specify the test case
    }

    @Test
    public void testError_String() {
        // Specify the test case
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}