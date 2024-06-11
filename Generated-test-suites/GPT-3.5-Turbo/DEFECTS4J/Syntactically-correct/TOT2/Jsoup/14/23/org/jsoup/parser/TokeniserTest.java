import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader("Test input"));
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character character = tokeniser.consumeCharacterReference('a', false);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = new Token.StartTag();
        tokeniser.tagPending = tag;
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
        Token.Comment comment = new Token.Comment();
        tokeniser.commentPending = comment;
        tokeniser.emitCommentPending();
        assertEquals(comment, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;
        tokeniser.emitDoctypePending();
        assertEquals(doctype, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        tokeniser.tagPending = startTag;
        tokeniser.lastStartTag = startTag;
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}