import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertNull(tokeniser.read());

        reader = new CharacterReader("<html>");
        tokeniser = new Tokeniser(reader);
        assertEquals(new Token.StartTag("html"), tokeniser.read());
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token token = new Token.StartTag("div");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        String str = "test";
        tokeniser.emit(str);
        assertEquals(new Token.Character(str), tokeniser.read());
    }

    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(new Token.Character(String.valueOf(c)), tokeniser.read());
    }

    @Test
    public void testGetState() {
        TokeniserState state = TokeniserState.Data;
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.TagName;
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName;
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("a"));
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertEquals(start, tag.start);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.tagPending = new Token.StartTag("div");
        tokeniser.emitTagPending();
        assertEquals(tokeniser.tagPending, tokeniser.read());
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        String comment = "Test comment";
        tokeniser.commentPending = new Token.Comment(comment);
        tokeniser.emitCommentPending();
        assertEquals(tokeniser.commentPending, tokeniser.read());
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        String doctype = "html";
        tokeniser.doctypePending = new Token.Doctype(doctype);
        tokeniser.emitDoctypePending();
        assertEquals(tokeniser.doctypePending, tokeniser.read());
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.tagPending = new Token.StartTag("div");
        tokeniser.lastStartTag = new Token.StartTag("div");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        assertEquals(trackErrors, tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("a"));
        TokeniserState state = TokeniserState.TagName;
        tokeniser.error(state);
        assertEquals(1, tokeniser.errors.size());
        assertEquals(state, tokeniser.errors.get(0).getState());
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertEquals(1, tokeniser.errors.size());
        assertEquals(state, tokeniser.errors.get(0).getState());
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("&#;"));
        tokeniser.characterReferenceError();
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testErrorWithMessage() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("a"));
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.errors.size());
        assertEquals(errorMsg, tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}