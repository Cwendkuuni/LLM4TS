import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

class TokeniserTest {

    private Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());

    @Test
    public void testRead() {
        // Test case for read method
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        // Test case for emit method with Token parameter
        Token token = new Token.Character("Hello");
        tokeniser.emit(token);

        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        // Test case for emit method with String parameter
        String str = "World";
        tokeniser.emit(str);

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(str, ((Token.Character)token).getData());
    }

    @Test
    public void testEmitChar() {
        // Test case for emit method with char parameter
        char c = '!';
        tokeniser.emit(c);

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(String.valueOf(c), ((Token.Character)token).getData());
    }

    @Test
    public void testGetState() {
        // Test case for getState method
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }

    @Test
    public void testTransition() {
        // Test case for transition method
        TokeniserState newState = TokeniserState.TagName;
        tokeniser.transition(newState);

        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        // Test case for advanceTransition method
        TokeniserState newState = TokeniserState.TagName;
        tokeniser.advanceTransition(newState);

        TokeniserState currentState = tokeniser.getState();
        assertEquals(newState, currentState);
        assertEquals(1, tokeniser.reader.pos()); // Reader should be advanced by 1
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test case for acknowledgeSelfClosingFlag method
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test case for consumeCharacterReference method
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;

        Character character = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);

        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        // Test case for createTagPending method
        boolean start = true;

        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tagPending);
    }

    @Test
    public void testEmitTagPending() {
        // Test case for emitTagPending method
        tokeniser.tagPending = new Token.StartTag();

        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals(tagPending, token);
    }

    @Test
    public void testCreateCommentPending() {
        // Test case for createCommentPending method
        tokeniser.createCommentPending();

        assertNotNull(commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        // Test case for emitCommentPending method
        tokeniser.commentPending = new Token.Comment();

        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
        assertEquals(commentPending, token);
    }

    @Test
    public void testCreateDoctypePending() {
        // Test case for createDoctypePending method
        tokeniser.createDoctypePending();

        assertNotNull(doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        // Test case for emitDoctypePending method
        tokeniser.doctypePending = new Token.Doctype();

        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
        assertEquals(doctypePending, token);
    }

    @Test
    public void testCreateTempBuffer() {
        // Test case for createTempBuffer method
        tokeniser.createTempBuffer();

        assertNotNull(dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test case for isAppropriateEndTagToken method
        tagPending = new Token.StartTag("div");

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertTrue(result);
    }

    @Test
    public void testAppropriateEndTagName() {
        // Test case for appropriateEndTagName method
        lastStartTag = new Token.StartTag("div");

        String tagName = tokeniser.appropriateEndTagName();

        assertEquals("div", tagName);
    }

    @Test
    public void testError() {
        // Test case for error method
        TokeniserState state = TokeniserState.Data;

        tokeniser.error(state);
        ParseErrorList errors = tokeniser.errors;
        assertEquals("", errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError() {
        // Test case for eofError method
        TokeniserState state = TokeniserState.Data;

        tokeniser.eofError(state);
        ParseErrorList errors = tokeniser.errors;
        assertEquals("", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        // Test case for characterReferenceError method
        String message = "Invalid character reference";

        tokeniser.characterReferenceError(message);
        ParseErrorList errors = tokeniser.errors;
        assertEquals("", errors.get(0).getErrorMessage());
    }

    @Test
    public void testError() {
        // Test case for error method
        String errorMsg = "Error";

        tokeniser.error(errorMsg);
        ParseErrorList errors = tokeniser.errors;
        assertEquals("", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test case for currentNodeInHtmlNS method
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }

}