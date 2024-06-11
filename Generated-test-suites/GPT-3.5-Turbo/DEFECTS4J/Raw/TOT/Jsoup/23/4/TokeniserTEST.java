import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(new CharacterReader(""), errors);
    }

    @Test
    public void testRead_EmptyInput_ReturnsNull() {
        Token result = tokeniser.read();

        assertNull(result);
    }

    @Test
    public void testRead_WithData_ReturnsTokenCharacter() {
        tokeniser.emit("Hello World");

        Token result = tokeniser.read();

        assertNotNull(result);
        assertTrue(result instanceof Token.Character);
        assertEquals("Hello World", ((Token.Character) result).getData());
    }

    @Test
    public void testEmit_StartTag_AddsTokenToQueue() {
        Token.Tag tag = tokeniser.createTagPending(true);

        tokeniser.emitTagPending();

        Token result = tokeniser.read();
        assertNotNull(result);
        assertTrue(result instanceof Token.StartTag);
        assertEquals(tag, result);
    }

    @Test
    public void testEmit_EndTagWithAttributes_ThrowsError() {
        Token.Tag tag = tokeniser.createTagPending(false);
        tag.addAttribute("class", "example");

        tokeniser.emitTagPending();

        ParseErrorList errors = tokeniser.getErrors();
        assertTrue(errors.size() > 0);
        assertEquals("Attributes incorrectly present on end tag", errors.get(errors.size() - 1).getMessage());
    }

    @Test
    public void testEmit_String_AddsToCharBuffer() {
        tokeniser.emit("This is a test");

        Token result = tokeniser.read();

        assertNotNull(result);
        assertTrue(result instanceof Token.Character);
        assertEquals("This is a test", ((Token.Character) result).getData());
    }

    @Test
    public void testEmit_Char_AddsToCharBuffer() {
        tokeniser.emit('A');

        Token result = tokeniser.read();

        assertNotNull(result);
        assertTrue(result instanceof Token.Character);
        assertEquals("A", ((Token.Character) result).getData());
    }

    @Test
    public void testTransition_ChangesState() {
        TokeniserState newState = TokeniserState.BeforeDoctypeName;
        tokeniser.transition(newState);

        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_ChangesStateAndAdvancesReader() {
        CharacterReader reader = tokeniser.getReader();

        tokeniser.advanceTransition(TokeniserState.Data);

        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_SetsFlagToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference_WithNumericReference_ReturnsCharacter() {
        CharacterReader reader = tokeniser.getReader();
        reader.addStream("#65;");
        Character expected = 'A';

        Character result = tokeniser.consumeCharacterReference(null, true);

        assertEquals(expected, result);
    }

    @Test
    public void testConsumeCharacterReference_WithInvalidNumericReference_ReturnsReplacementChar() {
        CharacterReader reader = tokeniser.getReader();
        reader.addStream("#999;");
        Character expected = '\ufffd';

        Character result = tokeniser.consumeCharacterReference(null, true);

        assertEquals(expected, result);
    }

    @Test
    public void testConsumeCharacterReference_WithNamedReference_ReturnsCharacter() {
        CharacterReader reader = tokeniser.getReader();
        reader.addStream("&amp;");
        Character expected = '&';

        Character result = tokeniser.consumeCharacterReference(null, true);

        assertEquals(expected, result);
    }

    @Test
    public void testConsumeCharacterReference_WithInvalidNamedReference_ReturnsNull() {
        CharacterReader reader = tokeniser.getReader();
        reader.addStream("&invalid;");
        Character expected = null;

        Character result = tokeniser.consumeCharacterReference(null, true);

        assertEquals(expected, result);
    }

    @Test
    public void testCreateTagPending_WithStartTrue_CreatesStartTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);

        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_WithStartFalse_CreatesEndTagPending() {
        Token.Tag tag = tokeniser.createTagPending(false);

        assertNotNull(tag);
        assertTrue(tag instanceof Token.EndTag);
    }

    @Test
    public void testEmitCommentPending_AddsTokenToQueue() {
        tokeniser.createCommentPending();

        tokeniser.emitCommentPending();

        Token result = tokeniser.read();
        assertNotNull(result);
        assertTrue(result instanceof Token.Comment);
    }

    @Test
    public void testEmitDoctypePending_AddsTokenToQueue() {
        tokeniser.createDoctypePending();

        tokeniser.emitDoctypePending();

        Token result = tokeniser.read();
        assertNotNull(result);
        assertTrue(result instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer_CreatesDataBuffer() {
        tokeniser.createTempBuffer();

        StringBuilder result = tokeniser.getDataBuffer();
        assertNotNull(result);
    }

    @Test
    public void testIsAppropriateEndTagToken_WhenTagPendingAndLastStartTagAreEqual_ReturnsTrue() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tokeniser.setLastStartTag(tagPending);

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertTrue(result);
    }

    @Test
    public void testIsAppropriateEndTagToken_WhenTagPendingAndLastStartTagAreDifferent_ReturnsFalse() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tokeniser.setLastStartTag(tagPending);

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertFalse(result);
    }

    @Test
    public void testAppropriateEndTagName_ReturnsLastStartTagTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.setName("div");
        tokeniser.setLastStartTag(startTag);

        String result = tokeniser.appropriateEndTagName();

        assertEquals("div", result);
    }

    @Test
    public void testError_WithState_AddsParseError() {
        TokeniserState state = TokeniserState.Data;

        tokeniser.error(state);

        ParseErrorList errors = tokeniser.getErrors();
        assertTrue(errors.size() > 0);
        assertEquals("Unexpected character 'null' in input state [Data]", errors.get(errors.size() - 1).getMessage());
    }

    @Test
    public void testEofError_WithState_AddsParseError() {
        TokeniserState state = TokeniserState.Data;

        tokeniser.eofError(state);

        ParseErrorList errors = tokeniser.getErrors();
        assertTrue(errors.size() > 0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(errors.size() - 1).getMessage());
    }

    @Test
    public void testCharacterReferenceError_WithMessage_AddsParseError() {
        String message = "Invalid character reference: invalid reference";

        tokeniser.characterReferenceError(message);

        ParseErrorList errors = tokeniser.getErrors();
        assertTrue(errors.size() > 0);
        assertEquals("Invalid character reference: invalid reference", errors.get(errors.size() - 1).getMessage());
    }

    @Test
    public void testError_WithErrorMessage_AddsParseError() {
        String errorMessage = "Error: Invalid input";

        tokeniser.error(errorMessage);

        ParseErrorList errors = tokeniser.getErrors();
        assertTrue(errors.size() > 0);
        assertEquals("Error: Invalid input", errors.get(errors.size() - 1).getMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS_ReturnsTrue() {
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }
}