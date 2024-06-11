package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        assertEquals("html", doctype.getName());
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.getPublicIdentifier().equals("public id");
        doctype.setPublicIdentifier("public id");
        assertEquals("public id", doctype.getPublicIdentifier());
        doctype.reset();
        assertNull(doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.getSystemIdentifier().equals("system id");
        doctype.setSystemIdentifier("system id");
        assertEquals("system id", doctype.getSystemIdentifier());
        doctype.reset();
        assertNull(doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        assertFalse(doctype.isForceQuirks());
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name().equals("tag");
        assertEquals("tag", tag.name());
        tag.reset();
        assertNull(tag.name());
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.newAttribute();
        assertNull(tag.getAttributes());
        tag.newAttribute();
        assertNotNull(tag.getAttributes());
    }

    @Test
    public void testTagName() {
        Tag tag = new StartTag();
        tag.name("tag");
        assertEquals("tag", tag.name());
        tag.reset();
        assertNull(tag.name());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag = new StartTag();
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing(true);
        assertTrue(tag.isSelfClosing());
        tag.reset();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Tag tag = new StartTag();
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attribute", "value"));
        tag.setAttributes(attributes);
        assertEquals(attributes, tag.getAttributes());
        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagAppendTagName() {
        Tag tag = new StartTag();
        tag.appendTagName("tag");
        assertEquals("tag", tag.name());
        tag.appendTagName("Tag");
        assertEquals("Tag", tag.name());
        tag.reset();
        assertNull(tag.name());
    }

    @Test
    public void testTagAppendAttributeName() {
        Tag tag = new StartTag();
        tag.appendAttributeName("attribute");
        assertEquals("attribute", tag.pendingAttributeName);
        tag.appendAttributeName("Attribute");
        assertEquals("Attribute", tag.pendingAttributeName);
        tag.reset();
        assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Tag tag = new StartTag();
        tag.appendAttributeValue("value");
        assertEquals("value", tag.pendingAttributeValue.toString());
        tag.appendAttributeValue("Value");
        assertEquals("Value", tag.pendingAttributeValue.toString());
        tag.reset();
        assertNull(tag.pendingAttributeValue);
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Tag tag = new StartTag();
        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
        tag.reset();
        assertFalse(tag.hasEmptyAttributeValue);
    }
}