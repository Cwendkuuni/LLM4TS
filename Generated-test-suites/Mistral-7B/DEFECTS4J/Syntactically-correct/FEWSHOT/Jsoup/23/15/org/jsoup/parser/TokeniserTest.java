package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testReadEmptyInput() {
        CharacterReader reader = new CharacterReader("") {};
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadText() {
        CharacterReader reader = new CharacterReader("Hello World");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            tokens.add(tokeniser.read());
        }

        Token lastToken = tokens.get(tokens.size() - 1);
        assertEquals(Token.TokenType.EndDocument, lastToken.type);

        Token firstToken = tokens.get(0);
        assertEquals(Token.TokenType.Character, firstToken.type);
        assertEquals("H", firstToken.charContent());
        assertEquals("Hello World".substring(0, 1), firstToken.charContentOriginal());

        Token lastButOneToken = tokens.get(tokens.size() - 2);
        assertEquals(Token.TokenType.Character, lastButOneToken.type);
        assertEquals("W", lastButOneToken.charContent());
        assertEquals("World".substring(6, 7), lastButOneToken.charContentOriginal());

        for (int i = 0; i < tokens.size() - 1; i++) {
            Token token = tokens.get(i);
            assertEquals(Token.TokenType.Character, token.type);
            assertEquals(String.valueOf(token.charContentOriginal().charAt(0)), token.charContent());
        }
    }

    @Test
    public void testReadTag() {
        CharacterReader reader = new CharacterReader("<html><head><title>Hello</title></head><body><p>World</p></body></html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            tokens.add(tokeniser.read());
        }

        Token lastToken = tokens.get(tokens.size() - 1);
        assertEquals(Token.TokenType.EndDocument, lastToken.type);

        Token htmlTag = tokens.get(0);
        assertEquals(Token.TokenType.StartTag, htmlTag.type);
        assertEquals("html", htmlTag.tagName);

        Token titleTag = tokens.get(9);
        assertEquals(Token.TokenType.StartTag, titleTag.type);
        assertEquals("title", titleTag.tagName);
        assertEquals(5, titleTag.attributes.size());

        Token titleText = tokens.get(10);
        assertEquals(Token.TokenType.Character, titleText.type);
        assertEquals("Hello", titleText.charContent());

        Token bodyTag = tokens.get(14);
        assertEquals(Token.TokenType.StartTag, bodyTag.type);
        assertEquals("body", bodyTag.tagName);

        Token pTag = tokens.get(16);
        assertEquals(Token.TokenType.StartTag, pTag.type);
        assertEquals("p", pTag.tagName);

        Token pText = tokens.get(17);
        assertEquals(Token.TokenType.Character, pText.type);
        assertEquals("World", pText.charContent());

        Token endPTag = tokens.get(18);
        assertEquals(Token.TokenType.EndTag, endPTag.type);
        assertEquals("p", endPTag.tagName);

        Token endBodyTag = tokens.get(19);
        assertEquals(Token.TokenType.EndTag, endBodyTag.type);
        assertEquals("body", endBodyTag.tagName);

        Token endHeadTag = tokens.get(15);
        assertEquals(Token.TokenType.EndTag, endHeadTag.type);
        assertEquals("head", endHeadTag.tagName);

        Token endHtmlTag = tokens.get(1);
        assertEquals(Token.TokenType.EndTag, endHtmlTag.type);
        assertEquals("html", endHtmlTag.tagName);
    }

    @Test   
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src=\"foo.jpg\" />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            tokens.add(tokeniser.read());
        }

        Token lastToken = tokens.get(tokens.size() - 1);
        assertEquals(Token.TokenType.EndDocument, lastToken.type);

        Token imgTag = tokens.get(0);
        assertEquals(Token.TokenType.StartTag, imgTag.type);
        assertEquals("img", imgTag.tagName);
        assertTrue(imgTag.selfClosing);
    }

    @Test
    public void testReadComment() {
        CharacterReader reader = new CharacterReader("<!-- This is a comment -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tokens.add(tokeniser.read());
        }

        Token lastToken = tokens.get(tokens.size() - 1);
        assertEquals(Token.TokenType.EndDocument, lastToken.type);

        Token commentToken = tokens.get(0);
        assertEquals(Token.TokenType.Comment, commentToken.type);
        assertEquals("This is a comment ", commentToken.charContent());
    }

    @Test
    public void testReadDoctype() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            tokens.add(tokeniser.read());
        }

        Token lastToken = tokens.get(tokens.size() - 1);
        assertEquals(Token.TokenType.EndDocument, lastToken.type);

        Token doctypeToken = tokens.get(0);
        assertEquals(Token.TokenType.Doctype, doctypeToken.type);
        assertEquals("html", doctypeToken.doctypeName);
    }

    @Test
    public void testReadErrorHandling() {
        CharacterReader reader = new CharacterReader("<html><head><title>Hello</title><script>//<![CDATA[ this.foo = 42; //]]>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            tokens.add(tokeniser.read());
        }

        Token lastToken = tokens.get(tokens.size() - 1);
        assertEquals(Token.TokenType.EndDocument, lastToken.type);

        Token htmlTag = tokens.get(0);
        assertEquals(Token.TokenType.StartTag, htmlTag.type);
        assertEquals("html", htmlTag.tagName);

        Token titleTag = tokens.get(4);
        assertEquals(Token.TokenType.StartTag, titleTag.type);
        assertEquals("title", titleTag.tagName);

        Token titleText = tokens.get(5);
        assertEquals(Token.TokenType.Character, titleText.type);
        assertEquals("Hello", titleText.charContent());

        Token scriptTag = tokens.get(6);
        assertEquals(Token.TokenType.StartTag, scriptTag.type);
        assertEquals("script", scriptTag.tagName);

        Token scriptContent = tokens.get(7);
        assertEquals(Token.TokenType.Text, scriptContent.type);
        assertEquals("//<![CDATA[ this.foo = 42; //]]>", scriptContent.charContent());

        Token endScriptTag = tokens.get(8);
        assertEquals(Token.TokenType.EndTag, endScriptTag.type);
        assertEquals("script", endScriptTag.tagName);

        Token error = tokens.get(9);
        assertEquals(Token.TokenType.Error, error.type);
        assertEquals("Unexpected character '[' in state [Script]", error.errorMessage);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&eacute;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals((char) 0xEA, character);

        character = tokeniser.consumeCharacterReference('x', false);
        assertEquals((char) 0x00C3, character);

        character = tokeniser.consumeCharacterReference(null, true);
        assertEquals(Tokeniser.replacementChar, character);

        character = tokeniser.consumeCharacterReference("abc", true);
        assertEquals(Tokeniser.replacementChar, character);

        character = tokeniser.consumeCharacterReference("&#x1F602;", false);
        assertEquals((char) 0x1F602, character);

        character = tokeniser.consumeCharacterReference("&#x{1F602};", false);
        assertEquals((char) 0x1F602, character);

        character = tokeniser.consumeCharacterReference("&#x1F602;", true);
        assertEquals(Tokeniser.replacementChar, character);

        character = tokeniser.consumeCharacterReference("&#10000;", false);
        assertEquals(Tokeniser.replacementChar, character);

        character = tokeniser.consumeCharacterReference("&#10000;", true);
        assertEquals(Tokeniser.replacementChar, character);

        character = tokeniser.consumeCharacterReference("&#x10FFED;", false);
        assertEquals((char) 0x10FFED, character);

        character = tokeniser.consumeCharacterReference("&#x10FFED;", true);
        assertEquals((char) 0x10FFED, character);

        character = tokeniser.consumeCharacterReference("&#x1FFFF;", false);
        assertEquals((char) 0x1FFFF, character);

        character = tokeniser.consumeCharacterReference("&#x1FFFF;", true);
        assertEquals(Tokeniser.replacementChar, character);
    }
}