import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.EOF;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        TokenType type = TokenType.Doctype;
        assertEquals("Doctype", type.name());
        assertEquals(0, type.ordinal());

        type = TokenType.StartTag;
        assertEquals("StartTag", type.name());
        assertEquals(1, type.ordinal());

        type = TokenType.EndTag;
        assertEquals("EndTag", type.name());
        assertEquals(2, type.ordinal());

        type = TokenType.Comment;
        assertEquals("Comment", type.name());
        assertEquals(3, type.ordinal());

        type = TokenType.Character;
        assertEquals("Character", type.name());
        assertEquals(4, type.ordinal());

        type = TokenType.EOF;
        assertEquals("EOF", type.name());
        assertEquals(5, type.ordinal());
    }

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.forceQuirks = false;

        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        StartTag startTag = new StartTag();
        startTag.name("div");
        assertFalse(startTag.isSelfClosing());
        assertEquals("div", startTag.name());

        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());

        Attribute attribute = new Attribute("id", "my-div");
        Attributes attributes = new Attributes();
        attributes.put(attribute);
        startTag.attributes = attributes;

        assertEquals("my-div", startTag.getAttributes().get("id"));
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        endTag.name("div");
        assertEquals("div", endTag.name());

        String expectedToString = "</div>";
        assertEquals(expectedToString, endTag.toString());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        comment.data.append("This is a comment.");
        assertEquals("This is a comment.", comment.getData());

        String expectedToString = "<!--This is a comment.-->";
        assertEquals(expectedToString, comment.toString());
    }

    @Test
    public void testCharacter() {
        Character character = new Character("A");
        assertEquals("A", character.getData());

        String expectedToString = "A";
        assertEquals(expectedToString, character.toString());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertEquals(TokenType.EOF, eof.type);
    }
}