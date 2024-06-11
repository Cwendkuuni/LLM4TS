package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("doctype");

        doctype.reset();

        assertEquals("doctype", doctype.tokenType());
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().append("html");
        doctype.getPublicIdentifier().append("Public ID:");
        doctype.getSystemIdentifier().append("System ID:");
        doctype.isForceQuirks(true);

        assertEquals("doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("Public ID:System ID:", doctype.getPublicIdentifier().toString());
        assertEquals("System ID:", doctype.getSystemIdentifier().toString());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        AbstractTag tag = new StartTag();
        tag.tagName = "tagname";
        tag.normalName = "tagname";
        tag.selfClosing = false;

        tag.reset();

        assertEquals("tag", tag.tokenType());
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertFalse(tag.selfClosing);
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagGetters() {
        StartTag tag = new StartTag();
        tag.tagName = "tagname";
        tag.normalName = "tagname";
        tag.selfClosing = false;
        tag.attributes = new Attributes();
        tag.attributes.put("attr", new Attribute("attr", "attrValue"));

        assertEquals("tag", tag.tokenType());
        assertEquals("tagname", tag.name());
        assertEquals("tagname", tag.normalName());
        assertFalse(tag.isSelfClosing());
        assertEquals("attr=\"attrValue\"", tag.getAttributes().toString());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data = new StringBuilder("commentData");

        comment.reset();

        assertEquals("comment", comment.tokenType());
        assertNull(comment.data);
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetter() {
        Comment comment = new Comment();
        comment.data = new StringBuilder("commentData");

        assertEquals("comment", comment.tokenType());
        assertEquals("commentData", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "characterData";

        character.reset();

        assertEquals("character", character.tokenType());
        assertNull(character.data);
    }

    @Test
    public void testCharacterGetter() {
        Character character = new Character();
        character.data = "characterData";

        assertEquals("character", character.tokenType());
        assertEquals("characterData", character.getData());
    }
}