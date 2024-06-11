import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.setForceQuirks(true);
        assertEquals("html", doctype.getName());
        assertEquals(true, doctype.isForceQuirks());
        doctype.reset();
        assertEquals(null, doctype.getName());
        assertEquals(false, doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.setForceQuirks(true);
        assertEquals("html", doctype.getName());
        assertEquals(true, doctype.isForceQuirks());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("test");
        tag.finaliseTag();
        assertEquals("div", tag.name());
        assertEquals("test", tag.getAttributes().getClass("class"));
        tag.reset();
        assertEquals(null, tag.tagName);
        assertEquals(null, tag.attributes);
    }

    @Test
    public void testTagGetters() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("test");
        tag.finaliseTag();
        assertEquals("div", tag.name());
        assertEquals("test", tag.getAttributes().getClass("class").getValue());
        assertEquals(TokenType.StartTag, tag.type);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData("This is a comment");
        assertEquals("This is a comment", comment.getData());
        comment.reset();
        assertEquals(null, comment.data);
    }

    @Test
    public void testCommentGetters() {
        Comment comment = new Comment();
        comment.getData("This is a comment");
        assertEquals("This is a comment", comment.getData());
        assertEquals(TokenType.Comment, comment.type);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("A");
        assertEquals("A", character.getData());
        character.reset();
        assertEquals(null, character.data);
    }

    @Test
    public void testCharacterGetters() {
        Character character = new Character();
        character.data("A");
        assertEquals("A", character.getData());
        assertEquals(TokenType.Character, character.type);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTokenType() {
        Token invalidToken = new Token();
        fail("Invalid token type: " + invalidToken.tokenType());
    }

    @Test
    public void testResetStringBuilder() {
        StringBuilder sb = new StringBuilder("Test");
        Token.reset(sb);
        assertEquals(0, sb.length());
    }

    @Test
    public void testTokenTypes() {
        Token token = new Doctype();
        assertEquals(TokenType.Doctype, token.type);
        token = new StartTag();
        assertEquals(TokenType.StartTag, token.type);
        token = new EndTag();
        assertEquals(TokenType.EndTag, token.type);
        token = new Comment();
        assertEquals(TokenType.Comment, token.type);
        token = new Character();
        assertEquals(TokenType.Character, token.type);
        token = new EOF();
        assertEquals(TokenType.EOF, token.type);
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        assertEquals(TokenType.EOF, eof.type);
        eof.reset();
        assertEquals(null, eof);
    }
}