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
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        assertEquals("Doctype", doctype.tokenType());

        doctype.getName().append("html");
        assertEquals("html", doctype.getName());

        doctype.getPublicIdentifier().append("public id");
        assertEquals("public id", doctype.getPublicIdentifier());

        doctype.getSystemIdentifier().append("system id");
        assertEquals("system id", doctype.getSystemIdentifier());

        doctype.isForceQuirks();
        assertFalse(doctype.isForceQuirks());

        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertEquals(doctype, resetDoctype);
    }

    @Test
    public void testTagToken() {
        Tag tag = new Tag();
        assertEquals("Tag", tag.tokenType());

        tag.name("div");
        assertEquals("div", tag.name());
        tag.normalName("div");

        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());

        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");

        tag.appendAttributeName("id");
        tag.appendAttributeValue("123");

        tag.newAttribute();
        tag.appendAttributeName("style");
        tag.appendAttributeValue("color: red;");

        tag.finaliseTag();

        Attributes attributes = tag.getAttributes();
        assertEquals(3, attributes.size());

        Attribute attrClass = attributes.get("class");
        assertEquals("test", attrClass.getValue());

        Attribute attrId = attributes.get("id");
        assertEquals("123", attrId.getValue());

        Attribute attrStyle = attributes.get("style");
        assertEquals("color: red;", attrStyle.getValue());

        Tag resetTag = tag.reset();
        assertEquals(tag, resetTag);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTagToken() {
        Tag tag = new Tag();
        tag.tagName = null;
        tag.newAttribute();
        tag.appendAttributeName("name");
        tag.appendAttributeValue("value");
        tag.attributes = null;
        tag.finaliseTag();
        tag.getAttributes();
    }

    @Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();
        assertEquals("StartTag", startTag.tokenType());

        startTag.name("p");
        startTag.attributes = new Attributes();

        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("test");

        startTag.newAttribute();
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("456");

        startTag.newAttribute();
        startTag.appendAttributeName("style");
        startTag.appendAttributeValue("color: blue;");

        startTag.finaliseTag();

        Attributes attributes = startTag.getAttributes();
        assertEquals(3, attributes.size());

        Attribute attrClass = attributes.get("class");
        assertEquals("test", attrClass.getValue());

        Attribute attrId = attributes.get("id");
        assertEquals("456", attrId.getValue());

        Attribute attrStyle = attributes.get("style");
        assertEquals("color: blue;", attrStyle.getValue());

        String tagName = startTag.toString();
        assertEquals("<p class=\"test\" id=\"456\" style=\"color: blue;\"></p>", tagName);

        StartTag resetStartTag = startTag.reset();
        assertEquals(startTag, resetStartTag);
    }

    @Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();
        assertEquals("EndTag", endTag.tokenType());

        endTag.name("p");

        String tagName = endTag.toString();
        assertEquals("</p>", tagName);

        EndTag resetEndTag = endTag.reset();
        assertEquals(endTag, resetEndTag);
    }

    @Test
    public void testCommentToken() {
        Comment comment = new Comment();
        assertEquals("Comment", comment.tokenType());

        comment.getData().append("This is a comment.");

        String data = comment.getData();
        assertEquals("This is a comment.", data);

        String commentString = comment.toString();
        assertEquals("<!--This is a comment.-->", commentString);

        Comment resetComment = comment.reset();
        assertEquals(comment, resetComment);
    }

    @Test
    public void testCharacterToken() {
        Character character = new Character();
        assertEquals("Character", character.tokenType());

        character.data("A");

        String data = character.getData();
        assertEquals("A", data);

        Character resetCharacter = character.reset();
        assertEquals(character, resetCharacter);
    }

    @Test
    public void testEOFToken() {
        EOF eof = new EOF();
        assertEquals("EOF", eof.tokenType());

        EOF resetEOF = eof.reset();
        assertEquals(eof, resetEOF);
    }
}