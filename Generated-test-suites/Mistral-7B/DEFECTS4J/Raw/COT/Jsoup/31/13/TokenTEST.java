import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        Doctype doctypeWithName = new Doctype();
        doctypeWithName.getName().append("html");
        assertEquals("html", doctypeWithName.getName().toString());

        Doctype doctypeWithPublicIdentifier = new Doctype();
        doctypeWithPublicIdentifier.getPublicIdentifier().append("XHTML");
        assertEquals("XHTML", doctypeWithPublicIdentifier.getPublicIdentifier().toString());

        Doctype doctypeWithSystemIdentifier = new Doctype();
        doctypeWithSystemIdentifier.getSystemIdentifier().append("/path/to/file.html");
        assertEquals("/path/to/file.html", doctypeWithSystemIdentifier.getSystemIdentifier().toString());

        Doctype doctypeWithForceQuirks = new Doctype();
        doctypeWithForceQuirks.isForceQuirks() = true;
        assertTrue(doctypeWithForceQuirks.isForceQuirks());
    }

    @Test
    public void testTag() {
        Tag tag = new Tag();
        assertEquals(TokenType.Tag, tag.type);

        StartTag startTag = new StartTag();
        startTag.name("div");
        assertEquals("div", startTag.name());

        StartTag startTagWithAttributes = new StartTag();
        startTagWithAttributes.name("input");
        startTagWithAttributes.appendAttributeName("type");
        startTagWithAttributes.appendAttributeValue("text");
        startTagWithAttributes.newAttribute();
        assertEquals("text", startTagWithAttributes.getAttributes().get("type").value());

        EndTag endTag = new EndTag();
        endTag.name("p");
        assertEquals("p", endTag.name());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        assertEquals(TokenType.Comment, comment.type);

        Comment commentWithData = new Comment();
        commentWithData.getData().append("This is a comment");
        assertEquals("This is a comment", commentWithData.getData().toString());
    }

    @Test
    public void testCharacter() {
        Character character = new Character("A");
        assertEquals(TokenType.Character, character.type);
        assertEquals("A", character.getData());

        Character characterWithSpecialCharacter = new Character("&amp;");
        assertEquals("&amp;", characterWithSpecialCharacter.getData());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        Token token = new Token();
        fail("Invalid Token created!");
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertEquals(TokenType.EOF, eof.type);
    }
}