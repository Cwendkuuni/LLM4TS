package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.reset();
        assertEquals(null, doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.getPublicIdentifier().append("PUBLIC");
        doctype.getSystemIdentifier().append("system.xml");
        doctype.isForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("PUBLIC", doctype.getPublicIdentifier().toString());
        assertEquals("system.xml", doctype.getSystemIdentifier().toString());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.reset();
        assertNull(tag.tagName);
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().set("id", "container");
        assertEquals("container", tag.getAttributes().getValue("id"));
    }

    @Test
    public void testTagFinaliseTag() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().set("id", "container");
        tag.finaliseTag();
        assertEquals("container", tag.getAttributes().getValue("id"));
    }

    @Test
    public void testTagName() {
        Tag tag = new StartTag();
        tag.name("div");
        assertEquals("div", tag.name());
    }

    @Test
    public void testTagNormalName() {
        Tag tag = new StartTag();
        tag.name("DIV");
        tag.name("div");
        assertEquals("div", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag = new StartTag();
        tag.name("img");
        tag.isSelfClosing(true);
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().set("id", "container");
        tag.newAttribute().set("class", "class1 class2");

        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("container", attributes.getValue("id"));
        assertEquals("class1 class2", attributes.getValue("class"));

        Element element = new Element("div");
        element.attributes().put("id", "container2");
        element.attributes().put("class", "class3 class4");

        attributes = tag.getAttributes();
        assertEquals("id", attributes.className("id"));
        assertEquals("container2", attributes.getValue("id"));
        assertEquals("class", attributes.className("class"));
        assertEquals("class3 class4", attributes.getValue("class"));
    }

    @Test
    public void testTagAppendTagName() {
        Tag tag = new StartTag();
        tag.appendTagName("div");
        tag.appendTagName("p");
        assertEquals("divp", tag.name());
    }

    @Test
    public void testTagAppendAttributeName() {
        Tag tag = new StartTag();
        tag.appendAttributeName("id");
        tag.appendAttributeName("class");
        assertEquals("idclass", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Tag tag = new StartTag();
        tag.appendAttributeValue("value1");
        tag.appendAttributeValue("value2");
        tag.appendAttributeValue("value3");
        assertEquals("value1value2value3", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagEmptyAttributeValue() {
        Tag tag = new StartTag();
        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testTagToString() {
        StartTag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().set("id", "container");
        tag.newAttribute().set("class", "class1 class2");

        assertEquals("<div id=\"container\" class=\"class1 class2\"></div>", tag.toString());
    }
}