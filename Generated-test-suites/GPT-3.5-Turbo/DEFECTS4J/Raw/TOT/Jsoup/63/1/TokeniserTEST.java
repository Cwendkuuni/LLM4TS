package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        reader = new CharacterReader("Test");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Test", token.toString());
    }

    @Test
    public void testEmit() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertTrue(token instanceof Token.StartTag);
        assertEquals(startTag, token);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.transition(state);

        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);

        assertEquals(state, tokeniser.getState());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeCharacterReference() {
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        
        assertNull(codepoints);
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);

        assertNotNull(tokeniser.tagPending);
        assertNotNull(tag);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
        assertEquals(Token.TokenType.StartTag, tokeniser.tagPending.type);
        assertEquals(tag, tokeniser.tagPending);
        assertEquals(tag, tokeniser.startPending);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals(tag, token);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
        assertEquals(Token.TokenType.Comment, tokeniser.commentPending.type);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Comment, token.type);
        assertTrue(token instanceof Token.Comment);
        assertEquals(token, tokeniser.commentPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
        assertEquals(Token.TokenType.Doctype, tokeniser.doctypePending.type);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Doctype, token.type);
        assertTrue(token instanceof Token.Doctype);
        assertEquals(token, tokeniser.doctypePending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
        assertTrue(tokeniser.dataBuffer.length() == 0);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "testTag";
        tokeniser.tagPending = new Token.EndTag("testTag");

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "testTag";

        assertEquals("testTag", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);

        assertTrue(errors.size() > 0);
        assertEquals("Unexpected character 'T' in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);

        assertTrue(errors.size() > 0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");

        assertTrue(errors.size() > 0);
        assertEquals("Invalid character reference: Invalid character reference", errors.get(0).getErrorMessage());
    }

    @Test
    public void testErrorWithMessage() {
        tokeniser.error("Error message");

        assertTrue(errors.size() > 0);
        assertEquals("Error message", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = tokeniser.unescapeEntities(false);

        assertEquals("Test", unescapedString);
    }

}