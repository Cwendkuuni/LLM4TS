package org.jsoup.parser;

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
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.emit(doctype);
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);
        Token.Comment comment = new Token.Comment();
        tokeniser.emit(comment);
        Token.Character character = new Token.Character("");
        tokeniser.emit(character);
        Token.EndTag endTag = new Token.EndTag();
        tokeniser.emit(endTag);
        Token.Tag tag = new Token.Tag();
        tokeniser.emit(tag);

        Token emittedToken = tokeniser.read();
        assertSame(doctype, emittedToken);

        emittedToken = tokeniser.read();
        assertSame(startTag, emittedToken);

        emittedToken = tokeniser.read();
        assertSame(comment, emittedToken);

        emittedToken = tokeniser.read();
        assertSame(character, emittedToken);

        emittedToken = tokeniser.read();
        assertSame(endTag, emittedToken);

        emittedToken = tokeniser.read();
        assertSame(tag, emittedToken);
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("test");

        Token emittedToken = tokeniser.read();
        assertEquals("test", ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('t');

        Token emittedToken = tokeniser.read();
        assertEquals("t", ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.CharacterReference);

        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.CharacterReference, state);
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.CharacterReference);

        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.CharacterReference, state);

        CharacterReader reader = tokeniser.reader;
        assertEquals(1, reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);

        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
        assertSame(tagPending, tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Doctype);
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
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = tokeniser.tagPending;

        String tagName = tokeniser.appropriateEndTagName();

        assertEquals(tokeniser.lastStartTag.tagName, tagName);
    }

    @Test
    public void testErrorState() {
        tokeniser.error(TokeniserState.CharacterReference);

        ParseErrorList errors = tokeniser.errors;
        ParseError error = errors.get(0);

        assertEquals("Unexpected character '' in input state [CharacterReference]", error.toString());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.CharacterReference);

        ParseErrorList errors = tokeniser.errors;
        ParseError error = errors.get(0);

        assertEquals("Unexpectedly reached end of file (EOF) in input state [CharacterReference]", error.toString());
    }

    @Test
    public void testCharacterReferenceError() {
        // Cannot test this method directly as it calls external methods and classes
        // May need to test indirectly by checking if an error is added to the errors list
    }

    @Test
    public void testError() {
        tokeniser.error("Error");

        ParseErrorList errors = tokeniser.errors;
        ParseError error = errors.get(0);

        assertEquals("Error", error.toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }
}