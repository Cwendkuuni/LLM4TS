import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = tokeniser.read();
        assertNull(token); // Expecting null as there is no token to read initially
    }
    
    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = new Token();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read()); // Expecting the emitted token to be read
    }
    
    @Test
    public void testEmit_String() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String str = "test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character); // Expecting the emitted string to be read as a character token
        assertEquals(str, ((Token.Character) token).getData());
    }
    
    @Test
    public void testEmit_Char() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        char c = 'a';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character); // Expecting the emitted char to be read as a character token
        assertEquals(String.valueOf(c), ((Token.Character) token).getData());
    }
    
    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState newState = TokeniserState.Comment;
        tokeniser.transition(newState);
        assertEquals(newState, tokeniser.getState()); // Expecting the state to be transitioned to the new state
    }
    
    @Test
    public void testAdvanceTransition() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState newState = TokeniserState.Comment;
        tokeniser.advanceTransition(newState);
        assertEquals(newState, tokeniser.getState()); // Expecting the state to be transitioned to the new state after advancing
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged()); // Expecting the self closing flag to be acknowledged
    }
    
    @Test
    public void testConsumeCharacterReference_ValidNumeric() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.emit("&");
        tokeniser.consumeCharacterReference(null, false);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character); // Expecting the emitted character reference to be read as a character token
        assertEquals(Character.toString((char) 65533), ((Token.Character) token).getData());
    }
    
    @Test
    public void testConsumeCharacterReference_ValidNamed() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.emit("&");
        tokeniser.consumeCharacterReference(null, false);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character); // Expecting the emitted character reference to be read as a character token
        assertEquals(Character.toString((char) 65533), ((Token.Character) token).getData());
    }
    
    @Test
    public void testCreateTagPending_Start() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.getTagPending() instanceof Token.StartTag); // Expecting the tag pending to be a start tag
    }
    
    @Test
    public void testCreateTagPending_End() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(false);
        assertTrue(tokeniser.getTagPending() instanceof Token.EndTag); // Expecting the tag pending to be an end tag
    }
    
    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token tagToken = tokeniser.read();
        assertTrue(tagToken instanceof Token.StartTag); // Expecting the emitted tag to be a start tag
        assertEquals(tokeniser.getTagPending(), tagToken);
    }
    
    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createCommentPending();
        assertTrue(tokeniser.getCommentPending() instanceof Token.Comment); // Expecting the comment pending to be a comment token
    }
    
    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token commentToken = tokeniser.read();
        assertTrue(commentToken instanceof Token.Comment); // Expecting the emitted comment token to be a comment token
        assertEquals(tokeniser.getCommentPending(), commentToken);
    }
    
    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.getDoctypePending() instanceof Token.Doctype); // Expecting the doctype pending to be a doctype token
    }
    
    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token doctypeToken = tokeniser.read();
        assertTrue(doctypeToken instanceof Token.Doctype); // Expecting the emitted doctype token to be a doctype token
        assertEquals(tokeniser.getDoctypePending(), doctypeToken);
    }
    
    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.getDataBuffer()); // Expecting the data buffer to be created
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().setTagName("tag");
        tokeniser.setLastStartTag(tokeniser.getTagPending());
        assertTrue(tokeniser.isAppropriateEndTagToken()); // Expecting the tag pending to be an appropriate end tag token
    }
    
    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().setTagName("tag");
        tokeniser.setLastStartTag(tokeniser.getTagPending());
        assertEquals("tag", tokeniser.appropriateEndTagName()); // Expecting the appropriate end tag name to be the same as the start tag name
    }
    
    @Test
    public void testError_TokeniserState() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.error(TokeniserState.Comment);
        assertEquals(1, tokeniser.getErrors().size()); // Expecting an error to be added to the error list
        assertEquals("Unexpected character '' in input state [Comment]", tokeniser.getErrors().get(0).getMessage());
    }
    
    @Test
    public void testEofError_TokeniserState() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.eofError(TokeniserState.Comment);
        assertEquals(1, tokeniser.getErrors().size()); // Expecting an error to be added to the error list
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Comment]", tokeniser.getErrors().get(0).getMessage());
    }
    
    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.characterReferenceError("Invalid character reference");
        assertEquals(1, tokeniser.getErrors().size()); // Expecting an error to be added to the error list
        assertEquals("Invalid character reference: Invalid character reference", tokeniser.getErrors().get(0).getMessage());
    }
    
    @Test
    public void testError_String() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.error("Invalid token");
        assertEquals(1, tokeniser.getErrors().size()); // Expecting an error to be added to the error list
        assertEquals("Invalid token", tokeniser.getErrors().get(0).getMessage());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertTrue(tokeniser.currentNodeInHtmlNS()); // Expecting the current node to be in the HTML namespace
    }

}