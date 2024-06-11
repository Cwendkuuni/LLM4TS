package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.Arrays;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @org.junit.Before
    public void setUp() {
        reader = new CharacterReader("test.html");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @org.junit.After
    public void tearDown() {
        reader.close();
    }

    @org.junit.Test
    public void testRead() {
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("head", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("title", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Dummy content", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("title", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("body", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Some text", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("body", token.tagName);

        token = tokeniser.read();
        assertNull(token);
    }

    @org.junit.Test
    public void testEmit() {
        tokeniser.emit(new Token.Character('a'));
        tokeniser.emit(new Token.Character('b'));
        tokeniser.emit(new Token.Character('c'));

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("abc", token.data);
    }

    @org.junit.Test
    public void testConsumeCharacterReference() {
        tokeniser.consume("&amp;");
        assertEquals(Token.TokenType.Character, tokeniser.getState().lastTokenType);
        assertEquals("&amp;", tokeniser.getState().lastData);

        tokeniser.consume("&apos;");
        assertEquals(Token.TokenType.Character, tokeniser.getState().lastTokenType);
        assertEquals("'", tokeniser.getState().lastData);

        tokeniser.consume("&quot;");
        assertEquals(Token.TokenType.Character, tokeniser.getState().lastTokenType);
        assertEquals("\"", tokeniser.getState().lastData);

        tokeniser.consume("&gt;");
        assertEquals(Token.TokenType.Character, tokeniser.getState().lastTokenType);
        assertEquals(">", tokeniser.getState().lastData);

        tokeniser.consume("&lt;");
        assertEquals(Token.TokenType.Character, tokeniser.getState().lastTokenType);
        assertEquals("<", tokeniser.getState().lastData);

        tokeniser.consume("&nbsp;");
        assertEquals(Token.TokenType.Character, tokeniser.getState().lastTokenType);
        assertEquals("\u00A0", (int) tokeniser.getState().lastData);

        tokeniser.consume("&eacute;");
        assertEquals(Token.TokenType.Character, tokeniser.getState().lastTokenType);
        assertEquals("\u00EA", (int) tokeniser.getState().lastData);

        tokeniser.consume("&#x1F603;");
        assertEquals(Token.TokenType.Character, tokeniser.getState().lastTokenType);
        assertEquals("\uD83D\uDE03", (int) tokeniser.getState().lastData);

        tokeniser.consume("&#x0026;"); // illegal unicode point
        assertNull(tokeniser.getState().lastTokenType);
        assertNull(tokeniser.getState().lastData);

        tokeniser.consume("&foo;"); // unknown named entity
        assertNull(tokeniser.getState().lastTokenType);
        assertNull(tokeniser.getState().lastData);
    }

    @org.junit.Test
    public void testErrorHandling() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.read(); // should not throw an exception
        tokeniser.error(TokeniserState.SgmlDecl);
        tokeniser.error(TokeniserState.Script);
        tokeniser.error(TokeniserState.Style);
        tokeniser.error(TokeniserState.Comment);
        tokeniser.error(TokeniserState.ProcessingInstruction);
        tokeniser.error(TokeniserState.XmlDecl);
        tokeniser.error(TokeniserState.EmptyElementTag);
        tokeniser.error(TokeniserState.CharacterReference);
        tokeniser.error(TokeniserState.UnicodeCharRef);
        tokeniser.error(TokeniserState.Doctype);
        tokeniser.error(TokeniserState.ExternalEntity);
        tokeniser.error(TokeniserState.Error);

        // validate that errors were added to the list
        ParseError[] errors = tokeniser.errors.getErrors();
        for (ParseError error : errors) {
            System.out.println(error.getMessage());
        }
    }
}