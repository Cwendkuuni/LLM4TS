package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testReadEmptyInput() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadWhiteSpace() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(" \t\n\f".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertNull(token);

        token = tokeniser.read();
        assertNull(token);

        token = tokeniser.read();
        assertNull(token);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacter() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("a".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("a", token.getText());
        assertEquals(Token.TokenType.Character, token.type);
    }

    @Test
    public void testReadComment() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<!-- This is a comment -->".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Comment);

        Token token = tokeniser.read();
        assertEquals("This is a comment", token.getText());
        assertEquals(Token.TokenType.Comment, token.type);
    }

    @Test
    public void testReadStartTag() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<p>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("p", token.getTag().getName());
        assertEquals(Token.TokenType.StartTag, token.type);
    }

    @Test
    public void testReadEndTag() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("</p>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("p", token.getTag().getName());
        assertEquals(Token.TokenType.EndTag, token.type);
    }

    @Test
    public void testReadScriptTag() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<script>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("script", token.getTag().getName());
        assertEquals(Token.TokenType.StartTag, token.type);
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<img src=\"image.png\" />".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals("img", token.getTag().getName());
        assertEquals(Token.TokenType.StartTag, token.type);
        assertTrue(token.getTag().isSelfClosing());
    }

    @Test
    public void testReadUnexpectedEndTag() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("</table></p>".getBytes())); // p tag should come before table tag
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        tokeniser.read(); // read "</"
        tokeniser.transition(TokeniserState.EndTag);

        tokeniser.read(); // read "p"
        tokeniser.error("Unexpected end tag: 'p' found, but expected 'table'");

        Token token = tokeniser.read();
        assertEquals("table", token.getTag().getName());
        assertEquals(Token.TokenType.EndTag, token.type);
    }

    @Test
    public void testReadString() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("\"Hello World\"".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        StringBuilder stringBuffer = new StringBuilder();
        tokeniser.emit("\"Hello World\"");

        Token token = tokeniser.read();
        assertEquals("Hello World", token.getText());
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(stringBuffer.toString(), token.getText());
    }

    @Test
    public void testReadCharacterReference() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&#x1F601;".getBytes())); // unicode character: smiley face
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        Character character = tokeniser.consumeCharacterReference('x', false);
        assertEquals('\uD835', (int) character); // unicode character for "x" in hex
        assertEquals('\uDCA1', (int) character); // unicode character for smiley face
    }

    @Test
    public void testReadNamedCharacterReference() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&eacute;".getBytes())); // named character reference: é
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('é', character);
    }

    @Test
    public void testReadCharacterReferenceInAttribute() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&eacute; class=\"foo\"".getBytes())); // named character reference: é in an attribute value
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        Character character = tokeniser.consumeCharacterReference(null, true);
        assertEquals('é', character);
    }

    @Test(expected = AssertionError.class)
    public void testReadInvalidCharacterReference() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&#x10000;".getBytes())); // unicode character outside of valid range
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Character.MIN_VALUE, character.intValue()); // replace with invalid character
    }

    @Test
    public void testReadEndTagWithAttributes() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<p id=\"foo\" class=\"bar\">Content</p>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        Token startTag = tokeniser.read();
        assertEquals("p", startTag.getTag().getName());
        assertEquals(Token.TokenType.StartTag, startTag.type);

        tokeniser.emit("id=\"foo\" class=\"bar\"");

        Token endTag = tokeniser.read();
        assertEquals("p", endTag.getTag().getName());
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertFalse(endTag.getTag().attributes.isEmpty());
        assertEquals("foo", endTag.getTag().getAttributeValue("id"));
        assertEquals("bar", endTag.getTag().getAttributeValue("class"));
    }

    @Test
    public void testReadEndTagWithEmptyAttributes() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<p id=\"\" class=\"\"></p>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        Token startTag = tokeniser.read();
        assertEquals("p", startTag.getTag().getName());
        assertEquals(Token.TokenType.StartTag, startTag.type);

        tokeniser.emit("id=\"\" class=\"\"");

        Token endTag = tokeniser.read();
        assertEquals("p", endTag.getTag().getName());
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertTrue(endTag.getTag().attributes.isEmpty());
    }

    @Test
    public void testReadEndTagWithInvalidAttributes() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<p id=\"foo\" class=\"bar\"></p>".getBytes())); // attribute value contains an end tag
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        Token startTag = tokeniser.read();
        assertEquals("p", startTag.getTag().getName());
        assertEquals(Token.TokenType.StartTag, startTag.type);

        tokeniser.emit("id=\"foo\" class=\"bar\"");

        tokeniser.error("Attributes incorrectly present on end tag");

        Token endTag = tokeniser.read();
        assertNull(endTag);
        assertEquals(1, errors.size());
        assertEquals("Attributes incorrectly present on end tag", errors.get(0).getMessage());
    }

    @Test
    public void testReadEndTagWithMissingSemicolon() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("</p>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        Token startTag = tokeniser.read();
        assertEquals("p", startTag.getTag().getName());
        assertEquals(Token.TokenType.StartTag, startTag.type);

        tokeniser.emit("p");

        tokeniser.error("missing semicolon");

        Token endTag = tokeniser.read();
        assertNull(endTag);
        assertEquals(1, errors.size());
        assertEquals("missing semicolon", errors.get(0).getMessage());
    }
}