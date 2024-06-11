import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        // Initialize the Tokeniser
        tokeniser = new Tokeniser(new CharacterReader(""));
    }

    @Test
    public void testRead() {
        // Test the read() method
        Token token = tokeniser.read();
        assertNull(token); // Token should be null when character buffer is empty
    }

    @Test
    public void testEmit() {
        // Test the emit(Token) method
        Token token = new Token.Character("Test");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken); // Token emitted should be same as the one passed to emit()
    }

    @Test
    public void testEmitString() {
        // Test the emit(String) method
        String str = "Test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(str, ((Token.Character) token).getData()); // String emitted should be same as the one passed to emit()
    }

    @Test
    public void testEmitChar() {
        // Test the emit(char) method
        char c = 'T';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(String.valueOf(c), ((Token.Character) token).getData()); // Char emitted should be same as the one passed to emit()
    }

    @Test
    public void testGetState() {
        // Test the getState() method
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state); // Initial state should be Data
    }

    @Test
    public void testTransition() {
        // Test the transition(TokeniserState) method
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.transition(state);
        TokeniserState currentState = tokeniser.getState();
        assertEquals(state, currentState); // Current state should be same as the state passed to transition()
    }

    @Test
    public void testAdvanceTransition() {
        // Test the advanceTransition(TokeniserState) method
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.advanceTransition(state);
        TokeniserState currentState = tokeniser.getState();
        assertEquals(state, currentState); // Current state should be same as the state passed to advanceTransition()
        // Test that reader is advanced
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test the acknowledgeSelfClosingFlag() method
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged); // selfClosingFlagAcknowledged should be set to true
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test the consumeCharacterReference(Character, boolean) method
        Character additionalAllowedCharacter = 'T';
        boolean inAttribute = false;
        Character characterReference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(characterReference); // characterReference should be null when reader is empty

        // Test additionalAllowedCharacter
        String input = "TABC;"; // Character should not be consumed as additionalAllowedCharacter is 'T'
        tokeniser = new Tokeniser(new CharacterReader(input));
        characterReference = tokeniser.consumeCharacterReference('T', inAttribute);
        assertNull(characterReference); // characterReference should be null as additionalAllowedCharacter is 'T'

        // Test invalid character reference
        input = "&#;"; // Empty numbered character reference
        tokeniser = new Tokeniser(new CharacterReader(input));
        characterReference = tokeniser.consumeCharacterReference(null, false);
        assertNull(characterReference); // characterReference should be null as character reference is invalid

        // Test valid numbered character reference - decimal mode
        input = "&#65;"; // Numeric character reference for ASCII 'A'
        tokeniser = new Tokeniser(new CharacterReader(input));
        characterReference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('A', characterReference.charValue()); // characterReference should be 'A'

        // Test valid numbered character reference - hexadecimal mode
        input = "&#x41;"; // Numeric character reference for ASCII 'A'
        tokeniser = new Tokeniser(new CharacterReader(input));
        characterReference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('A', characterReference.charValue()); // characterReference should be 'A'

        // Test invalid numbered character reference - out of range
        input = "&#123456;"; // Numeric character reference out of range
        tokeniser = new Tokeniser(new CharacterReader(input));
        characterReference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\uFFFD', characterReference.charValue()); // characterReference should be replacement character

        // Test valid named character reference
        input = "&amp;"; // Named character reference for '&'
        tokeniser = new Tokeniser(new CharacterReader(input));
        characterReference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', characterReference.charValue()); // characterReference should be '&'
    }

    @Test
    public void testCreateTagPending() {
        // Test the createTagPending(boolean) method
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertTrue(tag instanceof Token.StartTag); // createTagPending(true) should return StartTag object
        start = false;
        tag = tokeniser.createTagPending(start);
        assertTrue(tag instanceof Token.EndTag); // createTagPending(false) should return EndTag object
    }

    @Test
    public void testEmitTagPending() {
        // Test the emitTagPending() method
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(tag, emittedToken); // Token emitted should be same as the tag pending
    }

    @Test
    public void testCreateCommentPending() {
        // Test the createCommentPending() method
        tokeniser.createCommentPending();
        assertTrue(tokeniser.commentPending instanceof Token.Comment); // commentPending should be instantiated as Token.Comment
    }

    @Test
    public void testEmitCommentPending() {
        // Test the emitCommentPending() method
        tokeniser.createCommentPending();
        Token.Comment comment = tokeniser.commentPending;
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertEquals(comment, emittedToken); // Token emitted should be same as the comment pending
    }

    @Test
    public void testCreateDoctypePending() {
        // Test the createDoctypePending() method
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype); // doctypePending should be instantiated as Token.Doctype
    }

    @Test
    public void testEmitDoctypePending() {
        // Test the emitDoctypePending() method
        tokeniser.createDoctypePending();
        Token.Doctype doctype = tokeniser.doctypePending;
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertEquals(doctype, emittedToken); // Token emitted should be same as the doctype pending
    }

    @Test
    public void testCreateTempBuffer() {
        // Test the createTempBuffer() method
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer); // dataBuffer should be instantiated
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test the isAppropriateEndTagToken() method
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "div";
        boolean isAppropriate = tokeniser.isAppropriateEndTagToken();
        assertTrue(isAppropriate); // tagPending tagName should match lastStartTag tagName
    }

    @Test
    public void testIsTrackErrors() {
        // Test the isTrackErrors() method
        boolean trackErrors = tokeniser.isTrackErrors();
        assertTrue(trackErrors); // trackErrors should be true by default
    }

    @Test
    public void testSetTrackErrors() {
        // Test the setTrackErrors(boolean) method
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        boolean newTrackErrors = tokeniser.isTrackErrors();
        assertEquals(trackErrors, newTrackErrors); // trackErrors should be set to the value passed to setTrackErrors()
    }

    @Test
    public void testError() {
        // Test the error(TokeniserState) method
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.error(state);
        // TODO: test that an error is added to the errors list
    }

    @Test
    public void testEofError() {
        // Test the eofError(TokeniserState) method
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.eofError(state);
        // TODO: test that an error is added to the errors list
    }

    @Test
    public void testCharacterReferenceError() {
        // Test the characterReferenceError() method
        // TODO: test that an error is added to the errors list
    }

    @Test
    public void testErrorString() {
        // Test the error(String) method
        String errorMsg = "Invalid character";
        tokeniser.error(errorMsg);
        // TODO: test that an error is added to the errors list with the correct error message
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test the currentNodeInHtmlNS() method
        // TODO: implement test once namespaces are implemented correctly
    }

}