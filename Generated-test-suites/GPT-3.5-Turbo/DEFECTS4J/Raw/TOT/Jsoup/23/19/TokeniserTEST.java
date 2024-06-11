package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("This is a test");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_noSelfClosingFlagAcknowledged_error() {
        tokeniser.emit(new Token.StartTag("div", new ArrayList<>()));
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.selfClosingFlagAcknowledged = false;

        Token result = tokeniser.read();

        assertNull(result);
        assertTrue(tokeniser.errors.hasErrors());
        assertEquals("Self closing flag not acknowledged", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testRead_charBufferNotEmpty_returnCharacterToken() {
        tokeniser.emit("This is a test");

        Token result = tokeniser.read();

        assertNotNull(result);
        assertTrue(result instanceof Token.Character);
        assertEquals("This is a test", ((Token.Character) result).getData());
        assertEquals("", tokeniser.charBuffer.toString());
    }

    @Test
    public void testRead_charBufferEmpty_returnEmitPendingToken() {
        tokeniser.isEmitPending = true;
        tokeniser.emitPending = new Token.StartTag("div", new ArrayList<>());

        Token result = tokeniser.read();

        assertNotNull(result);
        assertTrue(result instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) result).name());
        assertFalse(tokeniser.isEmitPending);
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testEmit_emitStartTag_updateLastStartTagAndSelfClosingFlagAcknowledged() {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("id", "test"));
        attributes.add(new Attribute("class", "testClass"));
        Token.StartTag startTag = new Token.StartTag("div", attributes);

        tokeniser.emit(startTag);

        assertEquals(startTag, tokeniser.lastStartTag);
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testEmit_emitEndTag_checkEndTagAttributes() {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("id", "test"));
        attributes.add(new Attribute("class", "testClass"));
        Token.EndTag endTag = new Token.EndTag("div", attributes);

        tokeniser.emit(endTag);

        assertTrue(tokeniser.errors.hasErrors());
        assertEquals("Attributes incorrectly present on end tag", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testEmit_emitString_appendToCharBuffer() {
        String str = "This is a test";

        tokeniser.emit(str);

        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmit_emitChar_appendToCharBuffer() {
        char c = 'c';

        tokeniser.emit(c);

        assertEquals("c", tokeniser.charBuffer.toString());
    }

    @Test
    public void testTransition_setState() {
        TokeniserState state = TokeniserState.RCData;

        tokeniser.transition(state);

        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_advanceAndSetState() {
        TokeniserState state = TokeniserState.RCData;

        tokeniser.advanceTransition(state);

        assertEquals(state, tokeniser.getState());
        assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_setSelfClosingFlagAcknowledged() {
        tokeniser.selfClosingFlagAcknowledged = false;

        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_emptyReader_returnNull() {
        tokeniser.reader = new CharacterReader("");

        Character result = tokeniser.consumeCharacterReference(null, false);

        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_sameAllowedCharacter_returnNull() {
        tokeniser.reader = new CharacterReader("a");

        Character result = tokeniser.consumeCharacterReference('a', false);

        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_matchesAnyCharacter_returnNull() {
        tokeniser.reader = new CharacterReader("&");

        Character result = tokeniser.consumeCharacterReference(null, false);

        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_numberedReferenceHexMode_missingSemicolon_returnReplacementChar() {
        tokeniser.reader = new CharacterReader("&#x61");

        Character result = tokeniser.consumeCharacterReference(null, false);

        assertEquals('\uFFFD', result);
    }

    @Test
    public void testConsumeCharacterReference_numberedReferenceHexMode_invalidChar_returnReplacementChar() {
        tokeniser.reader = new CharacterReader("&#x110000;");

        Character result = tokeniser.consumeCharacterReference(null, false);

        assertEquals('\uFFFD', result);
    }

    @Test
    public void testConsumeCharacterReference_numberedReferenceDecimalMode_invalidChar_returnReplacementChar() {
        tokeniser.reader = new CharacterReader("&#1;");

        Character result = tokeniser.consumeCharacterReference(null, false);

        assertEquals('\uFFFD', result);
    }

    @Test
    public void testConsumeCharacterReference_namedReference_hasAdditionalInvalidChar_returnNull() {
        tokeniser.reader = new CharacterReader("&test=");

        Character result = tokeniser.consumeCharacterReference(null, true);

        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_namedReference_missingSemicolon_returnNull() {
        tokeniser.reader = new CharacterReader("&");

        Character result = tokeniser.consumeCharacterReference(null, false);

        assertNull(result);
    }

    @Test
    public void testCreateTagPending_createStartTag_returnStartTag() {
        Token.Tag result = tokeniser.createTagPending(true);

        assertNotNull(result);
        assertTrue(result instanceof Token.StartTag);
        assertEquals("div", result.name());
    }

    @Test
    public void testCreateTagPending_createEndTag_returnEndTag() {
        Token.Tag result = tokeniser.createTagPending(false);

        assertNotNull(result);
        assertTrue(result instanceof Token.EndTag);
        assertEquals("div", result.name());
    }

    @Test
    public void testEmitTagPending_emitTagPending() {
        tokeniser.tagPending = new Token.StartTag("div", new ArrayList<>());

        tokeniser.emitTagPending();

        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) tokeniser.emitPending).name());
    }

    @Test
    public void testCreateCommentPending_createCommentPending() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending_emitCommentPending() {
        tokeniser.commentPending = new Token.Comment();

        tokeniser.emitCommentPending();

        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending_createDoctypePending() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending_emitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();

        tokeniser.emitDoctypePending();

        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer_createTempBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken_tagPendingNotEqualToLastStartTag_returnFalse() {
        tokeniser.tagPending = new Token.StartTag("div", new ArrayList<>());
        tokeniser.lastStartTag = new Token.StartTag("span", new ArrayList<>());

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertFalse(result);
    }

    @Test
    public void testIsAppropriateEndTagToken_tagPendingEqualToLastStartTag_returnTrue() {
        tokeniser.tagPending = new Token.StartTag("div", new ArrayList<>());
        tokeniser.lastStartTag = new Token.StartTag("div", new ArrayList<>());

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertTrue(result);
    }

    @Test
    public void testAppropriateEndTagName_returnLastStartTagName() {
        tokeniser.lastStartTag = new Token.StartTag("div", new ArrayList<>());

        String result = tokeniser.appropriateEndTagName();

        assertEquals("div", result);
    }

    @Test
    public void testError_addErrorToErrors() {
        TokeniserState state = TokeniserState.Data;

        tokeniser.error(state);

        assertTrue(tokeniser.errors.canAddError());
        assertEquals("Unexpected character 'T' in input state [Data]", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testEofError_addErrorToErrors() {
        TokeniserState state = TokeniserState.Data;

        tokeniser.eofError(state);

        assertTrue(tokeniser.errors.canAddError());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testCharacterReferenceError_addErrorToErrors() {
        String message = "Invalid character reference: numeric reference with no numerals";

        tokeniser.characterReferenceError(message);

        assertTrue(tokeniser.errors.canAddError());
        assertEquals("Invalid character reference: numeric reference with no numerals", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testError_addErrorMessageToErrors() {
        String errorMsg = "Attributes incorrectly present on end tag";

        tokeniser.error(errorMsg);

        assertTrue(tokeniser.errors.canAddError());
        assertEquals("Attributes incorrectly present on end tag", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS_returnTrue() {
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }
}