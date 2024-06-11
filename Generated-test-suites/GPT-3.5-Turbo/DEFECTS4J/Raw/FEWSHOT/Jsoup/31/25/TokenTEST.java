import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token token = new Doctype();
        assertEquals("Doctype", token.tokenType());

        token = new StartTag();
        assertEquals("StartTag", token.tokenType());

        token = new EndTag();
        assertEquals("EndTag", token.tokenType());

        token = new Comment();
        assertEquals("Comment", token.tokenType());

        token = new Character("data");
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        assertFalse(doctype.isForceQuirks());
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());

        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());

        doctype.name.append("name");
        assertEquals("name", doctype.getName());

        doctype.publicIdentifier.append("public");
        assertEquals("public", doctype.getPublicIdentifier());

        doctype.systemIdentifier.append("system");
        assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testTag() {
        Tag tag = new StartTag();
        assertFalse(tag.isSelfClosing());
        assertNull(tag.getAttributes());

        String tagName = "tag";
        tag.name(tagName);
        assertEquals(tagName, tag.name());

        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attr1", "value1"));
        attributes.put(new Attribute("attr2", "value2"));
        tag.attributes = attributes;
        assertEquals(attributes, tag.getAttributes());

        tag.newAttribute();
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);

        tag.appendTagName("end");
        assertEquals(tagName + "end", tag.name());

        tag.appendAttributeName("attr");
        assertEquals("attr", tag.pendingAttributeName);

        tag.appendAttributeName('1');
        assertEquals("attr1", tag.pendingAttributeName);

        tag.appendAttributeValue("val");
        assertEquals("val", tag.pendingAttributeValue.toString());

        tag.appendAttributeValue('u');
        assertEquals("valu", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        assertEquals("", startTag.toString());

        startTag.name("tag");
        assertEquals("<tag>", startTag.toString());

        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("attr", "value"));
        assertEquals("<tag attr=\"value\">", startTag.toString());
    }

    @Test
    public void testEndTag() {
        String tagName = "tag";
        EndTag endTag = new EndTag(tagName);
        assertEquals("</tag>", endTag.toString());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        assertEquals("", comment.getData());

        comment.data.append("data");
        assertEquals("data", comment.getData());
        assertEquals("<!--data-->", comment.toString());
    }

    @Test
    public void testCharacter() {
        String data = "data";
        Character character = new Character(data);
        assertEquals(data, character.getData());
        assertEquals(data, character.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}