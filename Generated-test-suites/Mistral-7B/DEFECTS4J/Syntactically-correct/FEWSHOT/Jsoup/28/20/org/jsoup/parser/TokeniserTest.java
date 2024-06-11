package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.Tokeniser;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public int pos() {
                return 0;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertNull(tokeniser.read());
        tokeniser.transition(TokeniserState.StartTagBegin);
        assertSame(TokeniserState.StartTagBegin, tokeniser.getState());
        tokeniser.emit("<html");
        Token startTagHtml = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, startTagHtml.type);
        assertEquals("html", startTagHtml.tagName);
        assertTrue(startTagHtml.isStartTag());
        assertEquals(TokeniserState.Middle, tokeniser.getState());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit(">");
        assertNull(tokeniser.read());
        tokeniser.transition(TokeniserState.EndTagBegin);
        tokeniser.emit("/html");
        Token endTagHtml = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, endTagHtml.type);
        assertEquals("html", endTagHtml.tagName);
        assertTrue(endTagHtml.isEndTag());
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testCharacterBuffer() {
        CharacterReader reader = new CharacterReader("abcd") {
            @Override
            public int pos() {
                return 0;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit('a');
        tokeniser.emit('b');
        tokeniser.emit('c');
        tokeniser.emit('d');

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("abcd", token.getChars().toString());
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testCharacterReference() {
        CharacterReader reader = new CharacterReader("&a;") {
            @Override
            public int pos() {
                return 0;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character a = tokeniser.consumeCharacterReference(null, false);
        assertEquals('a', a);
        assertEquals(TokeniserState.Data, tokeniser.getState());

        Character aRef = tokeniser.consumeCharacterReference('a', false);
        assertNull(aRef);
        assertEquals(TokeniserState.Data, tokeniser.getState());

        Character ampersand = tokeniser.consumeCharacterReference(null, true);
        assertEquals('&', ampersand);
        assertEquals(TokeniserState.Data, tokeniser.getState());

        Character semicolon = tokeniser.consumeCharacterReference(null, false);
        assertEquals(';', semicolon);
        assertEquals(TokeniserState.Data, tokeniser.getState());

        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.Character, tag.type);
        assertEquals(";&", tag.getChars().toString());
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testSelfClosingFlag() {
        CharacterReader reader = new CharacterReader(" <img src=\"foo.png\" />") {
            @Override
            public int pos() {
                return 0;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("img", tag.tagName);
        assertTrue(tag.isStartTag());
        assertTrue(tag.isSelfClosing());
        assertEquals(TokeniserState.EmptyElemTag, tokeniser.getState());

        Token tag2 = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tag2.type);
        assertEquals("img", tag2.tagName);
        assertTrue(tag2.isEndTag());
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testComment() {
        CharacterReader reader = new CharacterReader("<!-- comment -->") {
            @Override
            public int pos() {
                return 0;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("comment", tag.tagName);
        assertTrue(tag.isStartTag());
        assertTrue(tag.isComment());
        assertEquals(TokeniserState.Comment, tokeniser.getState());

        Token comment = tokeniser.read();
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals("comment", comment.tagName);
        assertTrue(comment.isComment());
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testDoctype() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>") {
            @Override
            public int pos() {
                return 0;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("DOCTYPE", tag.tagName);
        assertTrue(tag.isStartTag());
        assertTrue(tag.isDoctype());
        assertEquals(TokeniserState.Doctype, tokeniser.getState());

        Token doctype = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals("html", doctype.tagName);
        assertTrue(doctype.isDoctype());
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testErrorHandling() {
        CharacterReader reader = new CharacterReader("<!--") {
            @Override
            public int pos() {
                return 0;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.error(TokeniserState.Data);
        assertTrue(errors.size() > 0);
        Token error = errors.get(0);
        assertEquals("Unexpected character '<?' in input state [DATA]", error.getMessage());
        assertEquals(error.getLine(), 0);
        assertEquals(error.getColumn(), 0);

        errors.clear();
        tokeniser.transition(TokeniserState.Comment);
        tokeniser.error(TokeniserState.Comment);
        assertTrue(errors.size() > 0);
        error = errors.get(0);
        assertEquals("Unexpected character '->' in input state [COMMENT]", error.getMessage());
        assertEquals(error.getLine(), 0);
        assertEquals(error.getColumn(), 0);
    }

    @Test
    public void testEOFErrorHandling() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public int pos() {
                return 0;
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.StartTagBegin);
        tokeniser.emit("<html");
        tokeniser.eofError(TokeniserState.StartTagBegin);
        assertTrue(errors.size() > 0);
        Token error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [START_TAG_BEGIN]", error.getMessage());
        assertEquals(error.getLine(), 0);
        assertEquals(error.getColumn(), 0);
    }
}