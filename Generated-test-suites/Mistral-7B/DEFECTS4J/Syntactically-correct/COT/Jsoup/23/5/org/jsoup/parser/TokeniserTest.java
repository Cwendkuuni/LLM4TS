import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TokeniserTest {
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        reader = mock(CharacterReader.class);
        errors = mock(ParseErrorList.class);
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_ReturnsTokenOnCharacterRead() {
        when(reader.current()).thenReturn('a');
        tokeniser.emit('a');
        tokeniser.emit('a');
        tokeniser.emit('a');

        Token token = tokeniser.read();
        assertEquals("Character", token.type.toString());
        assertEquals("aaa", token.data);
    }

    @Test
    public void testRead_ReturnsPendingTokenOnCharacterAfterEmitPending() {
        tokeniser.emitPending = new Token.Character("pend");
        tokeniser.isEmitPending = true;
        when(reader.current()).thenReturn('a');

        Token token = tokeniser.read();
        assertEquals("Character", token.type.toString());
        assertEquals("penda", token.data);
    }

    @Test(expected = IllegalStateException.class)
    public void testRead_ThrowsExceptionOnUnreadPendingToken() {
        tokeniser.emitPending = new Token.Character("pend");
        tokeniser.isEmitPending = true;

        tokeniser.read();

        tokeniser.read(); // should read the pending token

        tokeniser.read(); // should throw exception
    }

    @Test
    public void testRead_ReturnsStateOnEndOfFile() {
        when(reader.isEmpty()).thenReturn(true);

        Token token = tokeniser.read();
        assertEquals(TokeniserState.End, token.state);
    }

    @Test
    public void testEmit_EmitsToken() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmit_SetsEmitPendingToNull() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testEmit_SetsLastStartTag() {
        tokeniser.emitPending = new Token.StartTag();
        tokeniser.emit(tokeniser.emitPending);
        Token lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag = lastStartTag;
        assertEquals(lastStartTag, tokeniser.lastStartTag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmit_ThrowsExceptionOnNullToken() {
        tokeniser.emit(null);
    }

    @Test
    public void testEmit_ThrowsExceptionOnStateAfterStartTag() {
        tokeniser.emitPending = new Token.Character("pend");
        tokeniser.isEmitPending = true;
        tokeniser.emit(new Token.StartTag());

        try {
            tokeniser.emit(new Token.Character("char"));
        } catch (IllegalStateException e) {
            assertEquals("There is an unread token pending!", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testEmit_DoesNotResetEmitPendingOrIsEmitPending() {
        tokeniser.emitPending = new Token.Character("pend");
        tokeniser.isEmitPending = true;
        tokeniser.emit(new Token.Character("char"));
        assertEquals(new Token.Character("pend"), tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmit_SetsSelfClosingFlagAcknowledgedToFalse() {
        tokeniser.emitPending = new Token.StartTag();
        tokeniser.emit(tokeniser.emitPending);
        verify(tokeniser, times(1)).acknowledgeSelfClosingFlag();
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testTransition_SetsState() {
        tokeniser.transition(TokeniserState.Whitespace);
        assertEquals(TokeniserState.Whitespace, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_SetsStateAndAdvancesReader() {
        when(reader.current()).thenReturn('a');
        tokeniser.advanceTransition(TokeniserState.Whitespace);
        assertEquals(TokeniserState.Whitespace, tokeniser.getState());
        verify(reader, times(1)).advance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdvanceTransition_ThrowsExceptionOnNullState() {
        tokeniser.advanceTransition(null);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_SetsSelfClosingFlagAcknowledgedToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_ReturnsCharacter() {
        when(reader.consume('#')).thenReturn(true);
        when(reader.consume('X')).thenReturn(true);
        when(reader.consumeHexSequence()).thenReturn("0072");
        when(reader.consume(';')).thenReturn(true);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals((char) 0x52, character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_ReturnsReplacementCharOnInvalidCharacter() {
        when(reader.consume('#')).thenReturn(true);
        when(reader.consume('X')).thenReturn(true);
        when(reader.consumeHexSequence()).thenReturn("0xFFF");
        when(reader.consume(';')).thenReturn(true);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Tokeniser.replacementChar, character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_ReturnsReplacementCharOnCharacterOutsideValidRange() {
        when(reader.consume('#')).thenReturn(true);
        when(reader.consume('X')).thenReturn(true);
        when(reader.consumeHexSequence()).thenReturn("0x110000");
        when(reader.consume(';')).thenReturn(true);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Tokeniser.replacementChar, character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_ReturnsReplacementCharOnInvalidNamedReference() {
        when(reader.consume('#')).thenReturn(true);
        when(reader.consume('X')).thenReturn(false);
        when(reader.consumeLetterSequence()).thenReturn("InvalidEntity");
        when(reader.consume(';')).thenReturn(true);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Tokeniser.replacementChar, character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_ReturnsNamedCharacterOnValidNamedReference() {
        when(reader.consume('#')).thenReturn(true);
        when(reader.consume('X')).thenReturn(true);
        when(reader.consumeLetterSequence()).thenReturn("amp");
        when(reader.consume(';')).thenReturn(true);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', character.charValue());
    }

    @Test
    public void testCreateTagPending_CreatesStartTag() {
        Token tagPending = tokeniser.createTagPending(true);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_CreatesEndTag() {
        Token tagPending = tokeniser.createTagPending(false);
        assertTrue(tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_FinalisesTag() {
        Token tagPending = mock(Token.StartTag.class);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        verify(tagPending).finaliseTag();
    }

    @Test
    public void testCreateCommentPending_CreatesComment() {
        Token commentPending = tokeniser.createCommentPending();
        assertTrue(commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending_EmitsComment() {
        Token commentPending = mock(Token.Comment.class);
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending_CreatesDoctype() {
        Token doctypePending = tokeniser.createDoctypePending();
        assertTrue(doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending_EmitsDoctype() {
        Token doctypePending = mock(Token.Doctype.class);
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer_CreatesTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken_ReturnsTrueOnMatchingTags() {
        tokeniser.lastStartTag = new Token.StartTag("div");
        tokeniser.tagPending = new Token.EndTag("div");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_ReturnsFalseOnMismatchingTags() {
        tokeniser.lastStartTag = new Token.StartTag("div");
        tokeniser.tagPending = new Token.EndTag("p");
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_ReturnsFalseOnNullTags() {
        tokeniser.lastStartTag = null;
        tokeniser.tagPending = new Token.StartTag("div");
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_ReturnsFalseOnNullPendingTag() {
        tokeniser.lastStartTag = new Token.StartTag("div");
        tokeniser.tagPending = null;
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testError_AddsErrorMessage() {
        tokeniser.error(TokeniserState.Data);
        verify(errors, times(1)).add(any(ParseError.class));
    }

    @Test
    public void testEofError_AddsErrorMessage() {
        tokeniser.eofError(TokeniserState.Data);
        verify(errors, times(1)).add(any(ParseError.class));
    }

    @Test
    public void testCharacterReferenceError_AddsErrorMessage() {
        tokeniser.characterReferenceError("Invalid character reference");
        verify(errors, times(1)).add(any(ParseError.class));
    }
}