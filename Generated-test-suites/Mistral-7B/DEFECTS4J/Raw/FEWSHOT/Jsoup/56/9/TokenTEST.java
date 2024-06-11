package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        assertEquals(TokenType.Doctype, doctype.type);

        Doctype resetDoctype = doctype.reset();
        assertFalse(doctype.isForceQuirks());
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals(TokenType.Doctype, resetDoctype.type);
    }

    @Test
    public void testDoctypeName() {
        Doctype doctype = new Doctype().name("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeForceQuirks() {
        Doctype doctype = new Doctype().isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        StartTag startTag = new StartTag();

        StartTag resetStartTag = startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);

        assertEquals(TokenType.StartTag, resetStartTag.type);
    }

    @Test
    public void testTagName() {
        StartTag startTag = new StartTag().name("div");
        assertEquals("div", startTag.name());

        StartTag startTag2 = new StartTag().name("table").name("tr");
        assertEquals("tr", startTag2.name());
    }

    @Test
    public void testTagNormalName() {
        StartTag startTag = new StartTag().name("div");
        assertEquals("div", startTag.normalName());
    }

    @Test
    public void testTagSelfClosing() {
        StartTag startTag = new StartTag().isSelfClosing(true);
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testTagAttributes() {
        StartTag startTag = new StartTag();
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        attributes.put("id", "bar");
        startTag.attributes = attributes;

        assertEquals("class=\"foo\" id=\"bar\"", startTag.toString());
    }

    @Test
    public void testTagNewAttribute() {
        StartTag startTag = new StartTag();
        startTag.newAttribute().name("class").value("foo");
        assertEquals("class=\"foo\"", startTag.toString());
    }

    @Test
    public void testTagNewAttributeBoolean() {
        StartTag startTag = new StartTag().newAttribute().name("disabled");
        assertEquals("disabled", startTag.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testTagNewAttributeNameNull() {
        new StartTag().newAttribute().name(null);
    }

    @Test
    public void testTagGetAttributes() {
        StartTag startTag = new StartTag();
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        attributes.put("id", "bar");
        startTag.attributes = attributes;

        Attributes attributes2 = startTag.getAttributes();

        assertEquals("class=\"foo\" id=\"bar\"", attributes2.toString());
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag startTag = new StartTag();
        startTag.newAttribute().name("class").value("foo");
        startTag.finaliseTag();
        assertFalse(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("foo");
        assertEquals(TokenType.Comment, comment.type);

        Comment resetComment = comment.reset();
        assertEquals("", comment.getData());
        assertTrue(comment.bogus);
        assertEquals(TokenType.Comment, resetComment.type);
    }

    @Test
    public void testCommentData() {
        Comment comment = new Comment().getData("foo");
        assertEquals("foo", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character().data("foo");
        assertEquals("foo", character.getData());
        assertEquals(TokenType.Character, character.type);

        Character resetCharacter = character.reset();
        assertNull(character.getData());
        assertEquals(TokenType.Character, resetCharacter.type);
    }

    @Test
    public void testCharacterData() {
        Character character = new Character().data("foo");
        assertEquals("foo", character.getData());
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        assertEquals(TokenType.EOF, eof.type);

        EOF resetEOF = eof.reset();
        assertEquals(TokenType.EOF, resetEOF.type);
    }
}