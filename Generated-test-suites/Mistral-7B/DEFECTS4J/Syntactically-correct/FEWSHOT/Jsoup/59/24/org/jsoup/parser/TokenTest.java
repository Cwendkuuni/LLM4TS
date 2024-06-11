package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("");
        doctype.reset();
        doctype.asDoctype().getName().equals("");
    }

    @Test
    public void testDoctypeGetName() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        assertEquals("html", doctype.asDoctype().getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setPublicIdentifier("_public");
        assertEquals("_public", doctype.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setSystemIdentifier("/path/to/file.html");
        assertEquals("/path/to/file.html", doctype.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setForceQuirks(true);
        assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.reset();
        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals(null, tag.pendingAttributeValue);
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(false, tag.selfClosing);
        assertEquals(null, tag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.StartTag();
        tag.newAttribute();
        assertNotNull(tag.attributes);
        assertTrue(tag.attributes.isEmpty());
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.StartTag();
        tag.newAttribute();
        tag.finaliseTag();
        assertNotNull(tag.attributes);
        assertTrue(tag.attributes.isEmpty());
    }

    @Test
    public void testTagName() {
        Token tag = new Token.StartTag();
        tag.name("html");
        assertEquals("html", tag.name());
    }

    @Test
    public void testTagNameLowerCase() {
        Token tag = new Token.StartTag();
        tag.name("html");
        assertEquals("html", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token tag = new Token.StartTag();
        tag.isSelfClosing(true);
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token tag = new Token.StartTag();
        Attributes attributes = new Attributes();
        tag.getAttributes().put("class", "foo");
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("class", "foo");
        assertEquals(expectedAttributes, tag.getAttributes());
    }

    @Test
    public void testTagAppendTagName() {
        Token tag = new Token.StartTag();
        tag.appendTagName("html");
        assertEquals("html", tag.tagName);
        assertEquals("html", tag.normalName());
    }

    @Test
    public void testTagAppendAttributeName() {
        Token tag = new Token.StartTag();
        tag.appendAttributeName("class");
        assertEquals("class", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token tag = new Token.StartTag();
        tag.ensureAttributeValue();
        tag.appendAttributeValue("foo");
        assertEquals("foo", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Token tag = new Token.StartTag();
        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
    }
}