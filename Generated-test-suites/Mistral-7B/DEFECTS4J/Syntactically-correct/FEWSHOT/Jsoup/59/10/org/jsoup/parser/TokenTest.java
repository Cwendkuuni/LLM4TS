package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.pubSysKey = "pubsyskey";
        doctype.publicIdentifier.append("publicid");
        doctype.systemIdentifier.append("systemid");
        doctype.forceQuirks = true;

        Doctype resetDoctype = doctype.reset();

        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.pubSysKey);
        assertNull(resetDoctype.publicIdentifier);
        assertNull(resetDoctype.systemIdentifier);
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name().equals("div");
        tag.attributes = new Attributes();

        Tag resetTag = tag.reset();

        assertEquals(TokenType.StartTag, resetTag.type);
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertNull(resetTag.pendingAttributeValue);
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.isSelfClosing());
        assertNull(resetTag.attributes);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("This is a comment");

        Comment resetComment = comment.reset();

        assertEquals(TokenType.Comment, resetComment.type);
        assertNull(resetComment.getData());
        assertFalse(resetComment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals("!");

        Character resetCharacter = character.reset();

        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(resetCharacter.getData());
    }

    @Test
    public void testTagName() {
        Tag tag = new StartTag();
        tag.nameAttr("html", null);

        assertEquals("html", tag.name());
        assertEquals("html", tag.normalName());
    }

    @Test
    public void testTagNameLowerCase() {
        Tag tag = new StartTag();
        tag.nameAttr("HTML", null);

        assertEquals("html", tag.name());
        assertEquals("html", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        StartTag image = new StartTag().nameAttr("img", null).isSelfClosing(true);
        assertTrue(image.isSelfClosing());

        StartTag div = new StartTag().nameAttr("div", null);
        assertFalse(div.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "class1");
        attributes.put("id", "id1");

        StartTag tag = new StartTag().nameAttr("div", attributes);

        Attributes tagAttributes = tag.getAttributes();

        assertEquals(attributes, tagAttributes);
    }

    @Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag();
        tag.newAttribute();

        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagNewAttributeWithValue() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("class1");

        tag.newAttribute();

        Attributes tagAttributes = tag.getAttributes();

        assertEquals("class1", tagAttributes.get("class"));
    }

    @Test
    public void testTagNewAttributeWithEmptyValue() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.setEmptyAttributeValue();

        tag.newAttribute();

        Attributes tagAttributes = tag.getAttributes();

        assertEquals("", tagAttributes.get("class"));
    }

    @Test
    public void testTagNewAttributeWithValueAndEmptyValue() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("class1");
        tag.setEmptyAttributeValue();

        tag.newAttribute();

        Attributes tagAttributes = tag.getAttributes();

        assertEquals("class1", tagAttributes.get("class"));
        assertEquals("", tagAttributes.get("class"));
    }

    @Test
    public void testTagNewAttributeWithEmptyValueAndValue() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.setEmptyAttributeValue();
        tag.appendAttributeValue("class1");

        tag.newAttribute();

        Attributes tagAttributes = tag.getAttributes();

        assertEquals("class1", tagAttributes.get("class"));
        assertEquals("class1", tagAttributes.toString());
    }

    @Test
    public void testTagNewAttributeWithEmptyValueAndValueAndEmptyValueAgain() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.setEmptyAttributeValue();
        tag.appendAttributeValue("class1");
        tag.setEmptyAttributeValue();

        tag.newAttribute();

        Attributes tagAttributes = tag.getAttributes();

        assertEquals("class1", tagAttributes.get("class"));
        assertEquals("class1", tagAttributes.toString());
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("class1");
        tag.newAttribute();

        tag.finaliseTag();

        Attributes tagAttributes = tag.getAttributes();

        assertEquals("class1", tagAttributes.get("class"));
        assertEquals("class1", tagAttributes.toString());
    }
}