import static org.junit.Assert.*;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Normal token read
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("This is a test", ((Token.Character)token).getData());
    }

    @Test
    public void testEmit() {
        // Emitting StartTag token
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<div>"));
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.tagName = "div";
        tag.attributes.put("class", "test-class");

        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("<div class=\"test-class\">", token.toString());
    }

    @Test
    public void testEmit_char() {
        // Emitting Character token
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));
        tokeniser.emit('T');
        tokeniser.emit('e');
        tokeniser.emit('s');
        tokeniser.emit('t');

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Test", ((Token.Character)token).getData());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Acknowledging self closing flag
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testConsumeCharacterReference() {
        // Consuming character reference
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));
        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        // Creating tag pending
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));
        Token.Tag tag = tokeniser.createTagPending(true);

        assertEquals(Token.TokenType.StartTag, tag.type);
        assertTrue(tag.attributes.isEmpty());
    }

    @Test
    public void testCreateCommentPending() {
        // Creating comment pending
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));
        tokeniser.createCommentPending();

        Token.Comment comment = tokeniser.commentPending;
        assertNotNull(comment);
    }

    @Test
    public void testCreateDoctypePending() {
        // Creating doctype pending
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));
        tokeniser.createDoctypePending();

        Token.Doctype doctype = tokeniser.doctypePending;
        assertNotNull(doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        // Creating temporary buffer
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));
        tokeniser.createTempBuffer();

        StringBuilder buffer = tokeniser.dataBuffer;
        assertNotNull(buffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Checking if end tag token is appropriate
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));
        Token.StartTag startTag = new Token.StartTag();

        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = startTag;
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        // Checking if track errors flag is set
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));
        tokeniser.setTrackErrors(true);

        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        // Throwing error while reading
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));

        tokeniser.error(TokeniserState.Doctype);
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).errorMsg);
    }

    @Test
    public void testEofError() {
        // Throwing EOF error while reading
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));

        tokeniser.eofError(TokeniserState.Doctype);
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).errorMsg);
    }

    @Test
    public void testCharacterReferenceError() {
        // Throwing character reference error while reading
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));

        tokeniser.characterReferenceError();
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).errorMsg);
    }

    @Test
    public void testError_custom() {
        // Throwing custom error while reading
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));

        tokeniser.error("Invalid input");
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid input", tokeniser.errors.get(0).errorMsg);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Checking if current node is in HTML namespace
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("This is a test"));

        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}