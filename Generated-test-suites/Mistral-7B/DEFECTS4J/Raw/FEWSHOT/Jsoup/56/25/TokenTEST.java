package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Doctype token = new Doctype();
        token.getName().equals("");
        token.reset();
        token.getName().equals("");

        token.getName("html");
        token.reset();
        token.getName().equals("");
    }

    @Test
    public void testDoctypeGetters() {
        Doctype token = new Doctype();
        token.getName("html");

        assertEquals("html", token.getName());
        assertEquals("", token.getPublicIdentifier());
        assertEquals("", token.getSystemIdentifier());
        assertFalse(token.isForceQuirks());

        token.getSystemIdentifier("about:blank");
        assertEquals("about:blank", token.getSystemIdentifier());

        token.isForceQuirks(true);
        assertTrue(token.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag token = new StartTag();
        token.reset();
        assertNull(token.tagName);
        assertNull(token.normalName);
        assertNull(token.pendingAttributeName);
        assertNull(token.pendingAttributeValue);
        assertNull(token.pendingAttributeValueS);
        assertFalse(token.hasEmptyAttributeValue);
        assertFalse(token.hasPendingAttributeValue);
        assertFalse(token.selfClosing);
        assertNull(token.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Tag token = new StartTag();
        token.newAttribute();
        assertSame(token.attributes, new Attributes());
    }

    @Test
    public void testTagFinaliseTag() {
        Tag token = new StartTag();
        token.newAttribute();
        token.newAttribute();
        token.finaliseTag();
        assertSame(token.attributes, new Attributes());
    }

    @Test
    public void testTagGetters() {
        Tag token = new StartTag();
        token.name("div");

        assertEquals("div", token.name());
        assertEquals("div", token.normalName());
        assertFalse(token.isSelfClosing());
        assertNull(token.getAttributes());

        token.newAttribute();
        token.newAttribute("class", "foo");
        token.newAttribute("id", "bar");

        assertEquals("div", token.name());
        assertEquals("div", token.normalName());
        assertFalse(token.isSelfClosing());
        assertSame(token.getAttributes(), new Attributes().put("class", "foo").put("id", "bar"));
    }

    @Test
    public void testTagToString() {
        StartTag token = new StartTag().name("div");
        assertEquals("<div>", token.toString());

        StartTag tokenWithAttributes = new StartTag().name("input").attributes(new Attributes().put("type", "text").put("name", "email"));
        assertEquals("<div>", token.toString());
        assertEquals("<input type='text' name='email'>", tokenWithAttributes.toString());
    }

    @Test
    public void testCommentReset() {
        Comment token = new Comment();
        token.reset();
        assertNull(token.getData());
        assertFalse(token.isBogus());

        token.getData("this is a comment");
        assertEquals("this is a comment", token.getData());
        assertFalse(token.isBogus());

        token.isBogus(true);
        assertTrue(token.isBogus());
    }

    @Test
    public void testCommentGetters() {
        Comment token = new Comment();
        token.getData("this is a comment");

        assertEquals("this is a comment", token.getData());
        assertFalse(token.isBogus());

        token.isBogus(true);
        assertTrue(token.isBogus());
    }

    @Test
    public void testCommentToString() {
        Comment token = new Comment().getData("this is a comment");
        assertEquals("<!--this is a comment-->", token.toString());
    }

    @Test
    public void testCharacterReset() {
        Character token = new Character();
        token.reset();
        assertNull(token.getData());

        token.data("A");
        assertEquals("A", token.getData());
    }

    @Test
    public void testCharacterGetters() {
        Character token = new Character();
        token.data("A");

        assertEquals("A", token.getData());
    }

    @Test
    public void testCharacterToString() {
        Character token = new Character().data("A");
        assertEquals("A", token.toString());
    }

    @Test
    public void testEOFReset() {
        EOF token = new EOF();
        token.reset();
        assertTrue(token.isEOF());
    }
}