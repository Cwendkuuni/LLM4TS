package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private CharacterReader createTestInput(String input) {
        ByteArrayInputStream byteInput = new ByteArrayInputStream(input.getBytes());
        return new CharacterReader(byteInput);
    }

    private ParseErrorList createErrorList() {
        return new ParseErrorList();
    }

    private Tokeniser createTestTokeniser(String input) {
        CharacterReader reader = createTestInput(input);
        ParseErrorList errors = createErrorList();
        return new Tokeniser(reader, errors);
    }

    @Test
    public void testReadAndEmit() {
        String input = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Tokeniser tokeniser = createTestTokeniser(input);

        Token token;
        while ((token = tokeniser.read()) != null) {
            System.out.println(token.toString());
            tokeniser.emit(token);
        }

        assertTrue(tokeniser.getState() == TokeniserState.End);
        assertTrue(tokeniser.errors.size() == 0);
    }

    @Test
    public void testReadCharacterReference() {
        String input = "<html>&#x1F60A;</html>";
        Tokeniser tokeniser = createTestTokeniser(input);

        Character c = tokeniser.consumeCharacterReference(null, false);
        assertEquals("\uD83C\uDDE6", String.valueOf(c));

        c = tokeniser.consumeCharacterReference('#', false);
        assertNull(c);

        c = tokeniser.consumeCharacterReference("amp", false);
        assertEquals('&', c.charValue());

        c = tokeniser.consumeCharacterReference("apos", false);
        assertEquals('\'', c.charValue());

        c = tokeniser.consumeCharacterReference("quot", false);
        assertEquals('"', c.charValue());

        c = tokeniser.consumeCharacterReference("apos", true); // in attribute
        assertNull(c);
    }

    @Test
    public void testReadNamedCharacterReference() {
        String input = "<html>&#x26;amp;</html>";
        Tokeniser tokeniser = createTestTokeniser(input);

        Character c = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', c.charValue());

        c = tokeniser.consumeCharacterReference('&', false);
        assertEquals('amp', c.toString());

        c = tokeniser.consumeCharacterReference("amp", false);
        assertEquals('&', c.charValue());

        c = tokeniser.consumeCharacterReference("amp", true); // in attribute
        assertNull(c);

        c = tokeniser.consumeCharacterReference("apos", false);
        assertEquals('\'', c.charValue());

        c = tokeniser.consumeCharacterReference("apos", true); // in attribute
        assertNull(c);
    }

    @Test
    public void testReadSelfClosingTag() {
        String input = "<img src=\"test.png\" alt=\"Test\" />";
        Tokeniser tokeniser = createTestTokeniser(input);

        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("img", tag.tagName);
        assertTrue(tag.selfClosing);

        tokeniser.emit(tag);

        tokeniser.transition(TokeniserState.Empty);
        tag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tag.type);
        assertEquals("img", tag.tagName);

        tokeniser.emit(tag);
    }

    @Test
    public void testReadTagWithAttributes() {
        String input = "<a href=\"test.html\" target=\"_blank\">Link</a>";
        Tokeniser tokeniser = createTestTokeniser(input);

        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("a", tag.tagName);

        List<Token.Attribute> attributes = tag.attributes;
        assertEquals(2, attributes.size());

        Token.Attribute hrefAttribute = attributes.get(0);
        assertEquals("href", hrefAttribute.name);
        assertEquals("test.html", hrefAttribute.value);

        Token.Attribute targetAttribute = attributes.get(1);
        assertEquals("target", targetAttribute.name);
        assertEquals("_blank", targetAttribute.value);

        tokeniser.emit(tag);

        tag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tag.type);
        assertEquals("a", tag.tagName);

        tokeniser.emit(tag);
    }

    @Test
    public void testReadScriptTag() {
        String input = "<script src=\"test.js\"></script>";
        Tokeniser tokeniser = createTestTokeniser(input);

        tokeniser.createTempBuffer();

        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("script", tag.tagName);
        assertTrue(tag.selfClosing);

        tokeniser.transition(TokeniserState.ScriptData);

        String scriptData = tokeniser.dataBuffer.toString();
        assertEquals("src=\"test.js\"", scriptData);

        tokeniser.emitDoctypePending();
        tokeniser.emitTagPending();

        tokeniser.transition(TokeniserState.Empty);
    }

    @Test
    public void testReadCommentTag() {
        String input = "<!-- Test Comment -->";
        Tokeniser tokeniser = createTestTokeniser(input);

        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.Comment, tag.type);
        assertEquals("Test Comment", tag.content);

        tokeniser.emitCommentPending();

        tag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tag.type);
        assertEquals("!--", tag.tagName);

        tokeniser.emitCommentPending();
    }

    @Test
    public void testReadInvalidInput() {
        String input = "<html><!-- Test <script";
        Tokeniser tokeniser = createTestTokeniser(input);

        tokeniser.createTempBuffer();

        tokeniser.transition(TokeniserState.ScriptData);

        try {
            tokeniser.read();
        } catch (IOException e) {
            // expected
        }

        assertEquals(0, tokeniser.errors.size());
        assertEquals(TokeniserState.Error, tokeniser.getState());
    }

    @Test
    public void testReadInvalidEndTag() {
        String input = "<html><head><title></title></title>";
        Tokeniser tokeniser = createTestTokeniser(input);

        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("title", tag.tagName);

        tokeniser.emit(tag);

        tag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tag.type);
        assertEquals("title", tag.tagName);

        tag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tag.type);
        assertEquals("title", tag.tagName);

        tokeniser.error(TokeniserState.Empty);
    }
}