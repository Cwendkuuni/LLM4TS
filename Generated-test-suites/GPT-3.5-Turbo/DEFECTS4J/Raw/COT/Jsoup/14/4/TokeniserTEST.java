import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null);
    }

    @Test
    public void testRead() {
        Token result = tokeniser.read();
        assertNull(result);
    }

    @Test
    public void testEmit() {
        Token.Character character = new Token.Character("Test Character");
        tokeniser.emit(character);
        Token result = tokeniser.read();
        assertEquals(character, result);
    }

    @Test
    public void testEmitString() {
        String str = "Test String";
        tokeniser.emit(str);
        Token result = tokeniser.read();
        assertEquals(str, result.getData());
    }

    @Test
    public void testEmitChar() {
        char c = 'T';
        tokeniser.emit(c);
        Token result = tokeniser.read();
        assertEquals(Character.toString(c), result.getData());
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.BeforeAttributeName;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character result = tokeniser.consumeCharacterReference(null, true);
        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertNotNull(tokeniser.getTagPending());
    }

    @Test
    public void testEmitTagPending() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.setTagPending(startTag);
        tokeniser.emitTagPending();
        Token result = tokeniser.read();
        assertEquals(startTag, result);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.getCommentPending());
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment comment = new Token.Comment();
        tokeniser.setCommentPending(comment);
        tokeniser.emitCommentPending();
        Token result = tokeniser.read();
        assertEquals(comment, result);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.getDoctypePending());
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.setDoctypePending(doctype);
        tokeniser.emitDoctypePending();
        Token result = tokeniser.read();
        assertEquals(doctype, result);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.getDataBuffer());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.setTagName("test");
        tokeniser.setLastStartTag(startTag);
        tokeniser.createTagPending(false);
        tokeniser.getTagPending().setTagName("test");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        assertEquals(trackErrors, tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.getErrors().size());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.getErrors().size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        assertEquals(1, tokeniser.getErrors().size());
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Test error message";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.getErrors().size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}