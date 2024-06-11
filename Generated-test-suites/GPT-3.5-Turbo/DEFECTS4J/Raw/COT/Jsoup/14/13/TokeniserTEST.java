
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertNull(tokeniser.emitPending);
        assertFalse(tokeniser.isEmitPending);

        Token token = new Token();
        token.type = Token.TokenType.StartTag;
        tokeniser.emit(token);

        assertEquals(token, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.emit("test");

        assertEquals("test", tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.emit('c');

        assertEquals("c", tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        Tokeniser tokeniser = new Tokeniser(null);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.transition(TokeniserState.TagName);

        assertEquals(TokeniserState.TagName, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader(null);
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.advanceTransition(TokeniserState.TagName);

        assertEquals(TokeniserState.TagName, tokeniser.getState());
        assertTrue(reader.isAdvanced());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(null);
        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null);
        Token.Tag tagPending = tokeniser.createTagPending(true);

        assertNotNull(tagPending);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null);
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tagPending.tagName = "test";
        tokeniser.emitTagPending();

        assertEquals(tagPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null);
        Token.Comment commentPending = new Token.Comment();
        tokeniser.commentPending = commentPending;
        tokeniser.emitCommentPending();

        assertEquals(commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null);
        Token.Doctype doctypePending = new Token.Doctype();
        tokeniser.doctypePending = doctypePending;
        tokeniser.emitDoctypePending();

        assertEquals(doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null);
        Token.StartTag lastStartTag = new Token.StartTag();
        lastStartTag.tagName = "test";
        tokeniser.tagPending = lastStartTag;

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(null);

        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.setTrackErrors(false);

        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.error(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
    }

    private class CharacterReader extends org.jsoup.parser.CharacterReader {
        CharacterReader(java.util.List<Character> characters) {
            super(characters);
        }

        @Override
        char current() {
            return 0;
        }
    }
}

