package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token.Character characterToken = new Token.Character("test");
        tokeniser.emit(characterToken);
        assertEquals(characterToken, tokeniser.emitPending);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tagPending = tokeniser.createTagPending(start);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertEquals("", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag("div");
        tokeniser.lastStartTag = new Token.StartTag("div");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag("div");
        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        ParseErrorList errorList = new ParseErrorList();
        CharacterReader reader = new CharacterReader("[");
        Tokeniser tokeniser = new Tokeniser(reader, errorList);
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errorList.size());
    }

    @Test
    public void testEofError() {
        ParseErrorList errorList = new ParseErrorList();
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader, errorList);
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errorList.size());
    }

    @Test
    public void testCharacterReferenceError() {
        ParseErrorList errorList = new ParseErrorList();
        CharacterReader reader = new CharacterReader("#;");
        Tokeniser tokeniser = new Tokeniser(reader, errorList);
        tokeniser.characterReferenceError("numeric reference with no numerals");
        assertEquals(1, errorList.size());
    }

    @Test
    public void testErrorWithMessage() {
        ParseErrorList errorList = new ParseErrorList();
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader, errorList);
        tokeniser.error("Invalid character reference");
        assertEquals(1, errorList.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertTrue(result);
    }
}