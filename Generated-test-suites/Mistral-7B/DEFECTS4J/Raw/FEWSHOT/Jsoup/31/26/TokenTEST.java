package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype token = new Doctype();
        token.getName().equals("doctype");
        token.getPublicIdentifier().equals("");
        token.getSystemIdentifier().equals("");
        assertFalse(token.isForceQuirks());

        Doctype tokenWithName = new Doctype();
        tokenWithName.name("html");
        tokenWithName.getName().equals("doctype");
        tokenWithName.getName().equals("html");
        tokenWithName.getPublicIdentifier().equals("");
        tokenWithName.getSystemIdentifier().equals("");
        assertFalse(tokenWithName.isForceQuirks());

        Doctype tokenWithNameAndPublicId = new Doctype();
        tokenWithNameAndPublicId.name("html");
        tokenWithNameAndPublicId.getPublicIdentifier().equals("PUBLIC");
        tokenWithNameAndPublicId.getSystemIdentifier().equals("www.w3.org/TR/html4/strict.dtd");
        assertFalse(tokenWithNameAndPublicId.isForceQuirks());

        Doctype tokenWithNamePublicIdAndSystemId = new Doctype();
        tokenWithNamePublicIdAndSystemId.name("html");
        tokenWithNamePublicIdAndSystemId.getPublicIdentifier().equals("PUBLIC");
        tokenWithNamePublicIdAndSystemId.getSystemIdentifier().equals("www.w3.org/TR/html4/strict.dtd");
        tokenWithNamePublicIdAndSystemId.isForceQuirks(true);
    }

    @Test
    public void testTag() {
        Tag tag = new Tag();
        tag.tagName = "div";
        tag.getName().equals("tag");
        tag.getName().equals("div");
        assertFalse(tag.isSelfClosing());
        assertNull(tag.getAttributes());

        Tag tagWithAttributes = new Tag();
        tagWithAttributes.tagName = "img";
        tagWithAttributes.attributes = new Attributes();
        tagWithAttributes.newAttribute();
        tagWithAttributes.appendAttributeName("src");
        tagWithAttributes.appendAttributeValue("image.jpg");
        tagWithAttributes.newAttribute();
        tagWithAttributes.appendAttributeName("alt");
        tagWithAttributes.appendAttributeValue("Alternative text");
        assertEquals(tagWithAttributes.getAttributes().getClass().getName(), "org.jsoup.nodes.Attributes");
        assertEquals(tagWithAttributes.getAttributes().size(), 2);

        tagWithAttributes.finaliseTag();
        assertNull(tagWithAttributes.getAttributes());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        startTag.tagName = "p";
        assertEquals(startTag.getClass().getSimpleName(), "StartTag");
        assertEquals(startTag.name(), "p");
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());

        StartTag startTagWithAttributes = new StartTag();
        startTagWithAttributes.tagName = "input";
        startTagWithAttributes.newAttribute();
        startTagWithAttributes.appendAttributeName("type");
        startTagWithAttributes.appendAttributeValue("text");
        startTagWithAttributes.newAttribute();
        startTagWithAttributes.appendAttributeName("name");
        startTagWithAttributes.appendAttributeValue("username");
        assertEquals(startTagWithAttributes.getAttributes().size(), 2);

        StartTag startTagWithName = new StartTag("p");
        assertEquals(startTagWithName.getClass().getSimpleName(), "StartTag");
        assertEquals(startTagWithName.name(), "p");
        assertNull(startTagWithName.getAttributes());

        StartTag startTagWithNameAndAttributes = new StartTag("p", new Attributes());
        assertEquals(startTagWithNameAndAttributes.getClass().getSimpleName(), "StartTag");
        assertEquals(startTagWithNameAndAttributes.name(), "p");
        assertNotNull(startTagWithNameAndAttributes.getAttributes());
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        endTag.tagName = "p";
        assertEquals(endTag.getClass().getSimpleName(), "EndTag");
        assertEquals(endTag.name(), "p");

        EndTag endTagWithName = new EndTag("p");
        assertEquals(endTagWithName.getClass().getSimpleName(), "EndTag");
        assertEquals(endTagWithName.name(), "p");
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        comment.getData().equals("");
        assertEquals(comment.getClass().getSimpleName(), "Comment");
        assertEquals(comment.toString(), "<!---->");

        Comment commentWithData = new Comment();
        commentWithData.getData().equals("This is a comment");
        assertEquals(commentWithData.toString(), "<!--This is a comment-->");
    }

    @Test
    public void testCharacter() {
        Character character = new Character("&amp;");
        assertEquals(character.getClass().getSimpleName(), "Character");
        assertEquals(character.toString(), "&amp;");

        Character characterWithData = new Character("\u00A9");
        assertEquals(characterWithData.getClass().getSimpleName(), "Character");
        assertEquals(characterWithData.toString(), "\u00A9");
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertEquals(eof.getClass().getSimpleName(), "EOF");
    }
}