package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

import java.util.Collections;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.publicIdentifier("public id");
        doctype.systemIdentifier("system id");
        doctype.forceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("public id", doctype.getPublicIdentifier());
        assertEquals("system id", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertEquals("Doctype", resetDoctype.tokenType());
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.attributes(new Attributes().put("id", "test"));

        assertEquals("StartTag", tag.tokenType());
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertEquals("id", tag.pendingAttributeName);
        assertTrue(tag.hasPendingAttributeValue);
        assertTrue(tag.getAttributes() != null);
        assertEquals("test", tag.getAttributes().get("id").getValue());

        tag = tag.reset();
        assertEquals("StartTag", tag.tokenType());
        assertNull(tag.name());
        assertNull(tag.normalName());
        assertNull(tag.pendingAttributeName);
        assertFalse(tag.hasPendingAttributeValue);
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.name("div");

        tag.newAttribute().name("class").value("test");
        assertEquals("StartTag", tag.tokenType());
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertNull(tag.pendingAttributeName);
        assertFalse(tag.hasPendingAttributeValue);
        assertNull(tag.getAttributes());

        assertEquals("class", tag.pendingAttributeName);
        assertTrue(tag.hasPendingAttributeValue);
        assertEquals("test", tag.pendingAttributeValue.toString());

        tag.newAttribute();

        assertEquals("StartTag", tag.tokenType());
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertNull(tag.pendingAttributeName);
        assertFalse(tag.hasPendingAttributeValue);
        assertEquals(Collections.emptyMap(), tag.getAttributes());

        reset(tag);

        tag.newAttribute().name("class").value("test");
        assertEquals("StartTag", tag.tokenType());
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertNull(tag.pendingAttributeName);
        assertFalse(tag.hasPendingAttributeValue);
        assertEquals(Collections.singletonMap("class", new Attribute("class", "test")), tag.getAttributes());
    }

    @Test
    public void testTagFinaliseTag() {
        Tag tag = new StartTag();
        tag.name("div");

        tag.newAttribute().name("class").value("test");
        tag.finaliseTag();

        assertEquals("StartTag", tag.tokenType());
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertNull(tag.pendingAttributeName);
        assertFalse(tag.hasPendingAttributeValue);
        assertEquals(Collections.singletonMap("class", new Attribute("class", "test")), tag.getAttributes());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag = new StartTag();
        tag.name("img");

        tag.selfClosing = true;

        assertTrue(tag.isSelfClosing());

        tag.selfClosing = false;

        assertFalse(tag.isSelfClosing());

        reset(tag);

        tag.selfClosing = true;

        assertTrue(tag.isSelfClosing());
    }
}