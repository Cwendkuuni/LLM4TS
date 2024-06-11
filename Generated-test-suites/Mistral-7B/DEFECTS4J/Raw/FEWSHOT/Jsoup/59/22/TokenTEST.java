package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();

        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals(Doctype.class, doctype.getClass());

        doctype.reset();
        assertEquals(null, doctype.getName());
        assertEquals(null, doctype.getPubSysKey());
        assertEquals(null, doctype.getPublicIdentifier());
        assertEquals(null, doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.getName("html");
        assertEquals("html", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.setPubSysKey("pubsyskey");
        assertEquals("pubsyskey", doctype.getPubSysKey());

        doctype.setPublicIdentifier("publicid");
        assertEquals("publicid", doctype.getPublicIdentifier());

        doctype.setSystemIdentifier("systemid");
        assertEquals("systemid", doctype.getSystemIdentifier());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        doctype.reset();
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();

        assertEquals(TokenType.StartTag, startTag.type);
        assertEquals(StartTag.class, startTag.getClass());

        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);

        startTag.newAttribute();
        assertNull(startTag.pendingAttributeName);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);

        startTag.newAttribute().name("class").value("foo");
        assertEquals("class", startTag.pendingAttributeName);
        assertEquals("foo", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasEmptyAttributeValue);
        assertTrue(startTag.hasPendingAttributeValue);

        startTag.newAttribute();
        assertNull(startTag.pendingAttributeName);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);

        startTag.finaliseTag();
        assertNull(startTag.pendingAttributeName);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertNull(startTag.attributes);

        startTag.name("html");
        assertEquals("html", startTag.tagName);
        assertEquals("html", startTag.normalName);

        startTag.name("html").attributes(new Attributes());
        assertEquals("html", startTag.tagName);
        assertEquals("html", startTag.normalName);
        assertNotNull(startTag.attributes);

        startTag.selfClosing(true);
        assertTrue(startTag.isSelfClosing());

        startTag.reset();
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();

        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals(EndTag.class, endTag.getClass());

        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);

        endTag.name("html");
        assertEquals("html", endTag.tagName);
        assertEquals("html", endTag.normalName);
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();

        assertEquals(TokenType.Comment, comment.type);
        assertEquals(Comment.class, comment.getClass());

        comment.reset();
        assertNull(comment.data);
        assertFalse(comment.bogus);

        comment.data("This is a comment");
        assertEquals("This is a comment", comment.getData());

        comment.bogus(true);
        assertTrue(comment.isBogus());

        comment.reset();
    }

    @Test
    public void testCharacter() {
        Character character = new Character();

        assertEquals(TokenType.Character, character.type);
        assertEquals(Character.class, character.getClass());

        character.reset();
        assertNull(character.data);

        character.data("A");
        assertEquals("A", character.getData());

        character.data("\n");
        assertEquals("\n", character.getData());

        character.data("\u00A9");
        assertEquals("\u00A9", character.getData());

        character.data("\uD800\uDC00");
        assertEquals("\uD800\uDC00", character.getData());

        character.data("\uD840\uDC00");
        assertEquals("\uD840\uDC00", character.getData());

        character.data("\uD800\uDFFF\uDC00\uDFFF");
        assertEquals("\uD800\uDFFF\uDC00\uDFFF", character.getData());

        character.data("\uFFFE\uFFFF");
        assertEquals("\uFFFE\uFFFF", character.getData());

        character.reset();
    }

    @Test
    public void testReset() {
        Character character = new Character();
        character.data("A");
        character.reset();
        assertNull(character.data);
    }

    @Test
    public void testGetAttributes() {
        StartTag startTag = new StartTag();
        startTag.newAttribute().name("class").value("foo");
        startTag.newAttribute().name("id").value("bar");

        Attributes expected = new Attributes();
        expected.put("class", "foo");
        expected.put("id", "bar");

        assertEquals(expected, startTag.getAttributes());
    }

    @Test
    public void testIsDoctype() {
        Doctype doctype = new Doctype();
        assertTrue(doctype.isDoctype());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        StartTag startTag = new StartTag();
        assertTrue(startTag.isStartTag());

        Doctype doctype = new Doctype();
        assertFalse(doctype.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        EndTag endTag = new EndTag();
        assertTrue(endTag.isEndTag());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isEndTag());
    }

    @Test
    public void testIsComment() {
        Comment comment = new Comment();
        assertTrue(comment.isComment());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isComment());
    }

    @Test
    public void testIsCharacter() {
        Character character = new Character();
        assertTrue(character.isCharacter());

        Doctype doctype = new Doctype();
        assertFalse(doctype.isCharacter());
    }

    @Test
    public void testIsEOF() {
        EOF eof = new EOF();
        assertTrue(eof.isEOF());

        Character character = new Character();
        assertFalse(character.isEOF());
    }
}