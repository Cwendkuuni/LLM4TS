package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead_EmptyInput_ShouldReturnNull() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testRead_CharacterTokenInBuffer_ShouldReturnCharacterToken() {
        tokeniser.emit('a');
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("a", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_NonCharacterTokenInBuffer_ShouldReturnTokenAndClearBuffer() {
        tokeniser.emit("<tag>");
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals("tag", ((Token.StartTag) token).tagName());
        assertEquals(">", ((Token.StartTag) token).toString());

        token = tokeniser.read();
        assertNull(token);

        tokeniser.emit("</tag>");
        token = tokeniser.read();
        assertTrue(token instanceof Token.EndTag);
        assertEquals("tag", ((Token.EndTag) token).tagName());
        assertEquals(">", ((Token.EndTag) token).toString());
    }

    @Test
    public void testEmitTagPending_StartTagWithSelfClosingFlag_ShouldSetSelfClosingFlag() {
        tokeniser.createTagPending(true);
        Token.Tag tag = tokeniser.tagPending;

        tag.selfClosing = false;
        tokeniser.emitTagPending();
        assertFalse(tag.selfClosing);

        tag.selfClosing = true;
        tokeniser.emitTagPending();
        assertTrue(tag.selfClosing);
    }

    @Test
    public void testEmitTagPending_EndTagWithAttributes_ShouldReturnError() {
        tokeniser.createTagPending(false);
        tokeniser.tagPending.attributes.put("attr", "value");

        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;

        tokeniser.emitTagPending();
        assertEquals(1, errors.size());
        assertEquals("Attributes incorrectly present on end tag", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCreateCommentPending_ShouldCreateCommentToken() {
        tokeniser.createCommentPending();
        Token token = tokeniser.commentPending;

        assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending_ShouldEmitCommentToken() {
        tokeniser.createCommentPending();
        Token.Comment comment = tokeniser.commentPending;
        comment.data = " Comment Test ";

        tokeniser.emitCommentPending();
        Token.TokenType type = tokeniser.emitPending.type;

        assertTrue(type == Token.TokenType.Comment);
        assertEquals(" Comment Test ", ((Token.Comment) tokeniser.emitPending).getData());
    }

    @Test
    public void testCreateDoctypePending_ShouldCreateDoctypeToken() {
        tokeniser.createDoctypePending();
        Token token = tokeniser.doctypePending;

        assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending_ShouldEmitDoctypeToken() {
        tokeniser.createDoctypePending();
        Token.Doctype doctype = tokeniser.doctypePending;
        doctype.value = "html";

        tokeniser.emitDoctypePending();
        Token.TokenType type = tokeniser.emitPending.type;

        assertTrue(type == Token.TokenType.Doctype);
        assertEquals("html", ((Token.Doctype) tokeniser.emitPending).getValue());
    }

    @Test
    public void testCreateTempBuffer_ShouldCreateDataBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken_TagPendingAndLastStartTagHaveSameTagName_ShouldReturnTrue() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag";

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_TagPendingAndLastStartTagHaveDifferentTagNames_ShouldReturnFalse() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag1";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag2";

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName_ShouldReturnTagNameOfLastStartTag() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag";

        assertEquals("tag", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError_AddError_ShouldAddErrorToErrorList() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;

        tokeniser.error("Error message");

        assertEquals(1, errors.size());
        assertEquals("Error message", errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError_ShouldAddEofErrorToErrorList() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;

        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError_ShouldAddCharacterReferenceErrorToErrorList() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;

        tokeniser.characterReferenceError("Invalid character reference");

        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: Invalid character reference", errors.get(0).getErrorMessage());
    }

    @Test
    public void testNodeInHtmlNS_ShouldReturnTrue() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}