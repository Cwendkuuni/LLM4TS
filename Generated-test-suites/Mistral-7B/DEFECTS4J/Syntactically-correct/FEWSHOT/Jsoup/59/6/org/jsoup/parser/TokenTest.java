package org.jsoup.parser;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pubsyskey";
        doctype.publicIdentifier.append("publicid");
        doctype.systemIdentifier.append("systemid");
        doctype.forceQuirks = true;
        Doctype resetDoctype = doctype.reset();
        assertInstanceOf(Doctype.class, resetDoctype);
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.tagName = "div";
        tag.newAttribute();
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue.append("class1 class2");
        tag.pendingAttributeValueS = "class3";
        tag.hasPendingAttributeValue = true;
        tag.hasEmptyAttributeValue = true;
        tag.selfClosing = true;
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("id", "id1"));
        tag.attributes = attributes;
        Tag resetTag = tag.reset();
        assertInstanceOf(StartTag.class, resetTag);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.isSelfClosing());
        assertNull(tag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.tagName = "div";
        tag.newAttribute();
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValueS = "class1 class2";
        tag.attributes = new Attributes();
        tag.newAttribute();
        assertEquals(2, tag.attributes.size());
        Attribute attribute1 = tag.attributes.get("class");
        assertEquals("class1 class2", attribute1.getValue());
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("id1");
        tag.newAttribute();
        assertEquals(3, tag.attributes.size());
        Attribute attribute2 = tag.attributes.get("id");
        assertEquals("id1", attribute2.getValue());
    }

    @Test
    public void testTagFinaliseTag() {
        Tag tag = new StartTag();
        tag.tagName = "div";
        tag.newAttribute();
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValueS = "class1 class2";
        tag.attributes = new Attributes();
        tag.finaliseTag();
        assertEquals(1, tag.attributes.size());
        Attribute attribute = tag.attributes.get("class");
        assertEquals("class1 class2", attribute.getValue());
    }

    @Test
    public void testTagName() {
        Tag tag = new StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());

        tag.tagName = "Div";
        tag.normalName = "div";
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());

        tag.tagName = "DIV";
        tag.normalName = "div";
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        StartTag tag = new StartTag();
        assertFalse(tag.isSelfClosing());

        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagToString() {
        StartTag tag = new StartTag();
        tag.tagName = "div";
        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("class", "class1 class2"));
        assertEquals("<div class=\"class1 class2\"></div>", tag.toString());

        tag.tagName = "img";
        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("src", "image.jpg"));
        tag.attributes.put(new Attribute("alt", "image description"));
        assertEquals("<img src=\"image.jpg\" alt=\"image description\"/>", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("This is a comment");
        Comment resetComment = comment.reset();
        assertInstanceOf(Comment.class, resetComment);
        assertEquals("", comment.getData());
        assertTrue(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "Hello World";
        Character resetCharacter = character.reset();
        assertInstanceOf(Character.class, resetCharacter);
        assertNull(character.getData());
    }

    @Test
    public void testCharacterData() {
        Character character = new Character();
        character.data("Hi");
        assertEquals("Hi", character.getData());
    }

    @Test
    public void testTokenTypes() {
        Token token = new Doctype();
        assertTrue(token.isDoctype());

        token = new StartTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());

        token = new EndTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isEndTag());

        token = new Comment();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isComment());

        token = new Character();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());

        token = new EOF();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());
    }
}