package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.setForceQuirks(true);

        Doctype resetDoctype = doctype.reset();

        assertNull(resetDoctype.getName());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name("html").attributes(new Attributes());

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
    }

    @Test
    public void testTagAppendTagName() {
        Tag tag = new StartTag();
        tag.appendTagName("html");
        tag.appendTagName("body");

        assertEquals("htmlbody", tag.name());
    }

    @Test
    public void testTagAppendAttributeName() {
        Tag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.appendAttributeName("foo");
        tag.appendAttributeName("bar");

        assertEquals("classfoo bar", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Tag tag = new StartTag();
        tag.appendAttributeValue("value");
        tag.appendAttributeValue("foo");

        assertEquals("foo", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Tag tag = new StartTag();
        tag.appendAttributeName("checked");
        tag.setEmptyAttributeValue();

        assertFalse(tag.hasPendingAttributeValue);
        assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("foo");
        tag.newAttribute();

        tag.appendAttributeName("id");
        tag.appendAttributeValue("bar");
        tag.newAttribute();

        Attributes attributes = tag.getAttributes();

        assertEquals(2, attributes.size());
        assertEquals("foo", attributes.getClass());
        assertEquals("bar", attributes.getId());
    }

    @Test
    public void testTagFinaliseTag() {
        Tag tag = new StartTag();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("foo");
        tag.newAttribute();
        tag.finaliseTag();

        Attributes attributes = tag.getAttributes();

        assertEquals(1, attributes.size());
        assertEquals("foo", attributes.getClass());
    }
}