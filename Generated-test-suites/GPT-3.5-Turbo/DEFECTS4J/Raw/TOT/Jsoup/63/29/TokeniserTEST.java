package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Test case for read() method

        // Typical use case
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"), new ParseErrorList());
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("This is a test", token.toString());

        // Edge case: Empty input
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        token = tokeniser.read();
        assertEquals(null, token);

        // Error scenario: Invalid character in input
        tokeniser = new Tokeniser(new CharacterReader("<This is an invalid character>"), new ParseErrorList());
        token = tokeniser.read();
        assertEquals(Token.TokenType.Error, token.type);
        assertEquals("Invalid character found at position 1", token.toString());
    }

    @Test
    public void testEmit() {
        // Test case for emit() method

        // Typical use case
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token token = new Token.Character().data("This is a character");
        tokeniser.emit(token);
        assertEquals(Token.TokenType.Character, tokeniser.emitPending.type);
        assertEquals("This is a character", tokeniser.emitPending.toString());

        // Edge case: Empty emit()
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.emit("");
        assertEquals(null, tokeniser.charsString);

        // Error scenario: Unread token pending
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        token = new Token.Character().data("This is a character");
        tokeniser.emit(token);
        tokeniser.emit(new Token.Doctype().name("html"));
        assertEquals(true, tokeniser.isEmitPending);
        tokeniser.emit(new Token.Character().data("This is another character"));
        assertEquals(true, tokeniser.errors.hasErrors());
    }

    // Additional test cases for other methods in the Tokeniser class

    @Test
    public void testCreateTagPending() {
        // Test case for createTagPending() method

        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token.Tag startTag = tokeniser.createTagPending(true);
        assertEquals(Token.TokenType.StartTag, startTag.type);

        Token.Tag endTag = tokeniser.createTagPending(false);
        assertEquals(Token.TokenType.EndTag, endTag.type);
    }

    @Test
    public void testEmitTagPending() {
        // Test case for emitTagPending() method

        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.attributes.put("id", "testId");
        tokeniser.emitTagPending();
        assertEquals(Token.TokenType.StartTag, tokeniser.emitPending.type);
        assertEquals("<tag id=\"testId\">", tokeniser.emitPending.toString());
    }

    @Test
    public void testCreateCommentPending() {
        // Test case for createCommentPending() method

        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createCommentPending();
        assertEquals(Token.TokenType.Comment, tokeniser.commentPending.type);
    }

    @Test
    public void testEmitCommentPending() {
        // Test case for emitCommentPending() method

        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createCommentPending();
        tokeniser.commentPending.addData("This is a comment");
        tokeniser.emitCommentPending();
        assertEquals(Token.TokenType.Comment, tokeniser.emitPending.type);
        assertEquals("<!--This is a comment-->", tokeniser.emitPending.toString());
    }

    @Test
    public void testCreateDoctypePending() {
        // Test case for createDoctypePending() method

        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createDoctypePending();
        assertEquals(Token.TokenType.Doctype, tokeniser.doctypePending.type);
    }

    @Test
    public void testEmitDoctypePending() {
        // Test case for emitDoctypePending() method

        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.addData("html");
        tokeniser.emitDoctypePending();
        assertEquals(Token.TokenType.Doctype, tokeniser.emitPending.type);
        assertEquals("<!doctype html>", tokeniser.emitPending.toString());
    }

    @Test
    public void testUnescapeEntities() {
        // Test case for unescapeEntities() method

        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        String unescapedString = tokeniser.unescapeEntities(false);
        assertEquals("", unescapedString);

        tokeniser = new Tokeniser(new CharacterReader("This is a &lt; test"), new ParseErrorList());
        unescapedString = tokeniser.unescapeEntities(false);
        assertEquals("This is a < test", unescapedString);

        tokeniser = new Tokeniser(new CharacterReader("This is a &#60; test"), new ParseErrorList());
        unescapedString = tokeniser.unescapeEntities(false);
        assertEquals("This is a < test", unescapedString);

        tokeniser = new Tokeniser(new CharacterReader("This is an &invalid; entity"), new ParseErrorList());
        unescapedString = tokeniser.unescapeEntities(false);
        assertEquals("This is an &invalid; entity", unescapedString);
    }

}