package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        doctype.reset();
        assertEquals(TokenType.Doctype, doctype.type);

        doctype.name("html");
        assertEquals("html", doctype.getName());
        assertEquals("html", doctype.tokenType());

        doctype.pubSysKey("DTD XHTML 1.0");
        assertEquals("DTD XHTML 1.0", doctype.getPubSysKey());

        doctype.publicIdentifier("Public ID: -//W3C//DTD XHTML 1.0//EN");
        assertEquals("Public ID: -//W3C//DTD XHTML 1.0//EN", doctype.getPublicIdentifier());

        doctype.systemIdentifier("/path/to/file.xhtml");
        assertEquals("/path/to/file.xhtml", doctype.getSystemIdentifier());

        doctype.forceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.Tag();
        assertEquals(TokenType.Tag, tag.type);

        tag.reset();
        assertEquals(TokenType.Tag, tag.type);

        tag.name("div");
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());

        tag.attributes = new Attributes();
        tag.attributes.put("class", "foo");
        tag.attributes.put("id", "bar");

        assertEquals("div class=foo id=bar", tag.toString());

        tag.reset();
        assertEquals("", tag.tagName);
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
    public void testStartTag() {
        Token startTag = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTag.type);

        startTag.reset();
        assertEquals(TokenType.StartTag, startTag.type);

        startTag.name("div");
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());

        startTag.attributes = new Attributes();
        startTag.attributes.put("class", "foo");
        startTag.attributes.put("id", "bar");

        startTag.nameAttr("class", startTag.attributes);
        assertEquals("div class=foo", startTag.toString());

        startTag.reset();
        assertEquals("", startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.type);

        endTag.reset();
        assertEquals(TokenType.EndTag, endTag.type);

        endTag.name("div");
        assertEquals("div", endTag.name());
        assertEquals("div", endTag.normalName());

        endTag.reset();
        assertEquals("", endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);

        comment.reset();
        assertEquals(TokenType.Comment, comment.type);

        comment.data("This is a comment");
        assertEquals("This is a comment", comment.getData());
        assertEquals("This is a comment", comment.toString());

        comment.bogus = true;
        assertTrue(comment.isBogus());

        comment.reset();
        assertNull(comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character();
        character.data("A character");
        assertEquals("A character", character.getData());
        assertEquals("A character", character.toString());

        Token cdata = new Token.CData("CDATA[A character]");
        assertEquals("CDATA[A character]", cdata.toString());

        character.reset();
        assertNull(character.getData());
    }
}