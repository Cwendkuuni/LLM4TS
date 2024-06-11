package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Tag;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTokeniserInitialisation() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertNull(tokeniser.emitPending);
        assertNull(tokeniser.lastStartTag);
        assertNull(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testEmitPendingToken() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token startTag = tokeniser.createTagPending(true).name("test").data("test");
        tokeniser.emit(startTag);
        assertEquals(Token.TokenType.StartTag, tokeniser.read().type);
        assertSame(startTag, tokeniser.read());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("test");
        reader.current('&');
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertNull(codepoints);

        reader.current('#');
        reader.mark();
        reader.current('x');
        reader.consume();
        reader.consumeHexSequence();
        reader.consume(';');
        int codePoint = 0xFF;
        int[] expectedCodepoints = {codePoint};
        assertArrayEquals(expectedCodepoints, tokeniser.consumeCharacterReference(null, false));

        reader.reset();
        reader.current('#');
        reader.consume();
        reader.consumeLetterThenDigitSequence();
        reader.consume(';');
        String name = "amp";
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        assertArrayEquals(Entities.AMPERSAND_CODEPOINTS, codeRef);

        reader.reset();
        reader.current('#');
        reader.consume();
        reader.consumeLetterThenDigitSequence();
        reader.consume(';');
        String invalidNamedReference = "invalidref";
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testEmitString() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        String input = "test";
        tokeniser.emit(input);
        StringBuilder expected = new StringBuilder("test");
        assertEquals(Token.TokenType.Text, tokeniser.read().type);
        assertEquals(expected.toString(), tokeniser.read().data());
    }

    @Test
    public void testEmitCharacter() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(Token.TokenType.Text, tokeniser.read().type);
        assertEquals("a", tokeniser.read().data());
    }

    @Test
    public void testReadStartTag() {
        CharacterReader reader = new CharacterReader("<test>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token startTag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals("test", startTag.tagName());
        assertSame(Token.StartTag.class, startTag.getClass());
    }

    @Test
    public void testReadEndTag() {
        CharacterReader reader = new CharacterReader("/test>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token endTag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals("test", endTag.tagName());
        assertSame(Token.EndTag.class, endTag.getClass());
    }

    @Test
    public void testReadDoctype() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token doctype = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertSame(Token.Doctype.class, doctype.getClass());
    }

    @Test
    public void testReadComment() {
        CharacterReader reader = new CharacterReader("<!-- comment -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token comment = tokeniser.read();
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals("comment", comment.data());
        assertSame(Token.Comment.class, comment.getClass());
    }

    @Test
    public void testReadText() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token textToken = tokeniser.read();
        assertEquals(Token.TokenType.Text, textToken.type);
        assertEquals("test", textToken.data());
    }

    @Test
    public void testReadEmptyText() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token emptyTextToken = tokeniser.read();
        assertEquals(Token.TokenType.Text, emptyTextToken.type);
        assertEquals("", emptyTextToken.data());
    }

    @Test
    public void testReadTokensWithEofError() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        reader.consumeAll();
        assertEquals(Token.TokenType.Eof, tokeniser.read().type);
        assertTrue(errors.hasErrors());
    }

    @Test
    public void testReadTokensWithParseError() {
        CharacterReader reader = new CharacterReader("&invalid");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        assertEquals(Token.TokenType.Text, tokeniser.read().type);
        assertEquals("", tokeniser.read().data());
        assertTrue(errors.hasErrors());
    }
}