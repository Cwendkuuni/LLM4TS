package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null);
    }

    @After
    public void tearDown() {
        tokeniser = null;
    }

    @Test
    public void testRead() {
        // Test read method
        Token actual = tokeniser.read();
        assertNull(actual);
    }

    @Test
    public void testEmit() {
        // Test emit method
        Token token = new Token.Character("Test");
        tokeniser.emit(token);
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitString() {
        // Test emit method with string parameter
        String str = "Test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        // Test emit method with char parameter
        char c = 'T';
        tokeniser.emit(c);
        assertEquals(c, tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testGetState() {
        // Test getState method
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        // Test transition method
        TokeniserState newState = TokeniserState.CharacterReferenceInData;
        tokeniser.transition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        // Test advanceTransition method
        TokeniserState newState = TokeniserState.CharacterReferenceInData;
        tokeniser.advanceTransition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test acknowledgeSelfClosingFlag method
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test consumeCharacterReference method
        Character additionalAllowedCharacter = 'A';
        boolean inAttribute = true;
        Character actual = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(actual);
    }

    @Test
    public void testCreateTagPending() {
        // Test createTagPending method
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tokeniser.tagPending);
        assertEquals(tag, tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending() {
        // Test emitTagPending method
        tokeniser.emitTagPending();
        assertNotNull(tokeniser.tagPending);
        assertEquals(Token.TokenType.StartTag, tokeniser.tagPending.type);
    }

    @Test
    public void testCreateCommentPending() {
        // Test createCommentPending method
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        // Test emitCommentPending method
        tokeniser.emitCommentPending();
        assertNotNull(tokeniser.commentPending);
        assertEquals(Token.TokenType.Comment, tokeniser.commentPending.type);
    }

    @Test
    public void testCreateDoctypePending() {
        // Test createDoctypePending method
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        // Test emitDoctypePending method
        tokeniser.emitDoctypePending();
        assertNotNull(tokeniser.doctypePending);
        assertEquals(Token.TokenType.Doctype, tokeniser.doctypePending.type);
    }

    @Test
    public void testCreateTempBuffer() {
        // Test createTempBuffer method
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test isAppropriateEndTagToken method
        boolean expected = false;
        boolean actual = tokeniser.isAppropriateEndTagToken();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsTrackErrors() {
        // Test isTrackErrors method
        boolean expected = true;
        boolean actual = tokeniser.isTrackErrors();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTrackErrors() {
        // Test setTrackErrors method
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        // Test error method
        TokeniserState state = TokeniserState.CharacterReferenceInData;
        tokeniser.error(state);
        if (tokeniser.isTrackErrors()) {
            assertEquals(1, tokeniser.errors.size());
            assertEquals("Unexpected character in input", tokeniser.errors.get(0).errorMsg);
            assertEquals(state, tokeniser.errors.get(0).state);
        }
    }

    @Test
    public void testEofError() {
        // Test eofError method
        TokeniserState state = TokeniserState.CharacterReferenceInData;
        tokeniser.eofError(state);
        if (tokeniser.isTrackErrors()) {
            assertEquals(1, tokeniser.errors.size());
            assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).errorMsg);
            assertEquals(state, tokeniser.errors.get(0).state);
        }
    }

    @Test
    public void testCharacterReferenceError() {
        // Test characterReferenceError method
        tokeniser.characterReferenceError();
        if (tokeniser.isTrackErrors()) {
            assertEquals(1, tokeniser.errors.size());
            assertEquals("Invalid character reference", tokeniser.errors.get(0).errorMsg);
        }
    }

    @Test
    public void testErrorWithMessage() {
        // Test error method with errorMsg parameter
        String errorMsg = "Test error";
        tokeniser.error(errorMsg);
        if (tokeniser.isTrackErrors()) {
            assertEquals(1, tokeniser.errors.size());
            assertEquals(errorMsg, tokeniser.errors.get(0).errorMsg);
        }
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test currentNodeInHtmlNS method
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}