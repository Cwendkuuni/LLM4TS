import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("This is a test");
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        assertEquals("Expecting Token.Character", token.getClass(), Token.Character.class);
        assertEquals("Expecting 'This is a test'", ((Token.Character) token).getData(), "This is a test");
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token token = new Token.Character("Test Character Token");

        tokeniser.emit(token);
        assertTrue("Expecting isEmitPending to be true", tokeniser.isEmitPending);
        assertEquals("Expecting token to be emitted", token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        String str = "Test String Token";

        tokeniser.emit(str);
        assertTrue("Expecting isEmitPending to be true", tokeniser.isEmitPending);
        assertEquals("Expecting token to be emitted", str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        char c = 'T';

        tokeniser.emit(c);
        assertTrue("Expecting isEmitPending to be true", tokeniser.isEmitPending);
        assertEquals("Expecting token to be emitted", String.valueOf(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        TokeniserState state = tokeniser.getState();

        assertNotNull("Expecting TokeniserState object", state);
    }

    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        TokeniserState newState = TokeniserState.Data;

        tokeniser.transition(newState);
        assertEquals("Expecting current state to be changed", newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader("Test String");
        Tokeniser tokeniser = new Tokeniser(reader);
        TokeniserState newState = TokeniserState.Data;

        tokeniser.advanceTransition(newState);
        assertEquals("Expecting current state to be changed", newState, tokeniser.getState());
        assertEquals("Expecting character reader to advance by one", 'e', reader.current());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.selfClosingFlagAcknowledged = false;

        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue("Expecting selfClosingFlagAcknowledged to be true", tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&#65;");
        Tokeniser tokeniser = new Tokeniser(reader);
        Character additionalAllowedCharacter = 'A';
        boolean inAttribute = true;

        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertEquals("Expecting 'A'", additionalAllowedCharacter, result);
        assertEquals("Expecting character reference to be consumed", '5', reader.current());
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("Test String"));
        boolean start = true;

        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull("Expecting Tag object", tag);
        assertTrue("Expecting to be StartTag", tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token.Tag tag = new Token.StartTag();

        tokeniser.tagPending = tag;
        tokeniser.emitTagPending();
        assertEquals("Expecting tag to be emitted", tag, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.createCommentPending();
        assertNotNull("Expecting Comment object", tokeniser.commentPending);
    }
    
    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token.Comment comment = new Token.Comment();
        
        tokeniser.commentPending = comment;
        tokeniser.emitCommentPending();
        assertEquals("Expecting comment to be emitted", comment, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        tokeniser.createDoctypePending();
        assertNotNull("Expecting Doctype object", tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token.Doctype doctype = new Token.Doctype();

        tokeniser.doctypePending = doctype;
        tokeniser.emitDoctypePending();
        assertEquals("Expecting doctype to be emitted", doctype, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        tokeniser.createTempBuffer();
        assertNotNull("Expecting StringBuilder object", tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token.StartTag startTag = new Token.StartTag();
        
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = startTag;
        
        assertTrue("Expecting tagPending to be appropriate end tag token", tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        assertTrue("Expecting trackErrors to be true by default", tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        boolean trackErrors = false;

        tokeniser.setTrackErrors(trackErrors);
        assertFalse("Expecting trackErrors to be false", tokeniser.isTrackErrors());
    }

    @Test
    public void testErrorWithStringParam() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        String errorMsg = "Error Message";

        tokeniser.error(errorMsg);
        assertFalse("Expecting errors to be tracked", tokeniser.errors.isEmpty());
        assertEquals("Expecting error to have the specified message", errorMsg, tokeniser.errors.get(0).getErrorMessage());
    }
    
    @Test
    public void testErrorWithTokeniserStateParam() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        TokeniserState state = TokeniserState.Data;

        tokeniser.error(state);
        assertFalse("Expecting errors to be tracked", tokeniser.errors.isEmpty());
        assertEquals("Expecting error to have the specified state", state, tokeniser.errors.get(0).getState());
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        TokeniserState state = TokeniserState.Data;

        tokeniser.eofError(state);
        assertFalse("Expecting errors to be tracked", tokeniser.errors.isEmpty());
        assertEquals("Expecting error to have the specified state", state, tokeniser.errors.get(0).getState());
        assertTrue("Expecting error to be at end of file", tokeniser.errors.get(0).isEndOfFileError());
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        
        tokeniser.characterReferenceError();
        assertFalse("Expecting errors to be tracked", tokeniser.errors.isEmpty());
        assertTrue("Expecting error to be character reference error", tokeniser.errors.get(0).isCharacterReferenceError());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        assertTrue("Expecting currentNode in HTML namespace", tokeniser.currentNodeInHtmlNS());
    }

}