import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emit(tag);
        assertEquals(tag, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(String.valueOf(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = false;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tokeniser.tagPending);
        assertEquals(start, tag.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(tag, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = tokeniser.tagPending;
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertTrue(result);
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = tokeniser.isTrackErrors();
        assertTrue(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testErrorTokeniserState() {
        tokeniser.error(TokeniserState.Doctype);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testEofErrorTokeniserState() {
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testErrorString() {
        String errorMsg = "Invalid character reference";
        tokeniser.error(errorMsg);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertTrue(result);
    }
}