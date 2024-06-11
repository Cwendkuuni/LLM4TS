package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser();
        Token.TokenType type = tokeniser.read().type;
        assertEquals(Token.TokenType.StartTag, type);

        type = tokeniser.read().type;
        assertEquals(Token.TokenType.Character, type);

        type = tokeniser.read().type;
        assertEquals(Token.TokenType.EndTag, type);

        type = tokeniser.read().type;
        assertEquals(Token.TokenType.Comment, type);
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.emit(new Token.Character("Test"));
        Token.TokenType type = tokeniser.read().type;
        assertEquals(Token.TokenType.Character, type);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser();
        Token.Tag tag = tokeniser.createTagPending(true);
        assertEquals(true, tag.start);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser();
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emit(tag);
        Token.TokenType type = tokeniser.read().type;
        assertEquals(Token.TokenType.StartTag, type);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.createCommentPending();
        Token.TokenType type = tokeniser.read().type;
        assertEquals(Token.TokenType.Comment, type);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token.TokenType type = tokeniser.read().type;
        assertEquals(Token.TokenType.Comment, type);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.createDoctypePending();
        Token.TokenType type = tokeniser.read().type;
        assertEquals(Token.TokenType.Doctype, type);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token.TokenType type = tokeniser.read().type;
        assertEquals(Token.TokenType.Doctype, type);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.lastStartTag = new Token.StartTag("tag");
        tokeniser.tagPending = new Token.EndTag("tag");
        boolean isAppropriate = tokeniser.isAppropriateEndTagToken();
        assertTrue(isAppropriate);
    }

    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.lastStartTag = new Token.StartTag("tag");
        tokeniser.tagPending = new Token.EndTag("tag");
        String tagName = tokeniser.appropriateEndTagName();
        assertEquals("tag", tagName);
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.error("Error");
        String errorMsg = tokeniser.read().data;
        assertEquals("Error", errorMsg);
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.eofError(TokeniserState.Data);
        String errorMsg = tokeniser.read().data;
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errorMsg);
    }
}