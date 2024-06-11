import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("test");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit() {
        Token.Character character = new Token.Character("test");
        tokeniser.emit(character);

        assertTrue(tokeniser.isEmitPending);
        assertEquals(character, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);

        assertEquals("test", tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);

        assertEquals("t", tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();

        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.Rcdata;
        tokeniser.transition(newState);

        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.Rcdata;
        tokeniser.advanceTransition(newState);

        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = false;

        CharacterReference characterReference = new CharacterReference(tokeniser, additionalAllowedCharacter, inAttribute);
        Character result = characterReference.consumeCharacterReference();

        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);

        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertNotNull(tokeniser.tagPending);
        assertEquals(Token.TokenType.StartTag, tokeniser.tagPending.type);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        assertNotNull(tokeniser.commentPending);
        assertEquals(Token.TokenType.Comment, tokeniser.commentPending.type);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        assertNotNull(tokeniser.doctypePending);
        assertEquals(Token.TokenType.Doctype, tokeniser.doctypePending.type);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "test";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = true;
        tokeniser.setTrackErrors(trackErrors);

        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        List<ParseError> errors = tokeniser.errors;

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character in input", error.getErrorMsg());
        assertEquals(null, error.getCurrentChar());
        assertEquals(TokeniserState.Data, error.getState());
        assertEquals(0, error.getPos());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        List<ParseError> errors = tokeniser.errors;

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF)", error.getErrorMsg());
        assertEquals(TokeniserState.Data, error.getState());
        assertEquals(0, error.getPos());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        List<ParseError> errors = tokeniser.errors;

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference", error.getErrorMsg());
        assertEquals(0, error.getPos());
    }

    @Test
    public void testErrorWithMsg() {
        String errorMsg = "Test error";
        tokeniser.error(errorMsg);
        List<ParseError> errors = tokeniser.errors;

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(errorMsg, error.getErrorMsg());
        assertEquals(0, error.getPos());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }
}