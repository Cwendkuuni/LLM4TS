import org.junit.*;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        char c = 't';
        tokeniser.emit(c);
        assertEquals(c, tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testGetState() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }

    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.state);
    }

    @Test
    public void testAdvanceTransition() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Character characterRef = tokeniser.consumeCharacterReference(null, false);
        assertNull(characterRef);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        boolean start = true;
        Token.Tag tagPending = tokeniser.createTagPending(start);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        assertNotNull(tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        assertNotNull(tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        assertNotNull(tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.EndTag();
        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertTrue(isAppropriateEndTagToken);
    }

    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";
        String appropriateEndTagName = tokeniser.appropriateEndTagName();
        assertEquals(tokeniser.lastStartTag.tagName, appropriateEndTagName);
    }

    @Test
    public void testErrorState() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertTrue(errors.hasError());
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertTrue(errors.hasError());
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String message = "test";
        tokeniser.characterReferenceError(message);
        assertTrue(errors.hasError());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String errorMsg = "test";
        tokeniser.error(errorMsg);
        assertTrue(errors.hasError());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(currentNodeInHtmlNS);
    }
}