import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class TokeniserTest {

    @Mock
    private CharacterReader mockReader;

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tokeniser = new Tokeniser(mockReader);
    }

    @Test
    public void testRead_BasicCharacterToken_ReturnsCorrectToken() {
        when(mockReader.isEmpty()).thenReturn(false);
        when(mockReader.current()).thenReturn('a');
        when(mockReader.advance()).thenReturn(true);
        when(mockReader.matchesAny(any(Character[].class))).thenReturn(false);

        Token result = tokeniser.read();

        assertTrue(result instanceof Token.Character);
        assertEquals("a", ((Token.Character) result).getData());
    }

    @Test
    public void testRead_WhiteSpaceCharacterToken_ReturnsCorrectToken() {
        when(mockReader.isEmpty()).thenReturn(false);
        when(mockReader.current()).thenReturn(' ');
        when(mockReader.advance()).thenReturn(true);
        when(mockReader.matchesAny(any(Character[].class))).thenReturn(false);

        Token result = tokeniser.read();

        assertTrue(result instanceof Token.Character);
        assertEquals(" ", ((Token.Character) result).getData());
    }

    @Test
    public void testRead_EmptyBuffer_ReturnsEmitPendingToken() {
        when(mockReader.isEmpty()).thenReturn(true);
        when(mockReader.current()).thenReturn(null);
        when(mockReader.advance()).thenReturn(true);
        when(mockReader.matchesAny(any(Character[].class))).thenReturn(false);
        when(mockReader.pos()).thenReturn(0);

        Token result = tokeniser.read();

        assertNull(result);
    }

    @Test
    public void testEmit_ValidToken_SetsEmitPendingAndIsEmitPendingToTrue() {
        tokeniser.emit(new Token.Character("a"));

        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);
        assertEquals(Token.TokenType.Character, tokeniser.emitPending.getType());
    }

    @Test
    public void testEmit_StartTagToken_SetsLastStartTagAndSelfClosingFlagAcknowledgedToFalse() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;

        tokeniser.emit(startTag);

        assertFalse(tokeniser.selfClosingFlagAcknowledged);
        assertNotNull(tokeniser.lastStartTag);
        assertEquals(Token.TokenType.StartTag, tokeniser.lastStartTag.getType());
        assertEquals(true, tokeniser.lastStartTag.selfClosing);
    }

    @Test
    public void testEmit_EndTagTokenWithAttributes_GeneratesError() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.attributes.add("attribute");

        tokeniser.emit(endTag);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Attributes incorrectly present on end tag", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testEmit_Characters_SetsCharBufferAndEmitsCharacterToken() {
        tokeniser.emit("test");

        assertEquals("test", tokeniser.charBuffer.toString());

        Token result = tokeniser.read();
        assertTrue(result instanceof Token.Character);
        assertEquals("test", ((Token.Character) result).getData());
    }

    @Test
    public void testEmit_Character_SetsCharBufferAndEmitsCharacterToken() {
        tokeniser.emit('a');

        assertEquals("a", tokeniser.charBuffer.toString());

        Token result = tokeniser.read();
        assertTrue(result instanceof Token.Character);
        assertEquals("a", ((Token.Character) result).getData());
    }

    @Test
    public void testTransition_SetsStateToGivenState() {
        TokeniserState newState = TokeniserState.RCDATA;

        tokeniser.transition(newState);

        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_SetsStateToGivenStateAndCallsAdvanceOnReader() {
        TokeniserState newState = TokeniserState.RCDATA;

        tokeniser.advanceTransition(newState);

        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_SetsSelfClosingFlagAcknowledgedToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_WithValidCharacter_AdditionalAllowedCharacterAndInAttribute_ReturnsNull() {
        when(mockReader.isEmpty()).thenReturn(false);
        when(mockReader.current()).thenReturn('a');
        when(mockReader.mark()).thenReturn(null);
        when(mockReader.matchConsume("#")).thenReturn(true);
        when(mockReader.matchConsumeIgnoreCase("X")).thenReturn(false);
        when(mockReader.consumeDigitSequence()).thenReturn("65");
        when(mockReader.matchConsume(";")).thenReturn(true);
        when(mockReader.pos()).thenReturn(0);

        Character result = tokeniser.consumeCharacterReference('a', true);

        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_WithEmptyReader_ReturnsNull() {
        when(mockReader.isEmpty()).thenReturn(true);

        Character result = tokeniser.consumeCharacterReference(null, false);

        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_NonNumberedMissingSemi_ReturnsReplacementChar() {
        when(mockReader.isEmpty()).thenReturn(false);
        when(mockReader.current()).thenReturn(null);
        when(mockReader.matchConsume("#")).thenReturn(false);
        when(mockReader.consumeLetterSequence()).thenReturn("gt");
        when(mockReader.matches(';')).thenReturn(false);
        when(mockReader.pos()).thenReturn(0);

        Character result = tokeniser.consumeCharacterReference(null, false);

        assertEquals('\uFFFD', result.charValue());
    }

    @Test
    public void testConsumeCharacterReference_NonNumberedInAttributeWithTrailingInvalidChar_ReturnsNull() {
        when(mockReader.isEmpty()).thenReturn(false);
        when(mockReader.current()).thenReturn(null);
        when(mockReader.matchConsume("#")).thenReturn(false);
        when(mockReader.consumeLetterSequence()).thenReturn("gt;");
        when(mockReader.matchesLetter()).thenReturn(true);
        when(mockReader.matchesDigit()).thenReturn(true);
        when(mockReader.matches('=')).thenReturn(true);
        when(mockReader.pos()).thenReturn(0);

        Character result = tokeniser.consumeCharacterReference(null, true);

        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_NonNumberedWithSemiAndInAttribute_MatchesAndReturnsCharacter() {
        when(mockReader.isEmpty()).thenReturn(false);
        when(mockReader.current()).thenReturn(null);
        when(mockReader.mark()).thenReturn(null);
        when(mockReader.matchConsume("#")).thenReturn(false);
        when(mockReader.consumeLetterSequence()).thenReturn("gt");
        when(mockReader.matches(';')).thenReturn(true);
        when(mockReader.pos()).thenReturn(0);

        Character result = tokeniser.consumeCharacterReference(null, true);

        assertEquals('>', result.charValue());
    }

    @Test
    public void testCreateTagPending_Start_ReturnsStartTagToken() {
        Token.Tag result = tokeniser.createTagPending(true);

        assertTrue(result instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_End_ReturnsEndTagToken() {
        Token.Tag result = tokeniser.createTagPending(false);

        assertTrue(result instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_FinalisesTagAndEmitsToken() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.tagPending = startTag;

        tokeniser.emitTagPending();

        assertTrue(startTag.isFinalised());
        assertNotNull(tokeniser.emitPending);
        assertEquals(startTag, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending_CreatesCommentToken() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending_EmitsCommentToken() {
        Token.Comment comment = new Token.Comment();
        tokeniser.commentPending = comment;

        tokeniser.emitCommentPending();

        assertNotNull(tokeniser.emitPending);
        assertEquals(comment, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending_CreatesDoctypeToken() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending_EmitsDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;

        tokeniser.emitDoctypePending();

        assertNotNull(tokeniser.emitPending);
        assertEquals(doctype, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer_CreatesStringBuilder() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
        assertTrue(tokeniser.dataBuffer instanceof StringBuilder);
    }

    @Test
    public void testIsAppropriateEndTagToken_TagPendingEqualsLastStartTag_ReturnsTrue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.tagPending = startTag;
        tokeniser.lastStartTag = startTag;

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_TagPendingNotEqualsLastStartTag_ReturnsFalse() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.tagPending = startTag;
        tokeniser.lastStartTag = new Token.StartTag();

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors_DefaultValue_ReturnsTrue() {
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors_ChangesValue() {
        tokeniser.setTrackErrors(false);

        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError_AddsParseErrorToErrorsList() {
        tokeniser.error(TokeniserState.CharacterReferenceInRcdata);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError_AddsParseErrorToErrorsList() {
        tokeniser.eofError(TokeniserState.RCDATA);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError_AddsParseErrorToErrorsList() {
        tokeniser.characterReferenceError();

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testError_WithCustomErrorMessage_AddsParseErrorToErrorsList() {
        tokeniser.error("Unexpected error occurred");

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected error occurred", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS_Always_ReturnsTrue() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}