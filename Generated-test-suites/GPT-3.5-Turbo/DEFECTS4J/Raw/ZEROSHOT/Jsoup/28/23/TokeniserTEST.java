package org.jsoup.parser;

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
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertEquals(str, token.getValue());
        assertEquals(Token.TokenType.Character, token.getType());
    }

    @Test
    public void testEmitCharacter() {
        char c = 't';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertEquals(c, token.getValue().charAt(0));
        assertEquals(Token.TokenType.Character, token.getType());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.name = "test";
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertEquals("test", token.getValue());
        assertEquals(Token.TokenType.StartTag, token.getType());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "test";
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertEquals("test", token.getValue());
        assertEquals(Token.TokenType.Comment, token.getType());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "test";
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertEquals("test", token.getValue());
        assertEquals(Token.TokenType.Doctype, token.getType());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "test";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "test";
        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        ParseErrorList errors = tokeniser.errors;
        assertEquals(1, errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        ParseErrorList errors = tokeniser.errors;
        assertEquals(1, errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("test");
        ParseErrorList errors = tokeniser.errors;
        assertEquals(1, errors.size());
    }

    @Test
    public void testErrorString() {
        tokeniser.error("test");
        ParseErrorList errors = tokeniser.errors;
        assertEquals(1, errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}