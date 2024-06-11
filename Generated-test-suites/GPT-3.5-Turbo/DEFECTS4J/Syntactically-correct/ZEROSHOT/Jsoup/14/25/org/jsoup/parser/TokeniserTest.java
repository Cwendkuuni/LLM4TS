import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader();
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals("test", ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("test");
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals("test", ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('t');
        tokeniser.emit('e');
        tokeniser.emit('s');
        tokeniser.emit('t');
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals("test", ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.RcData);
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
        assertEquals(TokeniserState.RcData, state);
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.RcData);
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
        assertEquals(TokeniserState.RcData, state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character characterRef = tokeniser.consumeCharacterReference(null, false);
        assertNull(characterRef);
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertNotNull(tokeniser.getTagPending());
        assertTrue(tokeniser.getTagPending() instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertTrue(emittedToken instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.getCommentPending());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertTrue(emittedToken instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.getDoctypePending());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertTrue(emittedToken instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.getDataBuffer());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().setTagName("testTag");
        tokeniser.setLastStartTag(tokeniser.getTagPending());
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsNotAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().setTagName("startTag");
        tokeniser.setLastStartTag(tokeniser.getTagPending());
        tokeniser.createTagPending(false);
        tokeniser.getTagPending().setTagName("endTag");
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        List<ParseError> errors = tokeniser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Unexpected character in input", errors.get(0).getErrorMsg());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        List<ParseError> errors = tokeniser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getErrorMsg());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        List<ParseError> errors = tokeniser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference", errors.get(0).getErrorMsg());
    }

    @Test
    public void testErrorWithMessage() {
        tokeniser.error("Test error");
        List<ParseError> errors = tokeniser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Test error", errors.get(0).getErrorMsg());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}