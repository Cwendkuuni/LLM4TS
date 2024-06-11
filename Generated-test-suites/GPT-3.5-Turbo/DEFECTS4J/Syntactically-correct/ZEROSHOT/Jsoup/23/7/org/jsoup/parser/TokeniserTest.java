package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader();
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
        Token token = new Token();
        tokeniser.emit(token);
        assertFalse(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(Character.toString(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character character = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tokeniser.tagPending);
        assertEquals(start, tag.start);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertNotNull(tokeniser.tagPending);
        assertEquals(Token.TokenType.StartTag, tokeniser.tagPending.type);
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
        assertNotNull(tokeniser.commentPending);
        assertEquals(Token.TokenType.Comment, tokeniser.commentPending.type);
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
        assertNotNull(tokeniser.doctypePending);
        assertEquals(Token.TokenType.Doctype, tokeniser.doctypePending.type);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        boolean appropriate = tokeniser.isAppropriateEndTagToken();
        assertFalse(appropriate);
    }

    @Test
    public void testAppropriateEndTagName() {
        String tagName = "test";
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = tagName;
        String endTagName = tokeniser.appropriateEndTagName();
        assertEquals(tagName, endTagName);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.DoctypeName;
        tokeniser.error(state);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Comment;
        tokeniser.eofError(state);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testErrorString() {
        String errorMsg = "Error Message";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(inHtmlNS);
    }
}