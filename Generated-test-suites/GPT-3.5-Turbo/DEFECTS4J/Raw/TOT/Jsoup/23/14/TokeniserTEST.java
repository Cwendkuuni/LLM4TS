package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null, null);
    }

    @Test
    public void testRead_noError_returnToken() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testRead_emitCharacterToken_returnCharacterToken() {
        tokeniser.emit("Test");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Test", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_emitTagToken_returnTagToken() {
        Token.Tag tag = new Token.StartTag();
        tokeniser.emit(tag);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals(tag, token);
    }

    @Test
    public void testEmit_emitPendingSet_emitCharacterTokenOnNextRead() {
        tokeniser.emit("Test");
        tokeniser.emit("Case");
        Token token1 = tokeniser.read();
        Token token2 = tokeniser.read();
        assertEquals(Token.TokenType.Character, token1.type);
        assertEquals("Test", ((Token.Character) token1).getData());
        assertEquals(Token.TokenType.Character, token2.type);
        assertEquals("Case", ((Token.Character) token2).getData());
    }

    @Test(expected = IllegalStateException.class)
    public void testEmit_emitPendingSet_throwIllegalStateException() {
        tokeniser.emit("Test");
        tokeniser.emit("Case");
        tokeniser.emit("Error");
    }

    @Test
    public void testEmit_stringValue_emitStringToken() {
        tokeniser.emit("Test");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_characterValue_emitCharToken() {
        tokeniser.emit('T');
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("T", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_tagToken_emitStartTagToken() {
        Token.Tag tag = new Token.StartTag();
        tag.name = "test";
        tokeniser.emit(tag);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals(tag, token);
    }

    @Test
    public void testEmit_startTagNameTag_emitEndTagToken() {
        Token.Tag tag = new Token.StartTag();
        tag.name = "test";
        tokeniser.lastStartTag = tag;
        tokeniser.emit(new Token.EndTag());
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals(tag.name, ((Token.EndTag) token).name);
    }

    @Test
    public void testTransition_changeState_changeStateToGivenState() {
        TokeniserState state = TokeniserState.Comment;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_changeState_advanceReaderAndChangeState() {
        tokeniser.reader = new CharacterReader("Test");
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_setFlag_true() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_noAdditionalAllowedCharacter_returnCharacter() {
        tokeniser.reader = new CharacterReader("&#65;");
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Character.valueOf('A'), character);
    }

    @Test
    public void testConsumeCharacterReference_withAdditionalAllowedCharacter_returnCharacter() {
        tokeniser.reader = new CharacterReader("T&#65;");
        Character character = tokeniser.consumeCharacterReference('T', false);
        assertEquals(Character.valueOf('A'), character);
    }

    @Test
    public void testConsumeCharacterReference_emptyReader_returnNull() {
        tokeniser.reader = new CharacterReader("");
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_additionalAllowedCharacterPresent_returnNull() {
        tokeniser.reader = new CharacterReader("T");
        Character character = tokeniser.consumeCharacterReference('T', false);
        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_matchesAny_returnNull() {
        tokeniser.reader = new CharacterReader("&Test");
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending_startTrue_returnStartTag() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_startFalse_returnEndTag() {
        Token.Tag tag = tokeniser.createTagPending(false);
        assertNotNull(tag);
        assertTrue(tag instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_emitPendingTagFinalizedToken() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
    }

    @Test
    public void testCreateCommentPending_noError_returnCommentToken() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending_noError_returnCommentToken() {
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
    }

    @Test
    public void testCreateDoctypePending_noError_returnDoctypeToken() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending_noError_returnDoctypeToken() {
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testCreateTempBuffer_noError_returnStringBuilder() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
        assertEquals(0, tokeniser.dataBuffer.length());
    }

    @Test
    public void testIsAppropriateEndTagToken_tagNamesMatch_returnTrue() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.tagPending.tagName = "test";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName_returnLastStartTagTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";
        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError_validState_addParseErrorToErrorList() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;
        tokeniser.reader = new CharacterReader("");
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
    }

    @Test
    public void testEofError_validState_addParseErrorToErrorList() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;
        tokeniser.reader = new CharacterReader("");
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
    }

    @Test
    public void testCharacterReferenceError_validMessage_addParseErrorToErrorList() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;
        tokeniser.reader = new CharacterReader("");
        tokeniser.characterReferenceError("Invalid character reference");
        assertEquals(1, errors.size());
    }

    @Test
    public void testError_errorMsg_addParseErrorToErrorList() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;
        tokeniser.reader = new CharacterReader("");
        String errorMsg = "Error occurred";
        tokeniser.error(errorMsg);
        assertEquals(1, errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS_noImplementedNamespaces_returnTrue() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(currentNodeInHtmlNS);
    }
}