package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.TokeniserState;

import java.io.StringReader;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() {
        CharacterReader reader = new StringReader(" <html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, world!</p>\n</body>\n");
        ParseErrorList errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        List<Token> tokens = new ArrayList<>();
        while ((token = tokeniser.read()) != null) {
            tokens.add(token);
        }

        // Test that all tokens were correctly read
        assertEquals(15, tokens.size());
        assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
        assertEquals("html", tokens.get(0).tagName);
        assertEquals(Token.TokenType.EndTag, tokens.get(1).type);
        assertEquals("html", tokens.get(1).tagName);
        assertEquals(Token.TokenType.StartTag, tokens.get(2).type);
        assertEquals("head", tokens.get(2).tagName);
        assertEquals(Token.TokenType.EndTag, tokens.get(3).type);
        assertEquals("head", tokens.get(3).tagName);
        assertEquals(Token.TokenType.StartTag, tokens.get(4).type);
        assertEquals("title", tokens.get(4).tagName);
        assertEquals(Token.TokenType.Character, tokens.get(5).type);
        assertEquals("T", tokens.get(5).data);
        assertEquals(Token.TokenType.Text, tokens.get(6).type);
        assertEquals("est", tokens.get(6).data);
        assertEquals(Token.TokenType.Text, tokens.get(7).type);
        assertEquals("</", tokens.get(7).data);
        assertEquals(Token.TokenType.Character, tokens.get(8).type);
        assertEquals(">", tokens.get(8).data);
        assertEquals(Token.TokenType.EndTag, tokens.get(9).type);
        assertEquals("title", tokens.get(9).tagName);
        assertEquals(Token.TokenType.StartTag, tokens.get(10).type);
        assertEquals("body", tokens.get(10).tagName);
        assertEquals(Token.TokenType.EndTag, tokens.get(11).type);
        assertEquals("body", tokens.get(11).tagName);
        assertEquals(Token.TokenType.StartTag, tokens.get(12).type);
        assertEquals("p", tokens.get(12).tagName);
        assertEquals(Token.TokenType.Character, tokens.get(13).type);
        assertEquals("H", tokens.get(13).data);
        assertEquals(Token.TokenType.Text, tokens.get(14).type);
        assertEquals("ello, world!", tokens.get(14).data);
        assertEquals(Token.TokenType.Character, tokens.get(15).type);
        assertEquals("", tokens.get(15).data);

        // Test that emitting tokens works correctly
        assertEquals(15, tokens.size());
        tokeniser = new Tokeniser(new StringReader(""), errors);
        token = tokeniser.read();
        tokeniser.emit(token);
        token = tokeniser.read();
        tokeniser.emit(token);
        tokeniser.emit(new StartTag("html"));
        tokeniser.emit(new EndTag("html"));
        tokeniser.emit(new StartTag("head"));
        tokeniser.emit(new EndTag("head"));
        tokeniser.emit(new StartTag("title"));
        tokeniser.emit(new Character('T'));
        tokeniser.emit(new Text("est"));
        tokeniser.emit(new Text(""));
        tokeniser.emit(new Text("</"));
        tokeniser.emit(new Character('>'));
        tokeniser.emit(new EndTag("title"));
        tokeniser.emit(new StartTag("body"));
        tokeniser.emit(new EndTag("body"));
        tokeniser.emit(new StartTag("p"));
        tokeniser.emit(new Character('H'));
        tokeniser.emit(new Text("ello, world!"));
        tokeniser.emit(new Character(''));

        // Test that emitting a tag correctly sets lastStartTag
        tokeniser = new Tokeniser(new StringReader(""), errors);
        tokeniser.transition(TokeniserState.Tag);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertSame(tokeniser.lastStartTag, tokeniser.tagPending);

        // Test that isAppropriateEndTagToken returns true for appropriate end tags
        tokeniser.transition(TokeniserState.Tag);
        tokeniser.createTagPending(true);
        tokeniser.createTagPending(false);
        assertTrue(tokeniser.isAppropriateEndTagToken());

        // Test that isAppropriateEndTagToken returns false for inappropriate end tags
        tokeniser.transition(TokeniserState.Tag);
        tokeniser.createTagPending(true);
        tokeniser.createTagPending(false);
        tokeniser.tagPending.tagName = "body";
        assertFalse(tokeniser.isAppropriateEndTagToken());

        // Test that error methods correctly add errors
        tokeniser = new Tokeniser(new StringReader(""), errors);
        tokeniser.error(TokeniserState.Tag);
        assertEquals(1, errors.size());
        assertEquals("Unexpected character ' ' in input state [Tag]", errors.get(0).getMessage());

        tokeniser.eofError(TokeniserState.Tag);
        assertEquals(2, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Tag]", errors.get(1).getMessage());

        tokeniser.characterReferenceError("character outside of valid range");
        assertEquals(3, errors.size());
        assertEquals("Invalid character reference: character outside of valid range", errors.get(2).getMessage());

        // Test that errors are correctly cleared
        errors.clear();
        tokeniser.transition(TokeniserState.Tag);
        tokeniser.error(TokeniserState.Tag);
        assertEquals(0, errors.size());
    }
}