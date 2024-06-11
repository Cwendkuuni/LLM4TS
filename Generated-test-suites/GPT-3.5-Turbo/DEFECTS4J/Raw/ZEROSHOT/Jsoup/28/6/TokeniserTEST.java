import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }
    
    @Test
    public void testRead_emptyReader_returnsNull() {
        Token token = tokeniser.read();
        assertNull(token);
    }
    
    @Test
    public void testRead_selfClosingFlagNotAcknowledged_errorIsThrown() {
        tokeniser.acknowledgeSelfClosingFlag();
        
        // Ignore the error logs, just need to verify that an error is thrown
        Token token = tokeniser.read();
        
        assertTrue(tokeniser.getErrors().size() > 0);
    }
    
    @Test
    public void testEmit_tagToken_emitPendingIsSet() {
        Token.Tag tag = new Token.Tag();
        
        tokeniser.emit(tag);
        
        assertNotNull(tokeniser.getEmitPending());
        assertEquals(tokeniser.getEmitPending(), tag);
    }
    
    @Test
    public void testEmit_commentToken_emitPendingIsSet() {
        Token.Comment comment = new Token.Comment();
        
        tokeniser.emit(comment);
        
        assertNotNull(tokeniser.getEmitPending());
        assertEquals(tokeniser.getEmitPending(), comment);
    }
    
    @Test
    public void testEmit_doctypeToken_emitPendingIsSet() {
        Token.Doctype doctype = new Token.Doctype();
        
        tokeniser.emit(doctype);
        
        assertNotNull(tokeniser.getEmitPending());
        assertEquals(tokeniser.getEmitPending(), doctype);
    }
    
    @Test
    public void testEmit_string_strIsEmittedAsCharacterToken() {
        String str = "Hello World";
        
        tokeniser.emit(str);
        Token token = tokeniser.getEmitPending();
        
        assertNotNull(token);
        assertEquals(token.type, Token.TokenType.Character);
        assertEquals(((Token.Character)token).getData(), str);
    }
    
    @Test
    public void testEmit_char_charIsEmittedAsCharacterToken() {
        char c = 'H';
        
        tokeniser.emit(c);
        Token token = tokeniser.getEmitPending();
        
        assertNotNull(token);
        assertEquals(token.type, Token.TokenType.Character);
        assertEquals(((Token.Character)token).getData(), String.valueOf(c));
    }

    @Test
    public void testTransition_stateIsSetToGivenState() {
        TokeniserState state = TokeniserState.Doctype;
        
        tokeniser.transition(state);
        
        assertEquals(tokeniser.getState(), state);
    }
    
    @Test
    public void testAdvanceTransition_stateIsSetToGivenStateAndReaderIsAdvanced() {
        TokeniserState state = TokeniserState.StartTag;
        
        tokeniser.advanceTransition(state);
        
        assertEquals(tokeniser.getState(), state);
        assertEquals(tokeniser.getReader().pos(), 1);
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag_selfClosingFlagAcknowledgedIsSetToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();
        
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }
    
    @Test
    public void testConsumeCharacterReference_additonalAllowedCharacterIsNull_returnsNull() {
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertNull(result);
    }
    
    @Test
    public void testConsumeCharacterReference_additonalAllowedCharacterMatchesCurrentCharacter_returnsNull() {
        tokeniser.getReader().addCharacter('a');
        
        Character result = tokeniser.consumeCharacterReference('a', false);
        assertNull(result);
    }
    
    @Test
    public void testConsumeCharacterReference_matchedConsumeNumbered_returnsCharacter() {
        String numRef = "&#65;";
        tokeniser.getReader().addString(numRef);
        
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(result, 'A');
    }
    
    @Test
    public void testConsumeCharacterReference_matchedConsumeNamed_returnsCharacter() {
        String namedRef = "&amp;";
        tokeniser.getReader().addString(namedRef);
        
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(result, '&');
    }
    
    @Test
    public void testCreateTagPending_startIsTrue_returnsStartTag() {
        Token.Tag tag = tokeniser.createTagPending(true);
        
        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }
    
    @Test
    public void testCreateTagPending_startIsFalse_returnsEndTag() {
        Token.Tag tag = tokeniser.createTagPending(false);
        
        assertNotNull(tag);
        assertTrue(tag instanceof Token.EndTag);
    }
    
    @Test
    public void testEmitTagPending_tagPendingIsFinalisedAndEmitted() {
        tokeniser.createTagPending(true);
        
        tokeniser.emitTagPending();
        
        assertNotNull(tokeniser.getEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.StartTag);
        assertTrue(tokeniser.getTagPending().isFinalised);
    }
    
    @Test
    public void testCreateCommentPending_commentPendingIsCreated() {
        tokeniser.createCommentPending();
        
        assertNotNull(tokeniser.getCommentPending());
    }
    
    @Test
    public void testEmitCommentPending_commentPendingIsEmitted() {
        tokeniser.createCommentPending();
        
        tokeniser.emitCommentPending();
        
        assertNotNull(tokeniser.getEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.Comment);
    }
    
    @Test
    public void testCreateDoctypePending_doctypePendingIsCreated() {
        tokeniser.createDoctypePending();
        
        assertNotNull(tokeniser.getDoctypePending());
    }
    
    @Test
    public void testEmitDoctypePending_doctypePendingIsEmitted() {
        tokeniser.createDoctypePending();
        
        tokeniser.emitDoctypePending();
        
        assertNotNull(tokeniser.getEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.Doctype);
    }
    
    @Test
    public void testCreateTempBuffer_dataBufferIsCreated() {
        tokeniser.createTempBuffer();
        
        assertNotNull(tokeniser.getDataBuffer());
    }
    
    @Test
    public void testIsAppropriateEndTagToken_lastStartTagIsNull_returnsFalse() {
        boolean result = tokeniser.isAppropriateEndTagToken();
        
        assertFalse(result);
    }
    
    @Test
    public void testIsAppropriateEndTagToken_tagNameMatchesLastStartTag_returnsTrue() {
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().tagName = "div";
        
        tokeniser.lastStartTag = (Token.StartTag)tokeniser.getTagPending();
        
        boolean result = tokeniser.isAppropriateEndTagToken();
        
        assertTrue(result);
    }
    
    @Test
    public void testAppropriateEndTagName_lastStartTagIsNull_returnsNull() {
        String result = tokeniser.appropriateEndTagName();
        
        assertNull(result);
    }
    
    @Test
    public void testAppropriateEndTagName_lastStartTagIsNotNull_returnsTagName() {
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().tagName = "div";
        
        tokeniser.lastStartTag = (Token.StartTag)tokeniser.getTagPending();
        
        String result = tokeniser.appropriateEndTagName();
        
        assertEquals(result, "div");
    }
    
    @Test
    public void testError_addsErrorToList() {
        tokeniser.error(TokeniserState.Data);
        
        assertTrue(tokeniser.getErrors().size() == 1);
    }
    
    @Test
    public void testEofError_addsErrorToList() {
        tokeniser.eofError(TokeniserState.Data);
        
        assertTrue(tokeniser.getErrors().size() == 1);
    }
    
    @Test
    public void testCharacterReferenceError_addsErrorToList() {
        tokeniser.characterReferenceError("Invalid character reference");
        
        assertTrue(tokeniser.getErrors().size() == 1);
    }
    
    @Test
    public void testError_addsCustomErrorMessage() {
        String errorMsg = "Custom error message";
        
        tokeniser.error(errorMsg);
        
        assertTrue(tokeniser.getErrors().size() == 1);
        assertTrue(tokeniser.getErrors().get(0).getErrorMessage().equals(errorMsg));
    }
    
    @Test
    public void testCurrentNodeInHtmlNS_alwaysReturnsTrue() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        
        assertTrue(result);
    }

}