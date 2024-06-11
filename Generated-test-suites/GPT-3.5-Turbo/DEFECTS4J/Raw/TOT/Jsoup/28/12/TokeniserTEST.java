package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // Test empty input
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("", ((Token.Character) token).getData());
        assertTrue(errors.size() == 0);

        // Test non empty input
        CharacterReader reader = new CharacterReader("This is a test");
        tokeniser = new Tokeniser(reader, errors);
        token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("This is a test", ((Token.Character) token).getData());
        assertTrue(errors.size() == 0);
    }

    @Test
    public void testEmit() {
        // Test emitting a character token
        tokeniser.emit("abc");
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("abc", ((Token.Character) token).getData());
        assertTrue(errors.size() == 0);

        // Test emitting a start tag
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.tagName = "div";
        tokeniser.emitTagPending();
        token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) token).tagName);
        assertTrue(errors.size() == 0);

        // Test emitting an end tag
        tag = tokeniser.createTagPending(false);
        tag.tagName = "div";
        tokeniser.emitTagPending();
        token = tokeniser.read();
        assertTrue(token instanceof Token.EndTag);
        assertEquals("div", ((Token.EndTag) token).tagName);
        assertTrue(errors.size() == 0);
    }

    @Test
    public void testTransition() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
        tokeniser.transition(TokeniserState.TagName);
        state = tokeniser.getState();
        assertEquals(TokeniserState.TagName, state);
        tokeniser.advanceTransition(TokeniserState.TagName);
        state = tokeniser.getState();
        assertEquals(TokeniserState.TagName, state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test empty input
        Character character = tokeniser.consumeCharacterReference(null, true);
        assertNull(character);

        // Test input with invalid character reference
        CharacterReader reader = new CharacterReader("&invalid;");
        tokeniser = new Tokeniser(reader, errors);
        character = tokeniser.consumeCharacterReference(null, true);
        assertNull(character);
        assertTrue(errors.size() == 1);

        // Test input with valid character reference
        reader = new CharacterReader("&amp;");
        tokeniser = new Tokeniser(reader, errors);
        character = tokeniser.consumeCharacterReference(null, true);
        assertEquals('&', character.charValue());
        assertTrue(errors.size() == 0);
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tag instanceof Token.StartTag);
        tag = tokeniser.createTagPending(false);
        assertTrue(tag instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.tagName = "div";
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) token).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token token = tokeniser.commentPending;
        assertNotNull(token);
        assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token token = tokeniser.doctypePending;
        assertNotNull(token);
        assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        assertFalse(tokeniser.isAppropriateEndTagToken());

        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.tagPending.tagName = "div";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertTrue(errors.size() == 1);
        assertEquals("Unexpected character '' in input state [Data]", errors.get(0).toString());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertTrue(errors.size() == 1);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");
        assertTrue(errors.size() == 1);
        assertEquals("Invalid character reference: Invalid character reference", errors.get(0).toString());
    }

    @Test
    public void testErrorWithMessage() {
        tokeniser.error("Invalid error");
        assertTrue(errors.size() == 1);
        assertEquals("Invalid error", errors.get(0).toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}