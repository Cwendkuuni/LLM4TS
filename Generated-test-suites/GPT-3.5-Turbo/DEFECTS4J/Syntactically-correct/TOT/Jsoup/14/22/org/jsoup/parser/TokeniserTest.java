package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private CharacterReader reader;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead_EmptyInput_ReturnsNull() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testRead_NonCharacterToken_ReturnsCharacterToken() {
        reader = new CharacterReader("abc");
        tokeniser = new Tokeniser(reader);
        Token expectedToken = new Token.Character("abc");

        Token token = tokeniser.read();

        assertEquals(expectedToken, token);
    }

    @Test
    public void testEmit_ValidToken_TokensAreEqual() {
        Token expectedToken = new Token.StartTag();
        tokeniser.emit(expectedToken);

        Token token = tokeniser.read();

        assertEquals(expectedToken, token);
    }

    @Test
    public void testEmit_InvalidToken_ThrowsException() {
        Token token = new Token.StartTag();

        try {
            tokeniser.emit(token);
            fail("Exception not thrown");
        } catch (AssertionError e) {
            assertEquals("There is an unread token pending!", e.getMessage());
        }
    }

    @Test
    public void testCreateTagPending_StartTag_TagIsStartTag() {
        tokeniser.createTagPending(true);

        Token.Tag tag = tokeniser.tagPending;
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_EndTag_TagIsEndTag() {
        tokeniser.createTagPending(false);

        Token.Tag tag = tokeniser.tagPending;
        assertTrue(tag instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_TagIsFinalizedAndEmitted() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.tagPending = startTag;

        tokeniser.emitTagPending();

        assertEquals(startTag, tokeniser.emitPending);
        assertTrue(startTag.isFinalized());
    }

    @Test
    public void testCreateCommentPending_CommentIsCreated() {
        tokeniser.createCommentPending();

        Token.Comment comment = tokeniser.commentPending;
        assertNotNull(comment);
    }

    @Test
    public void testEmitCommentPending_CommentIsEmitted() {
        Token.Comment comment = new Token.Comment();
        tokeniser.commentPending = comment;

        tokeniser.emitCommentPending();

        assertEquals(comment, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending_DoctypeIsCreated() {
        tokeniser.createDoctypePending();

        Token.Doctype doctype = tokeniser.doctypePending;
        assertNotNull(doctype);
    }

    @Test
    public void testEmitDoctypePending_DoctypeIsEmitted() {
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;

        tokeniser.emitDoctypePending();

        assertEquals(doctype, tokeniser.emitPending);
    }

    @Test
    public void testIsAppropriateEndTagToken_TagNameMatchesLastStartTag_ReturnsTrue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "div";

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertTrue(result);
    }

    @Test
    public void testIsAppropriateEndTagToken_TagNameDoesNotMatchLastStartTag_ReturnsFalse() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "p";

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertFalse(result);
    }

    @Test
    public void testIsTrackErrors_TrackErrorsSet_ReturnsTrue() {
        tokeniser.setTrackErrors(true);

        boolean result = tokeniser.isTrackErrors();

        assertTrue(result);
    }

    @Test
    public void testIsTrackErrors_TrackErrorsNotSet_ReturnsFalse() {
        tokeniser.setTrackErrors(false);

        boolean result = tokeniser.isTrackErrors();

        assertFalse(result);
    }

    @Test
    public void testError_ErrorRaisedWithErrorMsg_ErrorIsAddedToErrorsList() {
        tokeniser.setTrackErrors(true);
        TokeniserState state = TokeniserState.Data;

        tokeniser.error(state);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).getErrorMsg());
        assertEquals(state, tokeniser.errors.get(0).getState());
        assertEquals(0, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testEofError_ErrorRaisedWithErrorMsg_ErrorIsAddedToErrorsList() {
        tokeniser.setTrackErrors(true);
        TokeniserState state = TokeniserState.Data;

        tokeniser.eofError(state);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).getErrorMsg());
        assertEquals(state, tokeniser.errors.get(0).getState());
        assertEquals(0, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testCharacterReferenceError_ErrorRaised_ErrorIsAddedToErrorsList() {
        tokeniser.setTrackErrors(true);

        tokeniser.characterReferenceError();

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).getErrorMsg());
        assertEquals(0, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testError_ErrorRaisedWithErrorMsg_ErrorWithCustomMsgIsAddedToErrorsList() {
        tokeniser.setTrackErrors(true);
        String errorMsg = "Custom error message";

        tokeniser.error(errorMsg);

        assertEquals(1, tokeniser.errors.size());
        assertEquals(errorMsg, tokeniser.errors.get(0).getErrorMsg());
        assertEquals(0, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testCurrentNodeInHtmlNS_NoNamespaceImplementation_CurrentNodeIsInHtmlNamespace() {
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }

    @Test
    public void testRead_ValidInput_ReturnsToken() {
        reader = new CharacterReader("<div>");
        tokeniser = new Tokeniser(reader);
        Token expectedToken = new Token.StartTag();
        expectedToken.tagName = "div";

        Token token = tokeniser.read();

        assertEquals(expectedToken, token);
    }
}