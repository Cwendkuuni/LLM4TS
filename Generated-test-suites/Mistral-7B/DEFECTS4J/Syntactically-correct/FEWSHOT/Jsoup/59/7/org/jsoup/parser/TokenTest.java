package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        assertEquals("html", doctype.getName());

        Doctype resetDoctype = doctype.reset();
        assertNull(resetDoctype.getName());
        assertFalse(doctype.isForceQuirks());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertEquals(doctype, resetDoctype);
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.setForceQuirks(true);
        doctype.getPublicIdentifier().equals("public id");
        doctype.setSystemIdentifier("system id");

        assertEquals("html", doctype.getName());
        assertTrue(doctype.isForceQuirks());
        assertEquals("public id", doctype.getPublicIdentifier());
        assertEquals("system id", doctype.getSystemIdentifier());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name().equals("div");
        assertEquals("div", tag.name());

        Tag resetTag = tag.reset();
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertNull(resetTag.pendingAttributeValue);
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertNull(resetTag.attributes);
        assertEquals(tag, resetTag);
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.appendAttributeName("class").appendAttributeValue("foo");
        tag.newAttribute();

        Attribute[] attributes = tag.getAttributes().toArray(new Attribute[0]);
        assertEquals(1, attributes.length);
        assertEquals("class", attributes[0].getName());
        assertEquals("foo", attributes[0].getValue());
        assertNull(tag.pendingAttributeName);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
    }

    @Test
    public void testTagFinaliseTag() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.appendAttributeName("class").appendAttributeValue("foo");
        tag.finaliseTag();

        Attribute[] attributes = tag.getAttributes().toArray(new Attribute[0]);
        assertEquals(1, attributes.length);
        assertEquals("class", attributes[0].getName());
        assertEquals("foo", attributes[0].getValue());
        assertNull(tag.pendingAttributeName);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
    }

    @Test
    public void testTagToString() {
        StartTag tag = new StartTag();
        tag.name("div");
        tag.appendAttributeName("class").appendAttributeValue("foo");

        assertEquals("<div class=\"foo\"></div>", tag.toString());
    }
}