import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        Token token = new Token.Character("character");
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.emit("string");
        assertTrue(tokeniser.charBuffer.length() > 0);
    }

    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.emit('c');
        assertTrue(tokeniser.charBuffer.length() > 0);
    }

    @Test
    public void testGetState() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }

    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNotNull(result);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
        assertEquals(start, tag.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);
        assertEquals(Token.TokenType.StartTag, tokeniser.emitPending.type);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);
        assertEquals(Token.TokenType.Comment, tokeniser.emitPending.type);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);
        assertEquals(Token.TokenType.Doctype, tokeniser.emitPending.type);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = tokeniser.tagPending;
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertTrue(result);
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        boolean result = tokeniser.isTrackErrors();
        assertTrue(result);
    }

    @Test
    public void testSetTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        assertEquals(trackErrors, tokeniser.isTrackErrors());
    }

    @Test
    public void testErrorTokeniserState() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        // Verify error is tracked
    }

    @Test
    public void testEofErrorTokeniserState() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        // Verify error is tracked
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        tokeniser.characterReferenceError();
        // Verify error is tracked
    }

    @Test
    public void testErrorString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        String errorMsg = "Error";
        tokeniser.error(errorMsg);
        // Verify error is tracked
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertTrue(result);
    }
}