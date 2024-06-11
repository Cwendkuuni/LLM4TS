
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());

        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());

        doctype.name.append("html");
        assertEquals("html", doctype.getName());

        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());

        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testTag() {
        Token.Tag tag = new Token.Tag() {};
        assertFalse(tag.isSelfClosing());
        assertNull(tag.tagname());

        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());

        tag.tagname("div");
        assertEquals("div", tag.tagname());

        Attributes attributes = new Attributes();
        attributes.put(new Attribute("id", "someid"));
        tag.attributes = attributes;
        assertEquals(attributes, tag.getAttributes());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTag.type);

        startTag = new Token.StartTag("div");
        assertEquals(TokenType.StartTag, startTag.type);
        assertEquals("div", startTag.tagname());

        startTag = new Token.StartTag("div", new Attributes());
        assertEquals(TokenType.StartTag, startTag.type);
        assertEquals("div", startTag.tagname());
        assertEquals(new Attributes(), startTag.getAttributes());

        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.type);

        endTag = new Token.EndTag("div");
        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals("div", endTag.tagname());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);
        assertNull(comment.getData());

        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("some character data");
        assertEquals(TokenType.Character, character.type);
        assertEquals("some character data", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.type);
    }
}
