import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.io.StringReader;


public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        tokeniser = new Tokeniser(new CharacterReader(new StringReader(html)));
    }

    @Test
    public void read_shouldReturnTokenCharacter_whenCharacterTokenPresent() {
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.Character);
        assertEquals("<", ((Token.Character) token).getData());
    }

    @Test
    public void read_shouldReturnTokenStartTag_whenStartTagTokenPresent() {
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.StartTag);
        assertEquals("html", ((Token.StartTag) token).name());
    }

    @Test
    public void read_shouldReturnTokenEndTag_whenEndTagTokenPresent() {
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.EndTag);
        assertEquals("html", ((Token.EndTag) token).name());
    }

    @Test
    public void read_shouldReturnTokenDoctype_whenDoctypeTokenPresent() {
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.Doctype);
        assertEquals("", ((Token.Doctype) token).getName());
    }

    @Test
    public void emit_shouldAddTokenToPending_whenTokenEmitted() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);

        assertEquals(startTag, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void emit_shouldAddStringToCharBuffer_whenStringEmitted() {
        String str = "Test";
        tokeniser.emit(str);

        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void emit_shouldAddCharacterToCharBuffer_whenCharacterEmitted() {
        char c = '<';
        tokeniser.emit(c);

        assertEquals(Character.toString(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void getState_shouldReturnTokeniserState() {
        TokeniserState state = tokeniser.getState();

        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void transition_shouldChangeCurrentTokeniserState() {
        TokeniserState state = TokeniserState.EndTag;
        tokeniser.transition(state);

        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void advanceTransition_shouldAdvanceReaderAndChangeTokeniserState() {
        TokeniserState state = TokeniserState.StartTag;
        tokeniser.advanceTransition(state);

        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void acknowledgeSelfClosingFlag_shouldSetSelfClosingFlagAcknowledgedToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void consumeCharacterReference_shouldReturnNull_whenReaderIsEmpty() {
        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void consumeCharacterReference_shouldReturnNull_whenAdditionalAllowedCharacterMatchesCurrentCharacter() {
        String html = "&lt test";
        tokeniser = new Tokeniser(new CharacterReader(new StringReader(html)));

        Character character = tokeniser.consumeCharacterReference('<', false);

        assertNull(character);
    }

    @Test
    public void consumeCharacterReference_shouldReturnNull_whenCurrentCharacterIsSpecialCharacter() {
        String html = "&";
        tokeniser = new Tokeniser(new CharacterReader(new StringReader(html)));

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void createTagPending_shouldReturnTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);

        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void emitTagPending_shouldAddTagPendingToTokenBuffer() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertEquals(startTag, tokeniser.emitPending);
    }

    @Test
    public void createCommentPending_shouldReturnCommentPending() {
        tokeniser.createCommentPending();

        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void emitCommentPending_shouldAddCommentPendingToTokenBuffer() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        assertTrue(tokeniser.emitPending instanceof Token.Comment);
    }

    @Test
    public void createDoctypePending_shouldReturnDoctypePending() {
        tokeniser.createDoctypePending();

        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void emitDoctypePending_shouldAddDoctypePendingToTokenBuffer() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }

    @Test
    public void createTempBuffer_shouldCreateDataBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void isAppropriateEndTagToken_shouldReturnTrue_whenTagPendingIsAppropriateEndTagToken() {
        boolean start = true;
        tokeniser.createTagPending(start);
        tokeniser.tagPending.start();
        tokeniser.lastStartTag = (Token.StartTag)tokeniser.tagPending;
        tokeniser.tagPending.tagName = "h1";

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void isAppropriateEndTagToken_shouldReturnFalse_whenTagPendingIsNotAppropriateEndTagToken() {
        boolean start = true;
        tokeniser.createTagPending(start);
        tokeniser.tagPending.start();
        tokeniser.lastStartTag = (Token.StartTag)tokeniser.tagPending;
        tokeniser.tagPending.tagName = "body";

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void isTrackErrors_shouldReturnTrue_whenTrackErrorsIsTrue() {
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void setTrackErrors_shouldSetTrackErrors() {
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);

        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void error_shouldAddParseErrorToErrorsList() {
        TokeniserState state = TokeniserState.EndTag;
        tokeniser.error(state);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void eofError_shouldAddParseErrorToErrorsList() {
        TokeniserState state = TokeniserState.CommentEndDash;
        tokeniser.eofError(state);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void characterReferenceError_shouldAddParseErrorToErrorsList() {
        tokeniser.characterReferenceError();

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void error_withCustomErrorMessage_shouldAddCustomParseErrorToErrorsList() {
        String errorMsg = "Invalid input format";
        tokeniser.error(errorMsg);

        assertEquals(1, tokeniser.errors.size());
        assertEquals(errorMsg, tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void currentNodeInHtmlNS_shouldReturnTrue_whenCurrentNodeIsInHtmlNS() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();

        assertTrue(currentNodeInHtmlNS);
    }

    // Add more test cases for other methods as required

}