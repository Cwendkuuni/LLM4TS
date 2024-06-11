package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Attributes;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EOF;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.TokenType;

import org.junit.Test;

/**
 * Test class for Token class.
 */
public class TokenTest {

    @Test
    public void testTokenType() {
        // Test for TokenType.Doctype
        Doctype doctype = new Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        // Test for TokenType.StartTag
        StartTag startTag = new StartTag("html");
        assertEquals(TokenType.StartTag, startTag.type);

        // Test for TokenType.EndTag
        EndTag endTag = new EndTag("html");
        assertEquals(TokenType.EndTag, endTag.type);

        // Test for TokenType.Comment
        Comment comment = new Comment();
        assertEquals(TokenType.Comment, comment.type);

        // Test for TokenType.Character
        Character character = new Character("A");
        assertEquals(TokenType.Character, character.type);

        // Test for TokenType.EOF
        EOF eof = new EOF();
        assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testDoctype() {
        // Test for getters
        Doctype doctype = new Doctype();
        doctype.getName().equals("doctype");
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        // Test for setting publicIdentifier and systemIdentifier
        Doctype doctype2 = new Doctype();
        doctype2.getName().append("html");
        doctype2.getPublicIdentifier().append("public");
        doctype2.getSystemIdentifier().append("system.html");
        assertEquals("html", doctype2.getName());
        assertEquals("public", doctype2.getPublicIdentifier());
        assertEquals("system.html", doctype2.getSystemIdentifier());
        assertFalse(doctype2.isForceQuirks());

        // Test for setting forceQuirks
        Doctype doctype3 = new Doctype();
        doctype3.getName().append("html");
        doctype3.setForceQuirks(true);
        assertEquals("html", doctype3.getName());
        assertEquals("", doctype3.getPublicIdentifier());
        assertEquals("", doctype3.getSystemIdentifier());
        assertTrue(doctype3.isForceQuirks());
    }

    @Test
    public void testTag() {
        // Test for getters
        Tag tag = new Tag();
        assertNull(tag.tagName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertFalse(tag.isSelfClosing());
        assertNull(tag.attributes);

        // Test for setting tagName
        Tag tag1 = new Tag();
        tag1.name("html");
        assertEquals("html", tag1.tagName);

        // Test for setting selfClosing
        Tag tag2 = new Tag();
        tag2.isSelfClosing(true);
        assertTrue(tag2.isSelfClosing());

        // Test for finaliseTag
        Tag tag3 = new Tag();
        tag3.finaliseTag();
        assertSame(tag3.attributes, new Attributes());

        // Test for newAttribute
        Tag tag4 = new Tag();
        tag4.newAttribute();
        assertSame(tag4.attributes, new Attributes());
        tag4.appendAttributeName("class");
        tag4.appendAttributeValue("test");
        tag4.newAttribute();
        assertNotNull(tag4.attributes);
        assertEquals("class", tag4.attributes.keySet().iterator().next());
        assertEquals("test", tag4.attributes.get("class"));
    }

    @Test
    public void testStartTag() {
        // Test for toString
        StartTag startTag = new StartTag("html");
        assertEquals("<html>", startTag.toString());

        // Test for setting attributes
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("class", "test"));
        StartTag startTag2 = new StartTag("div").attributes(attributes);
        assertEquals("div", startTag2.tagName);
        assertEquals("test", startTag2.attributes.get("class"));
    }

    @Test
    public void testEndTag() {
        // Test for toString
        EndTag endTag = new EndTag("html");
        assertEquals("</html>", endTag.toString());
    }

    @Test
    public void testComment() {
        // Test for getData
        Comment comment = new Comment();
        comment.getData().append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacter() {
        // Test for getData
        Character character = new Character("A");
        assertEquals("A", character.getData());
    }

    @Test
    public void testEOF() {
        // Test for toString
        EOF eof = new EOF();
        assertEquals("", eof.toString());
    }
}