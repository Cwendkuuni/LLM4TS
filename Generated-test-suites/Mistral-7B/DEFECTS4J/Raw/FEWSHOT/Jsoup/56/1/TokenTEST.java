package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.reset();
        assertEquals(null, doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());
        doctype.publicIdentifier("_public");
        assertEquals("_public", doctype.getPublicIdentifier());
        doctype.systemIdentifier("_system");
        assertEquals("_system", doctype.getSystemIdentifier());
        doctype.forceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        StartTag tag = new StartTag();
        tag.name("tag");
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("foo");
        tag.newAttribute();
        assertEquals("class", tag.pendingAttributeName);
        assertEquals("foo", tag.getAttributes().getValue("class"));
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("foo");
        tag.newAttribute();
        tag.finaliseTag();
        assertEquals("class", tag.getAttributes().getName(0));
        assertEquals("foo", tag.getAttributes().getValue("class"));
    }

    @Test
    public void testTagName() {
        StartTag tag = new StartTag();
        tag.name("Tag");
        tag.name("tag");
        assertEquals("Tag", tag.name());
        assertEquals("tag", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        StartTag tag = new StartTag();
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
        StartTag tag2 = new StartTag();
        assertFalse(tag2.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        StartTag tag = new StartTag();
        tag.name("tag");
        tag.appendAttributeName("class");
        tag.appendAttributeValue("foo");
        tag.newAttribute();
        Attributes attributes = tag.getAttributes();
        assertEquals("class", attributes.getName(0));
        assertEquals("foo", attributes.getValue("class"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testTagToString() {
        StartTag tag = new StartTag();
        tag.name("tag");
        tag.appendAttributeName("class");
        tag.appendAttributeValue("foo");
        tag.newAttribute();
        assertEquals("<tag class=\"foo\"></tag>", tag.toString());
    }

    @Test
    public void testEndTagToString() {
        EndTag endTag = new EndTag();
        endTag.name("tag");
        assertEquals("<tag/>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data("data");
        comment.reset();
        assertEquals(null, comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetters() {
        Comment comment = new Comment();
        comment.data("data");
        assertEquals("data", comment.getData());
        comment.bogus = true;
        assertTrue(comment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("data");
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterGetters() {
        Character character = new Character();
        character.data("data");
        assertEquals("data", character.getData());
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        assertSame(eof, eof.reset());
    }

    @Test(expected = IllegalStateException.class)
    public void testTagError() {
        StartTag tag = new StartTag();
        tag.name("tag");
        tag.appendAttributeName("class");
        tag.appendAttributeValue("foo");
        tag.newAttribute();
        tag.appendAttributeName("class");
    }
}