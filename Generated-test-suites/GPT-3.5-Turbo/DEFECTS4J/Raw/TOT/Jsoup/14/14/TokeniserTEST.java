package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Test normal case
        CharacterReader reader = new CharacterReader("Some HTML input");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token = tokeniser.read();
        assertEquals("Some HTML input", token.toString());

        // Test empty input
        reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
        token = tokeniser.read();
        assertNull(token);

        // Test null input
        reader = new CharacterReader(null);
        tokeniser = new Tokeniser(reader);
        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        // Test emitting a token
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("HTML tag"));

        Token.Character expectedToken = new Token.Character("HTML tag");
        tokeniser.emit(expectedToken);
        Token emittedToken = tokeniser.read();

        assertEquals(expectedToken, emittedToken);

        // Test emitting a string
        tokeniser = new Tokeniser(new CharacterReader("HTML"));
        String expectedString = "HTML";

        tokeniser.emit(expectedString);
        emittedToken = tokeniser.read();

        assertEquals(expectedString, emittedToken.toString());
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test consuming a character reference - numbered
        CharacterReader reader = new CharacterReader("&#65;");
        Tokeniser tokeniser = new Tokeniser(reader);
        Character expectedChar = 'A';

        Character actualChar = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expectedChar, actualChar);

        // Test consuming a character reference - invalid
        reader = new CharacterReader("&invalid;");
        tokeniser = new Tokeniser(reader);

        actualChar = tokeniser.consumeCharacterReference(null, false);
        assertNull(actualChar);

        // Test consuming a character reference - named
        reader = new CharacterReader("&lt;");
        tokeniser = new Tokeniser(reader);

        expectedChar = '<';
        actualChar = tokeniser.consumeCharacterReference(null, false);

        assertEquals(expectedChar, actualChar);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<div>"));

        Token.Tag expectedTag = new Token.StartTag();
        expectedTag.tagName = "div";

        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token emittedToken = tokeniser.read();
        assertEquals(expectedTag, emittedToken);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<!--This is a comment-->"));

        Token.Comment expectedComment = new Token.Comment();
        expectedComment.data = "This is a comment";

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token emittedToken = tokeniser.read();
        assertEquals(expectedComment, emittedToken);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<!DOCTYPE html>"));

        Token.Doctype expectedDoctype = new Token.Doctype();
        expectedDoctype.name = "html";

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token emittedToken = tokeniser.read();
        assertEquals(expectedDoctype, emittedToken);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<div>"));

        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag("div");

        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "span";
        tokeniser.lastStartTag = new Token.StartTag("div");

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

        assertTrue(tokeniser.isTrackErrors());

        tokeniser.setTrackErrors(false);

        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        tokeniser.error(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).errorMsg);
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).errorMsg);
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("HTML"));

        tokeniser.characterReferenceError();

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).errorMsg);
    }

    @Test
    public void testErrorWithCustomMessage() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("HTML"));

        tokeniser.error("Invalid input");

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid input", tokeniser.errors.get(0).errorMsg);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<div>"));

        assertTrue(tokeniser.currentNodeInHtmlNS());

        // TODO: Implement test for when currentNode is not in the HTML namespace
    }

}