import org.junit.Test;

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
        Token token = new Token();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String str = "test";
        tokeniser.emit(str);
        assertEquals(tokeniser.read().data(), str);
    }

    @Test
    public void testEmitCharArray() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        assertEquals(tokeniser.read().data(), new String(chars));
    }

    @Test
    public void testEmitIntArral() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        int[] codepoints = {116, 101, 115, 116};
        tokeniser.emit(codepoints);
        assertEquals(tokeniser.read().data(), new String(codepoints, 0, codepoints.length));
    }

    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        char c = 't';
        tokeniser.emit(c);
        assertEquals(tokeniser.read().data(), String.valueOf(c));
    }

    @Test
    public void testGetState() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertEquals(tokeniser.getState(), TokeniserState.Data);
    }

    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState state = TokeniserState.ScriptData;
        tokeniser.transition(state);
        assertEquals(tokeniser.getState(), state);
    }

    @Test
    public void testAdvanceTransition() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState state = TokeniserState.ScriptData;
        tokeniser.advanceTransition(state);
        assertEquals(tokeniser.getState(), state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        int[] codeRef = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(codeRef);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
        assertEquals(tokeniser.tagPending, tag);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(tokeniser.read().type, Token.TokenType.StartTag);
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
        tokeniser.createCommentPending();
        tokeniser.commentPending.append("test");
        tokeniser.emitCommentPending();
        assertEquals(tokeniser.read().data(), tokeniser.commentPending.data());
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
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.append("test");
        tokeniser.emitDoctypePending();
        assertEquals(tokeniser.read().data(), tokeniser.doctypePending.data());
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
        tokeniser.lastStartTag = "div";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.lastStartTag = "div";
        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertTrue(tokeniser.errors.hasErrors());
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertTrue(tokeniser.errors.hasErrors());
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        assertTrue(tokeniser.errors.hasErrors());
    }

    @Test
    public void testErrorWithMessage() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        assertTrue(tokeniser.errors.hasErrors());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String str = "test &amp; test";
        tokeniser.reader = new CharacterReader(str);
        assertEquals("test & test", tokeniser.unescapeEntities(false));
    }
}