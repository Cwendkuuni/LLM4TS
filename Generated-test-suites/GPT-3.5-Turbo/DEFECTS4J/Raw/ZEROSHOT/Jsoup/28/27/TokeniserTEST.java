package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() throws Exception {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);

        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);

        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Character(str), emittedToken);
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);

        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Character(Character.toString(c)), emittedToken);
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.Doctype;
        tokeniser.transition(newState);

        TokeniserState state = tokeniser.getState();
        assertEquals(newState, state);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.EndTag;
        tokeniser.advanceTransition(newState);

        TokeniserState state = tokeniser.getState();
        assertEquals(newState, state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        // No assertion, simply checking that the method does not throw any exception
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&#65;");
        tokeniser = new Tokeniser(reader, new ParseErrorList());

        Character c = tokeniser.consumeCharacterReference(null, true);
        assertEquals('A', (char) c);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        tokeniser.createTagPending(start);

        Token.Tag tagPending = tokeniser.tagPending;
        assertNotNull(tagPending);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertTrue(emittedToken instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        Token.Comment commentPending = tokeniser.commentPending;
        assertNotNull(commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertTrue(emittedToken instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        Token.Doctype doctypePending = tokeniser.doctypePending;
        assertNotNull(doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertTrue(emittedToken instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        StringBuilder dataBuffer = tokeniser.dataBuffer;
        assertNotNull(dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.createTagPending(false);

        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertTrue(isAppropriateEndTagToken);
    }

    @Test
    public void testAppropriateEndTagName() {
        String tagName = "div";
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = tagName;
        tokeniser.lastStartTag = startTag;

        String appropriateEndTagName = tokeniser.appropriateEndTagName();
        assertEquals(tagName, appropriateEndTagName);
    }
}