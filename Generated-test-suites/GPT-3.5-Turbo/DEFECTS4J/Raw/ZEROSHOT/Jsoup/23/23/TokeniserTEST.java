package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = new Token.Character("");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String str = "test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(str, ((Token.Character) token).getData());
    }

    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        char c = 'a';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(String.valueOf(c), ((Token.Character) token).getData());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(true);
        assertEquals(true, tokeniser.tagPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTempBuffer();
        assertEquals("", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.tagPending = new Token.StartTag("p");
        tokeniser.lastStartTag = new Token.StartTag("p");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.lastStartTag = new Token.StartTag("p");
        assertEquals("p", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorTokeniserState() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.error(TokeniserState.Data);
        // Check if error is added to error list
    }

    @Test
    public void testEofErrorTokeniserState() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.eofError(TokeniserState.Data);
        // Check if error is added to error list
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.error("Error");
        // Check if error is added to error list
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        // test implementation once namespaces are implemented correctly
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}