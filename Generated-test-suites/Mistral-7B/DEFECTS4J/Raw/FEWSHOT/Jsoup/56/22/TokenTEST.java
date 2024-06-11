package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype().getName("html").getPublicIdentifier("public").getSystemIdentifier("system");
        doctype.isForceQuirks(true);

        Doctype resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testDoctypeValues() {
        Doctype doctype = new Doctype().getName("html").getPublicIdentifier("public").getSystemIdentifier("system").isForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("system", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag().name("div");

        Tag resetTag = tag.reset();
        assertEquals(TokenType.StartTag, resetTag.type);
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
    public void testTagValues() {
        StartTag tag = new StartTag().name("div").name("myDiv").attributes(new Attributes().put("class", "class1").put("data-test", "test"));

        assertEquals("div", tag.tagName);
        assertEquals("myDiv", tag.normalName);
        assertEquals("div", tag.name().toString());
        assertEquals("class1", tag.getAttributes().get("class").getValue());
        assertEquals("test", tag.getAttributes().get("data-test").getValue());
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        StartTag tag = new StartTag().name("input").attributes(new Attributes().put("type", ""));
        tag.setEmptyAttributeValue();

        assertTrue(tag.hasEmptyAttributeValue);
        assertEquals("", tag.getAttributes().get("type").getValue());
    }

    @Test
    public void testTagAppendAttributeValue() {
        StartTag tag = new StartTag().name("input");
        tag.appendAttributeName("type");
        tag.appendAttributeValue("text");

        assertEquals("text", tag.getAttributes().get("type").getValue());
    }

    @Test
    public void testTagAppendAttributeValueWithBuilder() {
        StartTag tag = new StartTag().name("input");
        tag.appendAttributeName("type");
        tag.appendAttributeValue("text");

        tag.ensureAttributeValue();
        tag.pendingAttributeValue.append(" data-test");
        tag.newAttribute();

        assertEquals("text", tag.getAttributes().get("type").getValue());
        assertEquals(" data-test", tag.getAttributes().toString());
    }

    @Test
    public void testTagAppendAttributeValueWithCodepoints() {
        StartTag tag = new StartTag().name("input");
        tag.appendAttributeName("type");
        tag.appendAttributeValue("text");
        tag.appendAttributeValue("\u0068\u0065\u0061\u0074\u0065\u0072"); // html

        assertEquals("texthtml", tag.getAttributes().get("type").getValue());
    }

    @Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag().name("input").name("myInput");
        tag.newAttribute();

        assertFalse(tag.hasPendingAttributeValue);
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag().name("input").name("myInput");
        tag.newAttribute().name("class").value("class1");

        tag.finaliseTag();

        assertEquals("myInput", tag.tagName);
        assertEquals("myinput", tag.normalName);
        assertEquals("input", tag.name().toString());
        assertEquals("class1", tag.getAttributes().get("class").getValue());
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNotNull(tag.getAttributes());
    }
}