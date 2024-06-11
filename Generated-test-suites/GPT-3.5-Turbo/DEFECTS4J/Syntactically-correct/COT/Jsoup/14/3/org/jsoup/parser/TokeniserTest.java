import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", token.toString());
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));

        Token token = new Token.Character("test");
        tokeniser.emit(token);

        assertEquals(token, tokeniser.read());
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testReadEmptyString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        Token token = tokeniser.read();

        assertNull(token);
    }

    @Test
    public void testEmitEmptyString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        Token token = new Token.Character("");
        tokeniser.emit(token);

        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.emitTagPending();

        assertEquals(new Token.StartTag("div"), tokeniser.read());
    }

    @Test
    public void testEmitTagPendingWithAttributes() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.tagPending.attributes.put("id", "myDiv");
        tokeniser.emitTagPending();

        assertEquals(new Token.StartTag("div", "id", "myDiv"), tokeniser.read());
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "test";
        tokeniser.emitCommentPending();

        assertEquals(new Token.Comment("test"), tokeniser.read());
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.emitDoctypePending();

        assertEquals(new Token.Doctype("html"), tokeniser.read());
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

        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag("div");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagTokenDifferentTag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag("span");
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));

        tokeniser.setTrackErrors(true);
        assertTrue(tokeniser.isTrackErrors());

        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }
}