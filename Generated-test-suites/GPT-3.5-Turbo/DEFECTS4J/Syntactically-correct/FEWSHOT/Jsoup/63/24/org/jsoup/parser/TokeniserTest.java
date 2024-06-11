package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token1 = tokeniser.read();
        assertNull(token1);

        Token token2 = tokeniser.read();
        assertNull(token2);
    }

    @Test
    public void testEmit() {
        tokeniser.emit(new Token.Character("character"));

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("character", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("string");

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("string", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'c', 'h', 'a', 'r'};
        tokeniser.emit(chars);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("char", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitIntArry() {
        int[] codepoints = {'c', 'o', 'd', 'e', 'p', 'o', 'i', 'n', 't'};
        tokeniser.emit(codepoints);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("codepoint", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('c');

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("c", ((Token.Character) token).getData());
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertEquals(Token.Tag.TokenType.StartTag, tag.type);
        assertEquals("", tag.name());
        assertEquals(true, tag.isSelfClosing());
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.name("div");
        tag.setSelfClosing(false);
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("div", ((Token.StartTag) token).name());
        assertEquals(false, ((Token.StartTag) token).isSelfClosing());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        Token token = tokeniser.read();
        assertEquals("", ((Token.Character) token).getData());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertEquals(false, result);
    }

    @Test
    public void testAppropriateEndTagName() {
        String result = tokeniser.appropriateEndTagName();
        assertNull(result);
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);

        int count = tokeniser.errors.count();
        assertEquals(1, count);
        assertEquals("Unexpected character '' in input state [Data]", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);

        int count = tokeniser.errors.count();
        assertEquals(1, count);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference: message");

        int count = tokeniser.errors.count();
        assertEquals(1, count);
        assertEquals("Invalid character reference: message", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testErrorString() {
        tokeniser.error("Error message");

        int count = tokeniser.errors.count();
        assertEquals(1, count);
        assertEquals("Error message", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertEquals(true, result);
    }
}