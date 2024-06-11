import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TokeniserTest {

    @Mock
    private CharacterReader reader;

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead_EmptyInput_ReturnCharacterToken() {
        when(reader.isEmpty()).thenReturn(true);
        
        Token result = tokeniser.read();
        
        assertTrue(result instanceof Token.Character);
    }

    @Test
    public void testRead_NonEmptyInput_ReturnToken() {
        when(reader.isEmpty()).thenReturn(false);
        
        Token result = tokeniser.read();
        
        assertNotNull(result);
    }

    @Test
    public void testEmit_CharacterToken_ReturnPendingToken() {
        Token token = new Token.Character("test");
        
        tokeniser.emit(token);
        
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmit_String_ReturnPendingToken() {
        String str = "test";
        
        tokeniser.emit(str);
        
        assertTrue(tokeniser.isEmitPending);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmit_Char_ReturnPendingToken() {
        char c = 't';
        
        tokeniser.emit(c);
        
        assertTrue(tokeniser.isEmitPending);
        assertEquals(c, tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testEmitTagPending_CreateStartTag_ReturnTagToken() {
        tokeniser.tagPending = tokeniser.createTagPending(true);

        tokeniser.emitTagPending();

        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending_CreateEndTag_ReturnTagToken() {
        tokeniser.tagPending = tokeniser.createTagPending(false);

        tokeniser.emitTagPending();

        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.emitPending instanceof Token.EndTag);
    }

    @Test
    public void testCreateTagPending_CreateStartTag_ReturnStartTagToken() {
        Token.Tag tag = tokeniser.createTagPending(true);

        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_CreateEndTag_ReturnEndTagToken() {
        Token.Tag tag = tokeniser.createTagPending(false);

        assertNotNull(tag);
        assertTrue(tag instanceof Token.EndTag);
    }

    @Test
    public void testCreateCommentPending_ReturnCommentToken() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending_ReturnCommentToken() {
        tokeniser.createCommentPending();

        tokeniser.emitCommentPending();

        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending_ReturnDoctypeToken() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending_ReturnDoctypeToken() {
        tokeniser.createDoctypePending();

        tokeniser.emitDoctypePending();

        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer_ReturnStringBuilder() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
        assertTrue(tokeniser.dataBuffer instanceof StringBuilder);
    }

    @Test
    public void testIsAppropriateEndTagToken_MatchingTag_ReturnTrue() {
        tokeniser.tagPending = new Token.StartTag("html");

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertTrue(result);
    }

    @Test
    public void testIsAppropriateEndTagToken_NotMatchingTag_ReturnFalse() {
        tokeniser.tagPending = new Token.StartTag("div");

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertFalse(result);
    }

    @Test
    public void testIsTrackErrors_True_ReturnTrue() {
        boolean result = tokeniser.isTrackErrors();

        assertTrue(result);
    }

    @Test
    public void testSetTrackErrors_SetToFalse_ReturnFalse() {
        tokeniser.setTrackErrors(false);

        boolean result = tokeniser.isTrackErrors();

        assertFalse(result);
    }

    @Test
    public void testError_TrackErrorsEnabled_AddErrorToList() {
        TokeniserState state = TokeniserState.Data;
        int position = 10;

        tokeniser.setTrackErrors(true);
        tokeniser.error(state);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).getMessage());
        assertEquals(state, tokeniser.errors.get(0).getState());
        assertEquals(position, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testError_TrackErrorsDisabled_NoErrorAddedToList() {
        TokeniserState state = TokeniserState.Data;

        tokeniser.setTrackErrors(false);
        tokeniser.error(state);

        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testEofError_TrackErrorsEnabled_AddErrorToList() {
        TokeniserState state = TokeniserState.Data;
        int position = 10;

        tokeniser.setTrackErrors(true);
        tokeniser.eofError(state);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).getMessage());
        assertEquals(state, tokeniser.errors.get(0).getState());
        assertEquals(position, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testEofError_TrackErrorsDisabled_NoErrorAddedToList() {
        TokeniserState state = TokeniserState.Data;

        tokeniser.setTrackErrors(false);
        tokeniser.eofError(state);

        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError_TrackErrorsEnabled_AddErrorToList() {
        int position = 10;

        tokeniser.setTrackErrors(true);
        tokeniser.characterReferenceError();

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).getMessage());
        assertEquals(position, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testCharacterReferenceError_TrackErrorsDisabled_NoErrorAddedToList() {
        tokeniser.setTrackErrors(false);
        tokeniser.characterReferenceError();

        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testError_TrackErrorsEnabled_AddCustomErrorToList() {
        String errorMsg = "Error message";
        int position = 10;

        tokeniser.setTrackErrors(true);
        tokeniser.error(errorMsg);

        assertEquals(1, tokeniser.errors.size());
        assertEquals(errorMsg, tokeniser.errors.get(0).getMessage());
        assertEquals(position, tokeniser.errors.get(0).getPosition());
    }

    @Test
    public void testError_TrackErrorsDisabled_NoErrorAddedToList() {
        String errorMsg = "Error message";

        tokeniser.setTrackErrors(false);
        tokeniser.error(errorMsg);

        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS_AlwaysReturnTrue() {
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }
}