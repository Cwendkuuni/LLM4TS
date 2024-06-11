package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

//Test class for Tokeniser class
public class TokeniserTest {
    //Test read() method
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("Test"); //Create a CharacterReader object
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors); //Create a Tokeniser object
        
        Token result = tokeniser.read(); //Call the read() method

        assertEquals("Token type should be Character", Token.TokenType.Character, result.type);
        assertEquals("Token value should be 'Test'", "Test", ((Token.Character) result).getData());
    }

    //Test emit() method
    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        Token token = new Token.StartTag(); //Create a Token object
        tokeniser.emit(token); //Call the emit() method

        assertSame("Token should be the same", token, tokeniser.emitPending);
        assertTrue("isEmitPending should be true", tokeniser.isEmitPending);
    }

    //Test emit(String str) method
    @Test
    public void testEmitWithString() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.emit("Test"); //Call the emit(String str) method

        assertEquals("charBuffer should be 'Test'", "Test", tokeniser.charBuffer.toString());
    }

    //Test emit(char c) method
    @Test
    public void testEmitWithChar() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.emit('T'); //Call the emit(char c) method

        assertEquals("charBuffer should be 'T'", "T", tokeniser.charBuffer.toString());
    }

    //Test getState() method
    @Test
    public void testGetState() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        TokeniserState state = tokeniser.getState(); //Call the getState() method

        assertEquals("State should be TokeniserState.Data", TokeniserState.Data, state);
    }

    //Test transition(TokeniserState state) method
    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.transition(TokeniserState.Data); //Call the transition(TokeniserState state) method

        assertEquals("State should be TokeniserState.Data", TokeniserState.Data, tokeniser.getState());
    }

    //Test advanceTransition(TokeniserState state) method
    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader("Test"); //Create a CharacterReader object
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors); //Create a Tokeniser object

        tokeniser.advanceTransition(TokeniserState.Data); //Call the advanceTransition(TokeniserState state) method

        assertEquals("State should be TokeniserState.Data", TokeniserState.Data, tokeniser.getState());
        assertEquals("Position should be 1", 1, reader.pos());
    }

    //Test acknowledgeSelfClosingFlag() method
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.acknowledgeSelfClosingFlag(); //Call the acknowledgeSelfClosingFlag() method

        assertTrue("selfClosingFlagAcknowledged should be true", tokeniser.selfClosingFlagAcknowledged);
    }

    //Test consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) method
    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("Test"); //Create a CharacterReader object
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors); //Create a Tokeniser object

        Character result = tokeniser.consumeCharacterReference(null, false); //Call the consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) method

        assertNull("Result should be null", result);
    }

    //Test createTagPending(boolean start) method
    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        Token.Tag tag = tokeniser.createTagPending(true); //Call the createTagPending(boolean start) method

        assertTrue("Tag should be an instance of Token.StartTag", tag instanceof Token.StartTag);
    }

    //Test emitTagPending() method
    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.createTagPending(true); //Create a tagPending
        tokeniser.tagPending.setTagName("test"); //Set tag name

        tokeniser.emitTagPending(); //Call the emitTagPending() method

        assertEquals("Tag name should be 'test'", "test", tokeniser.emitPending.tagName);
    }

    //Test createCommentPending() method
    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.createCommentPending(); //Call the createCommentPending() method

        assertTrue("commentPending should not be null", tokeniser.commentPending != null);
    }

    //Test emitCommentPending() method
    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.createCommentPending(); //Create a commentPending

        tokeniser.emitCommentPending(); //Call the emitCommentPending() method

        assertTrue("Token should be an instance of Token.Comment", tokeniser.emitPending instanceof Token.Comment);
    }

    //Test createDoctypePending() method
    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.createDoctypePending(); //Call the createDoctypePending() method

        assertTrue("doctypePending should not be null", tokeniser.doctypePending != null);
    }

    //Test emitDoctypePending() method
    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.createDoctypePending(); //Create a doctypePending

        tokeniser.emitDoctypePending(); //Call the emitDoctypePending() method

        assertTrue("Token should be an instance of Token.Doctype", tokeniser.emitPending instanceof Token.Doctype);
    }

    //Test createTempBuffer() method
    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.createTempBuffer(); //Call the createTempBuffer() method

        assertTrue("dataBuffer should not be null", tokeniser.dataBuffer != null);
    }

    //Test isAppropriateEndTagToken() method
    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.lastStartTag = new Token.StartTag(); //Create a lastStartTag
        tokeniser.lastStartTag.tagName = "test"; //Set tag name

        assertTrue("Should return true", tokeniser.isAppropriateEndTagToken());
    }

    //Test appropriateEndTagName() method
    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        tokeniser.lastStartTag = new Token.StartTag(); //Create a lastStartTag
        tokeniser.lastStartTag.tagName = "test"; //Set tag name

        assertEquals("Tag name should be 'test'", "test", tokeniser.appropriateEndTagName());
    }

    //Test error(TokeniserState state) method
    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null, new ParseErrorList()); //Create a Tokeniser object

        tokeniser.error(TokeniserState.Data); //Call the error(TokeniserState state) method

        assertEquals("One error should be added", 1, tokeniser.errors.size());
        assertEquals("Error message should be 'Unexpected character 'null' in input state [Data]'", "Unexpected character 'null' in input state [Data]", tokeniser.errors.get(0).toString());
    }

    //Test eofError(TokeniserState state) method
    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(null, new ParseErrorList()); //Create a Tokeniser object

        tokeniser.eofError(TokeniserState.Data); //Call the eofError(TokeniserState state) method

        assertEquals("One error should be added", 1, tokeniser.errors.size());
        assertEquals("Error message should be 'Unexpectedly reached end of file (EOF) in input state [Data]'", "Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).toString());
    }

    //Test characterReferenceError(String message) method
    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(null, new ParseErrorList()); //Create a Tokeniser object

        tokeniser.characterReferenceError("Invalid"); //Call the characterReferenceError(String message) method

        assertEquals("One error should be added", 1, tokeniser.errors.size());
        assertEquals("Error message should be 'Invalid character reference: Invalid'", "Invalid character reference: Invalid", tokeniser.errors.get(0).toString());
    }

    //Test error(String errorMsg) method
    @Test
    public void testErrorWithString() {
        Tokeniser tokeniser = new Tokeniser(null, new ParseErrorList()); //Create a Tokeniser object

        tokeniser.error("Error"); //Call the error(String errorMsg) method

        assertEquals("One error should be added", 1, tokeniser.errors.size());
        assertEquals("Error message should be 'Error'", "Error", tokeniser.errors.get(0).toString());
    }

    //Test currentNodeInHtmlNS() method
    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null, null); //Create a Tokeniser object

        boolean result = tokeniser.currentNodeInHtmlNS(); //Call the currentNodeInHtmlNS() method

        assertTrue("Should return true", result);
    }
}