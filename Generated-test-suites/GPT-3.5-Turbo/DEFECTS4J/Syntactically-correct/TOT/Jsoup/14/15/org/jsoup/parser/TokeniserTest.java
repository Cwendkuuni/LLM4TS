package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        StringReader reader = new StringReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals("test", ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testEmitWithBuffer() {
        tokeniser.emit("test");
        tokeniser.emit("123");
        Token emittedToken = tokeniser.read();
        assertEquals("test123", ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('a');
        tokeniser.emit('b');
        Token emittedToken = tokeniser.read();
        assertEquals("ab", ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser.Token.Tag startTag = tokeniser.createTagPending(true);
        startTag.tagName = "div";
        startTag.attributes.put("id", "test");
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, emittedToken.type);
        Token.StartTag emittedStartTag = (Token.StartTag) emittedToken;
        assertEquals("div", emittedStartTag.tagName);
        assertEquals("test", emittedStartTag.attributes.get("id"));
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "test comment";
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.Comment, emittedToken.type);
        Token.Comment emittedComment = (Token.Comment) emittedToken;
        assertEquals("test comment", emittedComment.getData());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.setName("html");
        tokeniser.doctypePending.setPublicIdentifier("test public");
        tokeniser.doctypePending.setSystemIdentifier("test system");
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, emittedToken.type);
        Token.Doctype emittedDoctype = (Token.Doctype) emittedToken;
        assertEquals("html", emittedDoctype.getName());
        assertEquals("test public", emittedDoctype.getPublicIdentifier());
        assertEquals("test system", emittedDoctype.getSystemIdentifier());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser.Token.Tag startTag = tokeniser.createTagPending(true);
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        
        startTag = tokeniser.createTagPending(false);
        startTag.tagName = "div";
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
        
        startTag.tagName = "span";
        
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}