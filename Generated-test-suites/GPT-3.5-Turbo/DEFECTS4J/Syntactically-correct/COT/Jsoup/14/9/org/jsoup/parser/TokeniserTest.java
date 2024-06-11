
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null);
    }
    
    @Test
    public void testRead() {
        // Test case for reading tokens
        Token token = tokeniser.read();
        assertNull("Expected null token", token);
    }
    
    @Test
    public void testEmit() {
        // Test case for emitting a token
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals("Expected emitted token to be equal", token, emittedToken);
    }
    
    @Test
    public void testEmitString() {
        // Test case for emitting a string token
        String str = "test";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        assertEquals("Expected emitted string token to be equal", new Token.Character(str), emittedToken);
    }
    
    @Test
    public void testEmitChar() {
        // Test case for emitting a character token
        char c = 't';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        assertEquals("Expected emitted character token to be equal", new Token.Character(Character.toString(c)), emittedToken);
    }
    
    @Test
    public void testTransition() {
        // Test case for transitioning to a new state
        Tokeniser.TokeniserState state = Tokeniser.TokeniserState.TagName;
        tokeniser.transition(state);
        assertEquals("Expected current state to be equal", state, tokeniser.getState());
    }
    
    @Test
    public void testAdvanceTransition() {
        // Test case for advancing to a new state
        Tokeniser.TokeniserState state = Tokeniser.TokeniserState.TagName;
        tokeniser.advanceTransition(state);
        assertEquals("Expected current state to be equal", state, tokeniser.getState());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test case for acknowledging self closing flag
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue("Expected self closing flag to be acknowledged", tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        // Test case for consuming a character reference
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull("Expected null character reference", result);
    }
    
    @Test
    public void testCreateTagPending() {
        // Test case for creating a tag pending
        boolean start = true;
        Tokeniser.Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull("Expected tag pending to be created", tag);
        assertTrue("Expected tag pending to be a StartTag", tag instanceof Tokeniser.Token.StartTag);
    }
    
    @Test
    public void testEmitTagPending() {
        // Test case for emitting a tag pending
        tokeniser.tagPending = new Tokeniser.Token.StartTag();
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertTrue("Expected emitted token to be a StartTag", emittedToken instanceof Tokeniser.Token.StartTag);
    }
    
    @Test
    public void testCreateCommentPending() {
        // Test case for creating a comment pending
        tokeniser.createCommentPending();
        assertNotNull("Expected comment pending to be created", tokeniser.commentPending);
    }
    
    @Test
    public void testEmitCommentPending() {
        // Test case for emitting a comment pending
        tokeniser.commentPending = new Tokeniser.Token.Comment();
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertTrue("Expected emitted token to be a Comment", emittedToken instanceof Tokeniser.Token.Comment);
    }
    
    @Test
    public void testCreateDoctypePending() {
        // Test case for creating a doctype pending
        tokeniser.createDoctypePending();
        assertNotNull("Expected doctype pending to be created", tokeniser.doctypePending);
    }
    
    @Test
    public void testEmitDoctypePending() {
        // Test case for emitting a doctype pending
        tokeniser.doctypePending = new Tokeniser.Token.Doctype();
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertTrue("Expected emitted token to be a Doctype", emittedToken instanceof Tokeniser.Token.Doctype);
    }
    
    @Test
    public void testCreateTempBuffer() {
        // Test case for creating a temporary buffer
        tokeniser.createTempBuffer();
        assertNotNull("Expected temporary buffer to be created", tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        // Test case for checking if the current token is an appropriate end tag token
        tokeniser.tagPending = new Tokeniser.Token.StartTag("div");
        tokeniser.lastStartTag = new Tokeniser.Token.StartTag("div");
        assertTrue("Expected appropriate end tag token", tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testIsTrackErrors() {
        // Test case for checking if track errors is enabled
        assertTrue("Expected track errors to be enabled", tokeniser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        // Test case for setting track errors
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        assertFalse("Expected track errors to be disabled", tokeniser.isTrackErrors());
    }
    
    @Test
    public void testError() {
        // Test case for recording an error
        Tokeniser.TokeniserState state = Tokeniser.TokeniserState.Data;
        tokeniser.error(state);
        assertEquals("Expected 1 error to be recorded", 1, tokeniser.errors.size());
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).getErrorMessage());
        assertEquals(state, tokeniser.errors.get(0).getState());
    }
    
    @Test
    public void testEofError() {
        // Test case for recording an EOF error
        Tokeniser.TokeniserState state = Tokeniser.TokeniserState.Data;
        tokeniser.eofError(state);
        assertEquals("Expected 1 error to be recorded", 1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).getErrorMessage());
        assertEquals(state, tokeniser.errors.get(0).getState());
    }
    
    @Test
    public void testCharacterReferenceError() {
        // Test case for recording a character reference error
        tokeniser.characterReferenceError();
        assertEquals("Expected 1 error to be recorded", 1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).getErrorMessage());
    }
    
    @Test
    public void testErrorWithMessage() {
        // Test case for recording an error with a custom message
        String errorMsg = "Custom error message";
        tokeniser.error(errorMsg);
        assertEquals("Expected 1 error to be recorded", 1, tokeniser.errors.size());
        assertEquals(errorMsg, tokeniser.errors.get(0).getErrorMessage());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test case for checking if the current node is in the HTML namespace
        assertTrue("Expected current node to be in HTML namespace", tokeniser.currentNodeInHtmlNS());
    }
}
