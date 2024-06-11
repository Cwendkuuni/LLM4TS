package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {
    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        assertFalse(doctype.isForceQuirks());
        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());
        assertEquals("div", startTag.name());
        assertTrue(startTag.name("span").isStartTag());
        assertTrue(startTag.isStartTag());

        Token.Attributes attributes = new Token.Attributes();
        Token.Attribute attribute = new Token.Attribute("id", "myDiv");
        attributes.put(attribute);
        assertEquals(attributes.toString(), startTag.attributes.toString());

        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assertEquals("</div>", endTag.toString());
        assertEquals("div", endTag.name());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("text");
        assertEquals("text", character.getData());
        assertEquals("text", character.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}