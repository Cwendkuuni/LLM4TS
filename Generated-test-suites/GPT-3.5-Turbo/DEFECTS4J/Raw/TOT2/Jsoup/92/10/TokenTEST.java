import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testReset() {
        Token token = new Token.Doctype();
        Token resetToken = token.reset();
        assertNull(resetToken.getName());
        assertNull(resetToken.getPubSysKey());
        assertEquals("", resetToken.getPublicIdentifier());
        assertEquals("", resetToken.getSystemIdentifier());
        assertFalse(resetToken.isForceQuirks());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("<div>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("p");
        assertEquals("</p>", endTag.toString());
    }

    @Test
    public void testCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("Character data");
        assertEquals("Character data", character.getData());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();
        assertNotNull(eof);
    }
}