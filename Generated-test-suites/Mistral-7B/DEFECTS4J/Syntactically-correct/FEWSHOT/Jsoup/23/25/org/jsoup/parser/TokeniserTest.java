package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.TokenType;
import org.jsoup.parser.Tokeniser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniserWithEmptyStream() {
        Tokeniser tokeniser = new Tokeniser(new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        }, new ArrayList<>());
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testTokeniserWithSingleCharacter() {
        String testString = "a";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, testString);
    }

    @Test
    public void testTokeniserWithSingleCharacterEscaped() {
        String testString = "&aacute;";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, Character.toString((char) 0x00E1));
    }

    @Test
    public void testTokeniserWithSingleCharacterFollowedByWhiteSpace() {
        String testString = "a ";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "a");
        token = tokeniser.read();
        assertEquals(token.type, TokenType.Whitespace);
        assertEquals(token.data, " ");
    }

    @Test
    public void testTokeniserWithSingleCharacterFollowedByCharacter() {
        String testString = "aA";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "a");
        token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "A");
    }

    @Test
    public void testTokeniserWithSingleCharacterFollowedByComment() {
        String testString = "a<!-- comment -->";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "a");
        token = tokeniser.read();
        assertTrue(token.type == TokenType.Comment || token.type == TokenType.StartTag);
    }

    @Test
    public void testTokeniserWithSingleCharacterFollowedByStartTag() {
        String testString = "a<p>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "a");
        token = tokeniser.read();
        assertSame(token.type, TokenType.StartTag);
        assertEquals(token.tagName, "p");
    }

    @Test
    public void testTokeniserWithSingleCharacterFollowedByEndTag() {
        String testString = "a</p>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "a");
        token = tokeniser.read();
        assertSame(token.type, TokenType.EndTag);
        assertEquals(token.tagName, "p");
    }

    @Test
    public void testTokeniserWithMultipleCharacters() {
        String testString = "hello";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < testString.length(); i++) {
            tokens.add(tokeniser.read());
        }
        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tokens.get(i).type, TokenType.Character);
            assertEquals(tokens.get(i).data, Character.toString(testString.charAt(i)));
        }
    }

    @Test
    public void testTokeniserWithComment() {
        String testString = "<!-- comment -->";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertTrue(token.type == TokenType.Comment || token.type == TokenType.StartTag);
    }

    @Test
    public void testTokeniserWithStartTag() {
        String testString = "<p>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertSame(token.type, TokenType.StartTag);
        assertEquals(token.tagName, "p");
    }

    @Test
    public void testTokeniserWithStartTagWithAttributes() {
        String testString = "<p class='class1 class2' id='id1'>Content</p>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertSame(token.type, TokenType.StartTag);
        assertEquals(token.tagName, "p");
        Token startTag = token;
        while (token.type != TokenType.EndTag && token.type != TokenType.Eof) {
            token = tokeniser.read();
        }
        Token endTag = token;
        assertTrue(token.type == TokenType.EndTag);
        assertEquals(endTag.tagName, startTag.tagName);
        List<Token.Attribute> attributes = startTag.attributes();
        assertEquals(attributes.size(), 3);
        Token.Attribute classAttribute = attributes.get(0);
        assertEquals(classAttribute.name, "class");
        List<String> classValues = classAttribute.values();
        assertEquals(classValues.size(), 2);
        assertEquals(classValues.get(0), "class1");
        assertEquals(classValues.get(1), "class2");
        Token.Attribute idAttribute = attributes.get(1);
        assertEquals(idAttribute.name, "id");
        assertEquals(idAttribute.values().get(0), "id1");
        Token contentToken = tokeniser.read();
        assertEquals(contentToken.type, TokenType.Text);
        assertEquals(contentToken.data, "Content");
    }

    @Test
    public void testTokeniserWithEndTag() {
        String testString = "</p>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertSame(token.type, TokenType.EndTag);
        assertEquals(token.tagName, "p");
    }

    @Test
    public void testTokeniserWithDoctype() {
        String testString = "<!DOCTYPE html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertSame(token.type, TokenType.Doctype);
        assertEquals(token.data, testString.substring(9));
    }

    @Test
    public void testTokeniserWithEof() {
        String testString = "hello";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "h");
        token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "e");
        token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "l");
        token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "l");
        token = tokeniser.read();
        assertEquals(token.type, TokenType.Eof);
    }

    @Test
    public void testTokeniserWithError() {
        String testString = "<p<";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(token.type, TokenType.Character);
        assertEquals(token.data, "p");
        token = tokeniser.read();
        assertEquals(token.type, TokenType.StartTag);
        assertEquals(token.tagName, "p");
        token = tokeniser.read();
        tokeniser.error("Unexpected '<' in input state [DATA]");
    }
}