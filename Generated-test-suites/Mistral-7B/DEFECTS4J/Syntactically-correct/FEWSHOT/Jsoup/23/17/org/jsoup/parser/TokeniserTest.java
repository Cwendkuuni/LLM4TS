package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

    @Test
    public void testCharacterReading() throws IOException {
        String testString = "<html>&#x1F44D;<p>Hello World!</p>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        InputStreamReader reader = new InputStreamReader(inputStream);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        while ((token = tokeniser.read()) != null) {
            if (token.type == Token.TokenType.Character) {
                String charString = ((Token.Character) token).chars;
                assertEquals("&#x1F44D;", charString);
                break;
            }
        }
    }

    @Test
    public void testCharacterReferenceParsing() throws IOException {
        String testString = "<html>&#x1F44D;&#x26;#42;&#xA9;";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        InputStreamReader reader = new InputStreamReader(inputStream);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character c = tokeniser.consumeCharacterReference(null, false);
        assertEquals((char) 0xd7c4, c.intValue());

        c = tokeniser.consumeCharacterReference('&', false);
        assertEquals('&', c.intValue());

        c = tokeniser.consumeCharacterReference("\u00A9", false);
        assertEquals('©', c.intValue());
    }

    @Test
    public void testStateReading() throws IOException {
        String testString = "<html><head></head><body></body>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        InputStreamReader reader = new InputStreamReader(inputStream);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);

        tokeniser.transition(TokeniserState.Script);
        state = tokeniser.getState();
        assertEquals(TokeniserState.Script, state);

        tokeniser.transition(TokeniserState.SelfClosingStartTag);
        state = tokeniser.getState();
        assertEquals(TokeniserState.SelfClosingStartTag, state);

        tokeniser.transition(TokeniserState.CharacterReference);
        state = tokeniser.getState();
        assertEquals(TokeniserState.CharacterReference, state);

        tokeniser.transition(TokeniserState.Comment);
        state = tokeniser.getState();
        assertEquals(TokeniserState.Comment, state);

        tokeniser.transition(TokeniserState.EndTag);
        state = tokeniser.getState();
        assertEquals(TokeniserState.EndTag, state);
    }

    @Test
    public void testTagEmitting() throws IOException {
        String testString = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        InputStreamReader reader = new InputStreamReader(inputStream);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        List<Token> tokens = new ArrayList<>();
        while ((token = tokeniser.read()) != null) {
            tokens.add(token);
        }

        Token startTag = tokens.get(0);
        assertTrue(startTag.type == Token.TokenType.StartTag);
        assertEquals("html", startTag.tagName);

        Token endTag = tokens.get(tokens.size() - 1);
        assertTrue(endTag.type == Token.TokenType.EndTag);
        assertEquals("html", endTag.tagName);

        Token startTitle = tokens.get(1);
        assertTrue(startTitle.type == Token.TokenType.StartTag);
        assertEquals("title", startTitle.tagName);

        Token endTitle = tokens.get(3);
        assertTrue(endTitle.type == Token.TokenType.EndTag);
        assertEquals("title", endTitle.tagName);

        Token pTag = tokens.get(7);
        assertTrue(pTag.type == Token.TokenType.StartTag);
        assertEquals("p", pTag.tagName);

        Token textNode = tokens.get(9);
        assertTrue(textNode.type == Token.TokenType.Text);
        assertEquals("Hello World!", textNode.text);
    }

    @Test
    public void testErrorHandling() throws IOException {
        String testString = "<html><script><script";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        InputStreamReader reader = new InputStreamReader(inputStream);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertNull(token);

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character '<' in input state [Data]", error.getMessage());
    }

    @Test
    public void testEndTagEmitting() throws IOException {
        String testString = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        InputStreamReader reader = new InputStreamReader(inputStream);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);
        tokeniser.consumeCharacterReference(null, false); // consume &#x2F;

        tokeniser.emit(tokeniser.createTagPending(false)); // emit </html>
        tokeniser.emitCommentPending(); // consume comment

        Token token = tokeniser.read();
        assertTrue(token.type == Token.TokenType.EndTag);
        assertEquals("html", token.tagName);
    }
}