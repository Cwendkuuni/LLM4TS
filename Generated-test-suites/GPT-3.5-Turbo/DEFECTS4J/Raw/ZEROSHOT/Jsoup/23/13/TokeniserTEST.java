package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
        assertTrue(tokeniser.getState() == TokeniserState.Data);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token.StartTag startTag = new Token.StartTag("div");
        tokeniser.emit(startTag);
        Token token = tokeniser.read();
        assertTrue(token == startTag);
    }

    @Test
    public void testEmitString() {
        String str = "Hello World";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        Token.Character character = (Token.Character) token;
        assertEquals(str, character.getData());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        Token.Character character = (Token.Character) token;
        assertEquals(String.valueOf(c), character.getData());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.CommentStart);
        assertTrue(tokeniser.getState() == TokeniserState.CommentStart);
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.CommentStart);
        assertTrue(tokeniser.getState() == TokeniserState.CommentStart);
        assertTrue(tokeniser.reader.getCurrentPos() == 1);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.getState() == TokeniserState.Data);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character reference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(reference);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tagPending = tokeniser.createTagPending(start);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Token.StartTag startTag = new Token.StartTag("div");
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertTrue(token == startTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
        Token.Comment comment = (Token.Comment) token;
        assertEquals(tokeniser.commentPending, comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
        Token.Doctype doctype = (Token.Doctype) token;
        assertEquals(tokeniser.doctypePending, doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = tokeniser.tagPending;
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertTrue(result);
    }

    @Test
    public void testAppropriateEndTagName() {
        String tagName = "div";
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = tokeniser.tagPending;
        tokeniser.tagPending.tagName = tagName;
        String result = tokeniser.appropriateEndTagName();
        assertEquals(tagName, result);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertTrue(tokeniser.errors.size() == 1);
        ParseError error = tokeniser.errors.get(0);
        assertEquals(state.toString(), error.getMessage());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertTrue(tokeniser.errors.size() == 1);
        ParseError error = tokeniser.errors.get(0);
        assertEquals(state.toString(), error.getMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        assertTrue(tokeniser.errors.size() == 1);
        ParseError error = tokeniser.errors.get(0);
        assertEquals(String.format("Invalid character reference: %s", message), error.getMessage());
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        assertTrue(tokeniser.errors.size() == 1);
        ParseError error = tokeniser.errors.get(0);
        assertEquals(errorMsg, error.getMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertTrue(result);
    }
}