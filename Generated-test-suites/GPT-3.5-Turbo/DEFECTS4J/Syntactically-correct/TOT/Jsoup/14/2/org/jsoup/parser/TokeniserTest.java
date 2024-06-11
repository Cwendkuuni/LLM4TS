import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader mockReader;

    @Before
    public void setup() {
        mockReader = Mockito.mock(CharacterReader.class);
        tokeniser = new Tokeniser(mockReader);
    }

    @Test
    public void testRead_withEmptyReader_shouldReturnNull() {
        Mockito.when(mockReader.isEmpty()).thenReturn(true);

        assertNull(tokeniser.read());
    }

    @Test
    public void testRead_withNonCharacterToken_shouldReturnCharacterTokenFromBuffer() {
        Mockito.when(mockReader.isEmpty()).thenReturn(false);
        Mockito.when(mockReader.current()).thenReturn('\0');
        tokeniser.emit('a');

        Token result = tokeniser.read();

        assertNotNull(result);
        assertEquals(Token.TokenType.Character, result.type);
        assertEquals("a", ((Token.Character) result).getData());
    }

    @Test
    public void testAdvanceTransition_shouldIncreaseReaderIndexAndSetState() {
        Mockito.when(mockReader.current()).thenReturn('a');
        tokeniser.advanceTransition(TokeniserState.CommentEndBang);
        Mockito.verify(mockReader, Mockito.times(1)).advance();
        assertEquals(TokeniserState.CommentEndBang, tokeniser.getState());
    }

    @Test
    public void testCreateTagPending_withStartTag_shouldCreateStartTagObject() {
        tokeniser.createTagPending(true);

        assertNotNull(tokeniser.tagPending);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_withEndTag_shouldCreateEndTagObject() {
        tokeniser.createTagPending(false);

        assertNotNull(tokeniser.tagPending);
        assertTrue(tokeniser.tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_shouldFinalizeTagAndEmit() {
        Mockito.when(mockReader.current()).thenReturn('a');
        tokeniser.createTagPending(true);

        tokeniser.emitTagPending();

        Mockito.verify(tokeniser.tagPending, Mockito.times(1)).finaliseTag();
        Mockito.verify(tokeniser, Mockito.times(1)).emit(tokeniser.tagPending);
    }

    @Test
    public void testCreateCommentPending_shouldCreateCommentObject() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending_shouldEmitCommentObject() {
        Mockito.when(mockReader.current()).thenReturn('a');
        tokeniser.createCommentPending();

        tokeniser.emitCommentPending();

        Mockito.verify(tokeniser, Mockito.times(1)).emit(tokeniser.commentPending);
    }

    @Test
    public void testCreateDoctypePending_shouldCreateDoctypeObject() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending_shouldEmitDoctypeObject() {
        Mockito.when(mockReader.current()).thenReturn('a');
        tokeniser.createDoctypePending();

        tokeniser.emitDoctypePending();

        Mockito.verify(tokeniser, Mockito.times(1)).emit(tokeniser.doctypePending);
    }

    @Test
    public void testCreateTempBuffer_shouldInitializeDataBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken_withMatchingTagNames_shouldReturnTrue() {
        Mockito.when(mockReader.current()).thenReturn('a');
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag";
        tokeniser.lastStartTag = tokeniser.tagPending;

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_withDifferentTagNames_shouldReturnFalse() {
        Mockito.when(mockReader.current()).thenReturn('a');
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag1";
        tokeniser.lastStartTag = tokeniser.tagPending;

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_withNoPreviousStartTag_shouldReturnFalse() {
        Mockito.when(mockReader.current()).thenReturn('a');

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors_shouldReturnTrueIfTrackErrorsIsTrue() {
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors_shouldSetTrackErrors() {
        tokeniser.setTrackErrors(false);

        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError_shouldAddParseErrorIfTrackErrorsIsTrue() {
        Mockito.when(mockReader.current()).thenReturn('a');
        tokeniser.setTrackErrors(true);

        tokeniser.error(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).getErrorMsg());
        assertEquals('a', tokeniser.errors.get(0).getUnexpectedChar());
        assertEquals(TokeniserState.Data, tokeniser.errors.get(0).getState());
        assertEquals(0, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testEofError_shouldAddParseErrorIfTrackErrorsIsTrue() {
        Mockito.when(mockReader.current()).thenReturn('a');
        tokeniser.setTrackErrors(true);

        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).getErrorMsg());
        assertEquals(TokeniserState.Data, tokeniser.errors.get(0).getState());
        assertEquals(0, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testCharacterReferenceError_shouldAddParseErrorIfTrackErrorsIsTrue() {
        tokeniser.setTrackErrors(true);

        tokeniser.characterReferenceError();

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).getErrorMsg());
        assertEquals(0, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testError_withErrorMessage_shouldAddParseErrorIfTrackErrorsIsTrue() {
        tokeniser.setTrackErrors(true);

        tokeniser.error("Custom error message");

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Custom error message", tokeniser.errors.get(0).getErrorMsg());
        assertEquals(0, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testCurrentNodeInHtmlNS_shouldReturnTrue() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

}