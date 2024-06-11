package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);

        assertTrue(tokeniser.isEmitPending);
        assertEquals(startTag, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit("test");

        assertTrue(tokeniser.isEmitPending);
        assertEquals("test", tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit('t');

        assertTrue(tokeniser.isEmitPending);
        assertEquals('t', tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testTransition() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        TokeniserState newState = TokeniserState.BeforeAttributeName;
        tokeniser.transition(newState);

        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        TokeniserState newState = TokeniserState.BeforeAttributeName;
        tokeniser.advanceTransition(newState);

        assertEquals(newState, tokeniser.getState());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character additionalAllowedCharacter = 't';
        boolean inAttribute = true;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);

        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        boolean start = true;
        tokeniser.createTagPending(start);

        assertNotNull(tokeniser.tagPending);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.tagPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";

        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testEofError() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void testErrorWithStringMessage() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        String errorMsg = "Error message";
        tokeniser.error(errorMsg);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }
}