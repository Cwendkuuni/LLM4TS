package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Tokeniser.ParseError;
import org.jsoup.nodes.TokeniserState;

import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testReadEmptyStream() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public boolean isEmpty() {
                return true;
            }
        };

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadSingleCharacter() {
        CharacterReader reader = new CharacterReader("x");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('x', ((Character) token).text);
    }

    @Test
    public void testReadMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ab");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('a', ((Character) token).text);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('b', ((Character) token).text);
    }

    @Test
    public void testReadSingleWhiteSpace() {
        CharacterReader reader = new CharacterReader("\t");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('\t', ((Character) token).text);
    }

    @Test
    public void testReadSingleLineBreak() {
        CharacterReader reader = new CharacterReader("\n");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('\n', ((Character) token).text);
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src=\"image.jpg\" />");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token1 = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token1.type);
        assertTrue(token1.selfClosing);
        assertEquals("img", ((StartTag) token1).tagName);

        Token token2 = tokeniser.read();
        assertEquals(Token.TokenType.Character, token2.type);
        assertEquals(' ', ((Character) token2).text);

        Token token3 = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token3.type);
        assertEquals("img", ((EndTag) token3).tagName);
    }

    @Test
    public void testReadStartTagWithAttributes() {
        CharacterReader reader = new CharacterReader("<div id=\"test\" class=\"test\"></div>");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token1 = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token1.type);
        assertEquals("div", token1.tagName);
        assertEquals(2, token1.attributes.size());
        assertEquals("test", token1.attributes.get("id"));
        assertEquals("test", token1.attributes.get("class"));

        Token token2 = tokeniser.read();
        assertEquals(Token.TokenType.Character, token2.type);
        assertEquals(' ', ((Character) token2).text);

        Token token3 = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token3.type);
        assertEquals("div", token3.tagName);
    }

    @Test
    public void testReadEndTag() {
        CharacterReader reader = new CharacterReader("<div><p></p></div>");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token1 = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token1.type);
        assertEquals("div", token1.tagName);

        Token token2 = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token2.type);
        assertEquals("p", token2.tagName);
        assertTrue(token2.selfClosing);

        Token token3 = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token3.type);
        assertEquals("p", token3.tagName);

        Token token4 = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token4.type);
        assertEquals("div", token4.tagName);
    }

    @Test
    public void testReadScriptTag() {
        CharacterReader reader = new CharacterReader("<script src=\"script.js\"></script>");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token1 = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token1.type);
        assertEquals("script", token1.tagName);
        assertEquals(1, token1.attributes.size());
        assertEquals("src", token1.attributes.get("src"));
        assertEquals("script.js", token1.attributes.get("src").getValue());

        Token token2 = tokeniser.read();
        assertEquals(Token.TokenType.Character, token2.type);
        assertEquals(' ', ((Character) token2).text);

        Token token3 = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token3.type);
        assertEquals("script", token3.tagName);
    }

    @Test
    public void testReadCommentTag() {
        CharacterReader reader = new CharacterReader("<!-- This is a comment --)");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token1 = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token1.type);
        assertEquals("This is a comment", token1.text);
    }

    @Test
    public void testReadEmptyTag() {
        CharacterReader reader = new CharacterReader("<input/>");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token1 = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token1.type);
        assertEquals("input", token1.tagName);
        assertTrue(token1.selfClosing);

        Token token2 = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token2.type);
        assertEquals("input", token2.tagName);
    }

    @Test
    public void testReadDoctypeDeclaration() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token1 = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token1.type);
        assertEquals("html", token1.doctypeName);
    }

    @Test
    public void testReadErrorHandling() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html><script>alert('Hello World')</script>");

        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.setTrackErrors(true);

        Token token1 = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token1.type);
        assertEquals("html", token1.doctypeName);

        Token token2 = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token2.type);
        assertEquals("script", token2.tagName);

        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).message);
        assertEquals(TokeniserState.AfterScript, errors.get(0).state);
        assertEquals(1, errors.get(0).lineNumber);
    }

    @Test
    public void testReadCharacterReference() {
        CharacterReader reader = new CharacterReader("&#x1F603;");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token1 = tokeniser.read();
        assertEquals(Token.TokenType.Character, token1.type);
        assertEquals('\uD83D\uDE03', ((Character) token1).text.codePointAt(0));
    }

    @Test
    public void testReadCharacterReferenceWithInvalidHexCode() {
        CharacterReader reader = new CharacterReader("&#xINVALID;");

        Tokeniser tokeniser = new Tokeniser(reader);

        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference", errors.get(0).message);
    }

    @Test
    public void testReadCharacterReferenceWithInvalidDecimalCode() {
        CharacterReader reader = new CharacterReader("&#-1;");

        Tokeniser tokeniser = new Tokeniser(reader);

        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference", errors.get(0).message);
    }

    @Test
    public void testReadCharacterReferenceWithInvalidName() {
        CharacterReader reader = new CharacterReader("&#foo;");

        Tokeniser tokeniser = new Tokeniser(reader);

        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference", errors.get(0).message);
    }

    @Test
    public void testReadCharacterReferenceWithMissingSemicolon() {
        CharacterReader reader = new CharacterReader("&#x1F603");

        Tokeniser tokeniser = new Tokeniser(reader);

        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Missing semicolon", errors.get(0).message);
    }

    @Test
    public void testReadCharacterReferenceWithExtraSemicolon() {
        CharacterReader reader = new CharacterReader("&#x1F603;;");

        Tokeniser tokeniser = new Tokeniser(reader);

        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Extra semicolon", errors.get(0).message);
    }

    @Test
    public void testReadCharacterReferenceWithMultipleCharacterReferenceInSequence() {
        CharacterReader reader = new CharacterReader("&#x1F603;&#x1F602;");

        Tokeniser tokeniser = new Tokeniser(reader);

        Token token1 = tokeniser.read();
        assertEquals(Token.TokenType.Character, token1.type);
        assertEquals('\uD83D\uDE03', ((Character) token1).text.codePointAt(0));

        Token token2 = tokeniser.read();
        assertEquals(Token.TokenType.Character, token2.type);
        assertEquals('\uD83D\uDE02', ((Character) token2).text.codePointAt(0));
    }

    @Test
    public void testCreateTagPendingWithStartTag() {
        Tokeniser tokeniser = new Tokeniser(null);
        Tag tagPending = tokeniser.createTagPending(true);
        assertTrue(tagPending instanceof StartTag);
    }

    @Test
    public void testCreateTagPendingWithEndTag() {
        Tokeniser tokeniser = new Tokeniser(null);
        Tag tagPending = tokeniser.createTagPending(false);
        assertTrue(tagPending instanceof EndTag);
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        Tag tagPending = tokeniser.createTagPending(true);

        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertEquals(tagPending.tagName, token.tagName);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null);
        Comment commentPending = tokeniser.createCommentPending();
        assertEquals(Token.TokenType.Comment, commentPending.type);
    }

    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        Comment commentPending = tokeniser.createCommentPending();

        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertEquals(commentPending.text, token.text);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null);
        Doctype doctypePending = tokeniser.createDoctypePending();
        assertEquals(Token.TokenType.Doctype, doctypePending.type);
    }

    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        Doctype doctypePending = tokeniser.createDoctypePending();

        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertEquals(doctypePending.doctypeName, token.doctypeName);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null);
        StringBuilder dataBuffer = tokeniser.createTempBuffer();
        assertNotNull(dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.createTagPending(true);
        tokeniser.transition(TokeniserState.Data);
        tokeniser.createTagPending(true);

        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.transition(TokeniserState.Script);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertFalse(tokeniser.isTrackErrors());

        tokeniser.setTrackErrors(true);
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        CharacterReader reader = new CharacterReader("invalid");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.setTrackErrors(true);

        tokeniser.error(TokeniserState.Data);
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Unexpected character in input", errors.get(0).message);
    }

    @Test
    public void testEofError() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.setTrackErrors(true);

        tokeniser.eofError(TokeniserState.Data);
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).message);
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new CharacterReader("&#xINVALID;");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.setTrackErrors(true);

        tokeniser.characterReferenceError();
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference", errors.get(0).message);
    }
}