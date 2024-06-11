import static org.junit.Assert.*;
import org.junit.Test;

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
        assertEquals(token, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charsString);
        assertNull(tokeniser.charsBuilder);
    }

    @Test
    public void testEmitCharArray() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        assertEquals("test", tokeniser.charsString);
    }

    @Test
    public void testEmitIntArrya() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        int[] codepoints = {116, 101, 115, 116};
        tokeniser.emit(codepoints);
        assertEquals("test", tokeniser.charsString);
    }

    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        char c = 't';
        tokeniser.emit(c);
        assertEquals("t", tokeniser.charsString);
    }

    @Test
    public void testGetState() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertEquals(tokeniser.state, tokeniser.getState());
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
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        int[] result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
        assertNotNull(tokeniser.tagPending);
        assertEquals(tag, tokeniser.tagPending);
        assertEquals(start ? "StartTag" : "EndTag", tag.type.name());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.tagPending = new Token.Tag();
        tokeniser.emitTagPending();
        assertEquals(tokeniser.tagPending, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.tagPending.name(), tokeniser.lastStartTag);
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
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
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
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
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
        tokeniser.lastStartTag = "start";
        assertTrue(tokeniser.isAppropriateEndTagToken());
        tokeniser.lastStartTag = null;
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.lastStartTag = "start";
        assertEquals("start", tokeniser.appropriateEndTagName());
        tokeniser.lastStartTag = null;
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorTokeniserState() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofErrorTokeniserState() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String message = "invalid";
        tokeniser.characterReferenceError(message);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String errorMsg = "error";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String unescaped = "unescaped";
        assertEquals(unescaped, tokeniser.unescapeEntities(false));
    }

}