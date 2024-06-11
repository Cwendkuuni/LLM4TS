package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character)token).getData());
    }

    @Test
    public void testEmit() {
        Token.Character character = new Token.Character();
        character.data("test");
        tokeniser.emit(character);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character)token).getData());
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertNotNull(tokeniser.tagPending);
        assertTrue(tokeniser.tagPending.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.name("test");
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("test", ((Token.StartTag)token).tagName());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data("test");
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("test", ((Token.Comment)token).getData());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.reset();
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertEquals(0, tokeniser.dataBuffer.length());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.name("test");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "test";
        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        ParseErrorList errors = new ParseErrorList();
        errors.add(new ParseError(1, "test error"));
        tokeniser.error("test error");
        assertEquals("test error", errors.get(0).getMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        tokeniser.unescapeEntities(true);
    }
}