package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);

        tokeniser.emit("test");
        token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag("div");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());

        tokeniser.emit("test");
        token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitCharacter() {
        tokeniser.emit('a');
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("a", ((Token.Character) token).getData());
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);

        tokeniser.createTagPending(false);
        assertTrue(tokeniser.tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "test";
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals("test", ((Token.StartTag) token).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "test";
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
        assertEquals("test", ((Token.Comment) token).getData());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
        assertEquals("html", ((Token.Doctype) token).getName());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = tokeniser.tagPending;

        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = tokeniser.tagPending;

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag("div");
        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpected character '' in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference: test");
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: test", errors.get(0).getErrorMessage());
    }

    @Test
    public void testErrorWithMessage() {
        tokeniser.error("test error");
        assertEquals(1, errors.size());
        assertEquals("test error", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}