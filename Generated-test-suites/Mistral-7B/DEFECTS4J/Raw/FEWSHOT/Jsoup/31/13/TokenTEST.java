package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.getPublicIdentifier().equals("");
        doctype.getSystemIdentifier().equals("");
        assertFalse(doctype.isForceQuirks());

        Doctype doctype2 = new Doctype();
        doctype2.getName().equals("html");
        doctype2.getPublicIdentifier().equals("--public-id-");
        doctype2.getSystemIdentifier().equals("--system-id-");
        assertTrue(doctype2.isForceQuirks());
    }

    @Test
    public void testTag_StartTag() {
        StartTag startTag = new StartTag("html");
        startTag.isSelfClosing().equals(false);
        startTag.getAttributes().size().equals(0);

        StartTag startTag2 = new StartTag("html", new Attributes().put("class", "test"));
        startTag2.isSelfClosing().equals(false);
        startTag2.getAttributes().size().equals(1);
        assertEquals("class", startTag2.getAttributes().keySet().iterator().next());
        assertEquals("test", startTag2.getAttributes().get("class").getValue());

        StartTag startTag3 = new StartTag("img", new Attributes().put("src", "test.jpg").put("alt", "test image"));
        startTag3.isSelfClosing().equals(false);
        startTag3.getAttributes().size().equals(2);
        assertEquals("src", startTag3.getAttributes().keySet().iterator().next());
        assertEquals("test.jpg", startTag3.getAttributes().get("src").getValue());
        assertEquals("alt", startTag3.getAttributes().keySet().iterator().next());
        assertEquals("test image", startTag3.getAttributes().get("alt").getValue());
    }

    @Test
    public void testTag_EndTag() {
        EndTag endTag = new EndTag("html");
        endTag.isSelfClosing().equals(false);
        assertNull(endTag.getAttributes());

        EndTag endTag2 = new EndTag("p");
        endTag2.isSelfClosing().equals(false);
        assertNull(endTag2.getAttributes());

        EndTag endTag3 = new EndTag("img");
        endTag3.isSelfClosing().equals(true);
        assertNull(endTag3.getAttributes());
    }

    @Test
    public void testTag_Comment() {
        Comment comment = new Comment();
        comment.getData().equals("");

        Comment comment2 = new Comment("--test--");
        comment2.getData().equals("test");
    }

    @Test
    public void testTag_Character() {
        Character character = new Character("A");
        character.getData().equals("A");

        Character character2 = new Character("&nbsp;");
        character2.getData().equals(" ");

        Character character3 = new Character("&lt;");
        character3.getData().equals("<");

        Character character4 = new Character("&gt;");
        character4.getData().equals(">");
    }

    @Test
    public void testTag_EOF() {
        EOF eof = new EOF();
        assertTrue(eof.isEOF());
    }
}