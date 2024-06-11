package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        assertEquals("html", doctype.getName());

        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeAttributes() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.getPublicIdentifier().equals("HTML 4.01");
        doctype.getSystemIdentifier().equals("about:blank");
        doctype.isForceQuirks(false);

        assertEquals("html", doctype.getName());
        assertEquals("HTML 4.01", doctype.getPublicIdentifier());
        assertEquals("about:blank", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new Tag();
        tag.name("div");
        tag.getAttributes() == null;

        assertEquals("div", tag.name());
        assertNull(tag.getAttributes());

        tag.reset();
        assertNull(tag.name());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new Tag();

        tag.newAttribute();
        assertNull(tag.getAttributes());

        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        tag.hasPendingAttributeValue = true;
        tag.newAttribute();

        Attributes attributes = tag.getAttributes();
        Attribute attribute = attributes.get("class");
        assertEquals("red", attribute.getValue());

        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag startTag = new StartTag();
        startTag.name("div");

        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("red");
        startTag.newAttribute();

        startTag.finaliseTag();
        Attributes attributes = startTag.getAttributes();
        Attribute attribute = attributes.get("class");
        assertEquals("red", attribute.getValue());

        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag = new Tag();
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());

        tag.reset();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        StartTag startTag = new StartTag();
        startTag.name("div");

        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("red");
        startTag.newAttribute();

        Attributes attributes = startTag.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get("class");
        assertEquals("red", attribute.getValue());

        startTag.reset();
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testTagToString() {
        StartTag startTag = new StartTag();
        startTag.name("div");

        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("red");
        startTag.newAttribute();

        String tagString = startTag.toString();
        assertEquals("<div class=\"red\"></div>", tagString);

        startTag.reset();
        assertNull(startTag.toString());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("This is a comment");

        assertEquals("This is a comment", comment.getData());

        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment();
        comment.getData().equals("<!-- This is a comment -->");

        String commentString = comment.toString();
        assertEquals("<!-- This is a comment -->", commentString);

        comment.reset();
        assertNull(comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals("A");

        assertEquals("A", character.getData());

        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterToString() {
        Character character = new Character();
        character.data("A");

        String characterString = character.toString();
        assertEquals("A", characterString);

        character.reset();
        assertNull(character.toString());
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();

        assertSame(eof, eof.reset());
    }
}