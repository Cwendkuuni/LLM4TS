package org.junit.Test;

import static org.junit.Assert.*;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;

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
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        tokeniser.emit(Token.StartTag());
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, emittedToken.type);
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("Test");
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals("Test", ((Token.Character) emittedToken).data);
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('T');
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals("T", ((Token.Character) emittedToken).data);
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.Comment;
        tokeniser.transition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.Comment;
        tokeniser.advanceTransition(newState);
        assertEquals(newState, tokeniser.getState());
        assertTrue(tokeniser.reader.pos() == 1);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = new Character('a');
        boolean inAttribute = false;
        Character character = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, emittedToken.type);
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
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.Comment, emittedToken.type);
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
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, emittedToken.type);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = new Token.StartTag("test", new Attributes());
        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.tagPending = new Token.EndTag("test");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag("test", new Attributes());
        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("message");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testErrorWithMsg() {
        tokeniser.error("error message");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}