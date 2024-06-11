import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("Hello, world!"));
        Token token = tokeniser.read();
        assertEquals(token.type, Token.TokenType.Character);
        assertEquals(((Token.Character)token).getData(), "Hello, world!");
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("Hello, world!"));
        Token token = new Token.Character("Testing");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(emittedToken, token);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("&lt;"));
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(character.charValue(), '<');
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<p>"));
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertEquals(tagPending.tagName, "p");
        assertFalse(tagPending.selfClosing);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<p>"));
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(emittedToken.type, Token.TokenType.StartTag);
        assertEquals(((Token.StartTag)emittedToken).tagName, "p");
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<!-- Comment -->"));
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<!-- Comment -->"));
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertEquals(emittedToken.type, Token.TokenType.Comment);
        assertEquals(((Token.Comment) emittedToken).getData(), " Comment ");
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<!DOCTYPE html>"));
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<!DOCTYPE html>"));
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertEquals(emittedToken.type, Token.TokenType.Doctype);
        assertEquals(((Token.Doctype) emittedToken).getName(), "html");
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<script>"));
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<p></p>"));
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        Token.EndTag endTag = (Token.EndTag) tokeniser.tagPending;
        endTag.attributes.add(new Token.Attribute("id", "test"));
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("Testing"));
        assertTrue(tokeniser.isTrackErrors());
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("Test"));
        tokeniser.error(TokeniserState.Doctype);
        assertEquals(tokeniser.errors.size(), 1);
        assertEquals(tokeniser.errors.get(0).getErrorMessage(), "Unexpected character in input");
        assertEquals(tokeniser.errors.get(0).getPosition(), 0);
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.eofError(TokeniserState.CommentEndBang);
        assertEquals(tokeniser.errors.size(), 1);
        assertEquals(tokeniser.errors.get(0).getErrorMessage(), "Unexpectedly reached end of file (EOF)");
        assertEquals(tokeniser.errors.get(0).getState(), TokeniserState.CommentEndBang);
        assertEquals(tokeniser.errors.get(0).getPosition(), 0);
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("&abc;"));
        tokeniser.characterReferenceError();
        assertEquals(tokeniser.errors.size(), 1);
        assertEquals(tokeniser.errors.get(0).getErrorMessage(), "Invalid character reference");
        assertEquals(tokeniser.errors.get(0).getPosition(), 0);
    }

    @Test
    public void testErrorWithMessage() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("Test"));
        tokeniser.error("Custom error message");
        assertEquals(tokeniser.errors.size(), 1);
        assertEquals(tokeniser.errors.get(0).getErrorMessage(), "Custom error message");
        assertEquals(tokeniser.errors.get(0).getPosition(), 0);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("<html>"));
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}