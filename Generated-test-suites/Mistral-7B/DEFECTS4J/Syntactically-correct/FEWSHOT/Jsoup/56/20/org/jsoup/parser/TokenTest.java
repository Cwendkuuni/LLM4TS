package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype().name("html");
        assertEquals("html", doctype.name());
        doctype.reset();
        assertNull(doctype.name());
    }

    @Test
    public void testDoctypeGetName() {
        Token doctype = new Token.Doctype().name("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token doctype = new Token.Doctype().publicIdentifier("_publicId_");
        assertEquals("_publicId_", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token doctype = new Token.Doctype().systemIdentifier("_systemId_");
        assertEquals("_systemId_", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token doctype = new Token.Doctype().isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag().name("div");
        assertEquals("div", tag.name());
        tag.reset();
        assertNull(tag.name());
    }

    @Test
    public void testTagName() {
        Token tag = new Token.StartTag().name("div");
        assertEquals("div", tag.name());
        tag.name("p");
        assertEquals("p", tag.name());
    }

    @Test
    public void testTagNormalName() {
        Token tag = new Token.StartTag().name("DIv");
        assertEquals("DIv", tag.name());
        tag.normalName();
        assertEquals("div", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token tag = new Token.StartTag().selfClosing(true);
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token tag = new Token.StartTag().name("img").attributes(new Attributes().put("src", "image.jpg"));
        Attributes attributes = tag.getAttributes();
        assertEquals("src", attributes.getKey(0));
        assertEquals("image.jpg", attributes.getValue(0));
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.StartTag().name("input").attributes(new Attributes().put("type", "text"));
        tag.newAttribute().name("name").value("username");
        Attributes attributes = tag.getAttributes();
        assertEquals("name", attributes.getKey(1));
        assertEquals("username", attributes.getValue(1));
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.StartTag().name("a").attributes(new Attributes().put("href", "http://example.com"));
        tag.finaliseTag();
        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("href", attributes.getKey(0));
        assertEquals("http://example.com", attributes.getValue(0));
        assertEquals("name", attributes.getKey(1));
        assertEquals("", attributes.getValue(1));
    }

    @Test
    public void testTagToString() {
        Token tag = new Token.StartTag().name("img").attributes(new Attributes().put("src", "image.jpg"));
        assertEquals("<img src=\"image.jpg\">", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment().data("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCommentgetData() {
        Token comment = new Token.Comment().data("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCommenttoString() {
        Token comment = new Token.Comment().data("This is a comment.");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character().data("A");
        assertEquals("A", character.getData());
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharactergetData() {
        Token character = new Token.Character().data("A");
        assertEquals("A", character.getData());
    }

    @Test
    public void testCharactertoString() {
        Token character = new Token.Character().data("A");
        assertEquals("A", character.toString());
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        assertTrue(eof.isEOF());
        eof.reset();
        assertSame(eof, eof.reset());
        assertFalse(eof.isEOF());
    }
}