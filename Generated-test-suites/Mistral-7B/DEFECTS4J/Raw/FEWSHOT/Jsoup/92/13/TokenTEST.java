package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.reset();
        assertTrue(doctype.getName() == null);
    }

    @Test
    public void testDoctypeGetName() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypePubSysKey() {
        Doctype doctype = new Doctype();
        doctype.setPubSysKey("pubsyskey");
        assertEquals("pubsyskey", doctype.getPubSysKey());
        doctype.reset();
        assertNull(doctype.getPubSysKey());
    }

    @Test
    public void testDoctypePublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.setPublicIdentifier("publicid");
        assertEquals("publicid", doctype.getPublicIdentifier());
        doctype.reset();
        assertNull(doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.setSystemIdentifier("systemid");
        assertEquals("systemid", doctype.getSystemIdentifier());
        doctype.reset();
        assertNull(doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name("tagname");
        tag.reset();
        assertTrue(tag.tagName == null);
    }

    @Test
    public void testTagName() {
        StartTag tag = new StartTag();
        tag.name("tagname");
        assertEquals("tagname", tag.name());
    }

    @Test
    public void testTagNormalName() {
        StartTag tag = new StartTag();
        tag.name("TagName");
        assertEquals("tagname", tag.normalName());
    }

    @Test
    public void testTagSelfClosing() {
        StartTag tag = new StartTag();
        tag.setSelfClosing(true);
        assertTrue(tag.isSelfClosing());
        tag.reset();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagAttributes() {
        StartTag tag = new StartTag();
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        tag.newAttribute();
        tag.appendAttributeName("key");
        tag.appendAttributeValue("value");
        assertEquals("value", tag.getAttributes().get("key"));

        tag.newAttribute();
        tag.appendAttributeName("newkey");
        tag.appendAttributeValue("newvalue");
        assertEquals("newvalue", tag.getAttributes().get("newkey"));

        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag();
        tag.name("tagname");
        tag.newAttribute();
        tag.appendAttributeName("key");
        tag.appendAttributeValue("value");
        tag.finaliseTag();
        assertEquals("tagname", tag.name());
        assertSame(tag.getAttributes(), tag.getAttributes());
    }
}