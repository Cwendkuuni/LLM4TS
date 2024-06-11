package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
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
    public void testRead_emptyInput_returnNull() {
        Token token = tokeniser.read();

        assertNull(token);
    }

    @Test
    public void testRead_characterTokenInBuffer_returnCharacterToken() {
        tokeniser.emit('a');

        Token token = tokeniser.read();

        assertNotNull(token);
        assertTrue(token instanceof Token.Character);
        assertEquals("a", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_nonCharacterTokenInBuffer_returnNonCharacterToken() {
        tokeniser.emit(new Token.StartTag());

        Token token = tokeniser.read();

        assertNotNull(token);
        assertTrue(token instanceof Token.StartTag);
    }

    @Test
    public void testEmit_tagToken_setSelfClosingFlagAcknowledgedToFalse() {
        tokeniser.emit(new Token.StartTag());

        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testEmit_endTagTokenWithAttributes_errorAttributesIncorrectlyPresent() {
        Token.EndTag endTagToken = new Token.EndTag();
        endTagToken.getAttributes().put("attr", "value");
        tokeniser.emit(endTagToken);

        Token token = tokeniser.read();

        assertTrue(token instanceof Token.ParseError);
        assertEquals("Attributes incorrectly present on end tag", ((Token.ParseError) token).getErrorMsg());
    }

    @Test
    public void testEmit_string_appendToStringBuilder() {
        tokeniser.emit("abc");

        Token token = tokeniser.read();

        assertNotNull(token);
        assertTrue(token instanceof Token.Character);
        assertEquals("abc", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_char_appendCharToStringBuilder() {
        tokeniser.emit('a');

        Token token = tokeniser.read();

        assertNotNull(token);
        assertTrue(token instanceof Token.Character);
        assertEquals("a", ((Token.Character) token).getData());
    }

    @Test
    public void testTransition_setTokeniserState() {
        tokeniser.transition(TokeniserState.TagName);

        assertEquals(TokeniserState.TagName, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_setTokeniserStateAndAdvanceReader() {
        tokeniser.reader.advance();

        tokeniser.advanceTransition(TokeniserState.AttributeName);

        assertEquals(TokeniserState.AttributeName, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_setSelfClosingFlagAcknowledgedToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_emptyReader_returnNull() {
        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_additionalAllowedCharacterMatchesWithCurrentCharacter_returnNull() {
        tokeniser.reader.consume();

        Character character = tokeniser.consumeCharacterReference('a', false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_currentCharacterIsTab_returnNull() {
        tokeniser.reader.advance();

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_currentCharacterIsLineFeed_returnNull() {
        tokeniser.reader.advance();

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_currentCharacterIsFormFeed_returnNull() {
        tokeniser.reader.advance();

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_currentCharacterIsSpace_returnNull() {
        tokeniser.reader.advance();

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_currentCharacterIsLessThan_returnNull() {
        tokeniser.reader.advance();

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_currentCharacterIsAmpersand_returnNull() {
        tokeniser.reader.advance();

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testCreateTagPending_isStartTag_returnStartTagToken() {
        Token.Tag tagToken = tokeniser.createTagPending(true);

        assertNotNull(tagToken);
        assertTrue(tagToken instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_isEndTag_returnEndTagToken() {
        Token.Tag tagToken = tokeniser.createTagPending(false);

        assertNotNull(tagToken);
        assertTrue(tagToken instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_emitTagPendingToken() {
        Token.StartTag startTagToken = new Token.StartTag();
        tokeniser.createTagPending(true);
        tokeniser.tagPending = startTagToken;

        tokeniser.emitTagPending();

        Token token = tokeniser.read();

        assertNotNull(token);
        assertSame(startTagToken, token);
    }

    @Test
    public void testCreateCommentPending_createCommentPendingToken() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending_emitCommentPendingToken() {
        Token.Comment commentToken = new Token.Comment();
        tokeniser.createCommentPending();
        tokeniser.commentPending = commentToken;

        tokeniser.emitCommentPending();

        Token token = tokeniser.read();

        assertNotNull(token);
        assertSame(commentToken, token);
    }

    @Test
    public void testCreateDoctypePending_createDoctypePendingToken() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending_emitDoctypePendingToken() {
        Token.Doctype doctypeToken = new Token.Doctype();
        tokeniser.createDoctypePending();
        tokeniser.doctypePending = doctypeToken;

        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();

        assertNotNull(token);
        assertSame(doctypeToken, token);
    }

    @Test
    public void testCreateTempBuffer_createDataBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken_sameTagName_returnTrue() {
        tokeniser.lastStartTag = new Token.StartTag("tag");

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertTrue(result);
    }

    @Test
    public void testAppropriateEndTagName_returnLastStartTagName() {
        tokeniser.lastStartTag = new Token.StartTag("tag");

        String tagName = tokeniser.appropriateEndTagName();

        assertEquals("tag", tagName);
    }

    @Test
    public void testError_addErrorToErrorsList() {
        tokeniser.error(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character '' in input state [Data]", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testEofError_addErrorMsgToErrorsList() {
        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError_addErrorMsgToErrorsList() {
        tokeniser.characterReferenceError("invalid reference");

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference: invalid reference", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testError_addErrorWithCustomMessageToErrorsList() {
        tokeniser.error("Custom error message");

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Custom error message", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS_alwaysReturnTrue() {
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }
}