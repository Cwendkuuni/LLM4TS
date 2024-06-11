package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.pubSysKey = "DTD HTML 4.01";
        doctype.publicIdentifier.append("public \"-//W3C//DTD HTML 4.01//EN\"");
        doctype.systemIdentifier.append("system \"http://www.w3.org/TR/html4/strict.dtd\"");
        doctype.forceQuirks = true;

        Doctype resetDoctype = doctype.reset();

        assertSame(doctype, resetDoctype);
        assertEquals("", doctype.getName());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "class";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.pendingAttributeValueS = "id";
        tag.selfClosing = false;
        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("style", "color: red"));

        Tag resetTag = tag.reset();

        assertSame(tag, resetTag);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("This is a test comment");

        Comment resetComment = comment.reset();

        assertSame(comment, resetComment);
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "Hello World!";

        Character resetCharacter = character.reset();

        assertSame(character, resetCharacter);
        assertNull(character.getData());
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.appendTagName("div");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("test");

        Tag finaliseTag = tag.finaliseTag();

        assertEquals("div", finaliseTag.name());
        assertEquals("div", finaliseTag.normalName());
        assertFalse(finaliseTag.isSelfClosing());
        assertEquals(2, finaliseTag.getAttributes().size());
        Attribute classAttr = finaliseTag.getAttributes().get("class");
        assertEquals("red", classAttr.getValue());
        Attribute idAttr = finaliseTag.getAttributes().get("id");
        assertEquals("test", idAttr.getValue());
    }

    @Test
    public void testTagAppendAttributeValue() {
        Tag tag = new StartTag();
        tag.appendTagName("div");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        tag.appendAttributeValue("-blue");

        Tag finaliseTag = tag.finaliseTag();

        assertEquals("div", finaliseTag.name());
        assertEquals("div", finaliseTag.normalName());
        assertFalse(finaliseTag.isSelfClosing());
        assertEquals(1, finaliseTag.getAttributes().size());
        Attribute classAttr = finaliseTag.getAttributes().get("class");
        assertEquals("red-blue", classAttr.getValue());
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Tag tag = new StartTag();
        tag.appendTagName("div");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.setEmptyAttributeValue();

        Tag finaliseTag = tag.finaliseTag();

        assertEquals("div", finaliseTag.name());
        assertEquals("div", finaliseTag.normalName());
        assertFalse(finaliseTag.isSelfClosing());
        assertEquals(1, finaliseTag.getAttributes().size());
        Attribute classAttr = finaliseTag.getAttributes().get("class");
        assertNull(classAttr.getValue());
    }
}