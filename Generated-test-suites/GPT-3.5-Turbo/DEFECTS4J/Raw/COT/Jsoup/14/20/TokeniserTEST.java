import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'c';
        tokeniser.emit(c);
        assertEquals(Character.toString(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Comment);
        assertEquals(TokeniserState.Comment, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());
        assertTrue(tokeniser.reader.isEmpty());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag("div");
        tokeniser.lastStartTag = new Token.StartTag("div");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Comment);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Comment);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Invalid input";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}

