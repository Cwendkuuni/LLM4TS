package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        doctype.reset();

        assertEquals(null, doctype.name);
        assertEquals(null, doctype.pubSysKey);
        assertEquals(null, doctype.publicIdentifier);
        assertEquals(null, doctype.systemIdentifier);
        assertEquals(false, doctype.forceQuirks);
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctypeGetters() {
        String name = "html";
        Token doctype = new Token.Doctype();
        doctype.name(name);

        assertEquals(name, doctype.getName());
        assertEquals(null, doctype.getPubSysKey());
        assertEquals(null, doctype.getPublicIdentifier());
        assertEquals(null, doctype.getSystemIdentifier());
        assertEquals(false, doctype.isForceQuirks());

        doctype.pubSysKey = "public -1";
        doctype.publicIdentifier.append("ISOD TF-PI");
        doctype.systemIdentifier.append("about:blank");
        doctype.forceQuirks = true;

        assertEquals(name, doctype.getName());
        assertEquals("public -1", doctype.getPubSysKey());
        assertEquals("ISOD TF-PI", doctype.getPublicIdentifier());
        assertEquals("about:blank", doctype.getSystemIdentifier());
        assertEquals(true, doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.Tag();
        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue);
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(false, tag.isSelfClosing);
        assertEquals(null, tag.attributes);
        assertEquals(TokenType.Tag, tag.type);

        tag.reset();

        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue);
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(false, tag.isSelfClosing);
        assertEquals(null, tag.attributes);
        assertEquals(TokenType.Tag, tag.type);
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.Tag();
        tag.newAttribute();

        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue);
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(false, tag.isSelfClosing);
        assertEquals(null, tag.attributes);

        tag.pendingAttributeName = "class";
        tag.newAttribute();

        assertEquals("class", tag.pendingAttributeName);
        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue);
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(false, tag.isSelfClosing);
        assertEquals(null, tag.attributes);
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.pendingAttributeName = "class";
        tag.finaliseTag();

        assertEquals("class", tag.pendingAttributeName);
        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue);
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(false, tag.isSelfClosing);
        assertNotNull(tag.attributes);
    }

    @Test
    public void testTagName() {
        Token tag = new Token.Tag();
        tag.name("div");

        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
    }

    @Test
    public void testTagNameLowerCase() {
        Token tag = new Token.Tag();
        tag.name("DIV");

        assertEquals("div", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token tag = new Token.Tag();
        tag.selfClosing = true;

        assertTrue(tag.isSelfClosing());

        tag.reset();

        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token tag = new Token.StartTag();
        tag.attributes = new Attributes();
        tag.attributes.put("class", "foo");
        tag.attributes.put("id", "bar");

        Attributes attributes = tag.getAttributes();

        assertEquals("class=foo id=bar", attributes.toString());
    }

    @Test
    public void testTagToStringStartTag() {
        Token tag = new Token.StartTag();
        tag.name("div");
        tag.attributes = new Attributes();
        tag.attributes.put("class", "foo");
        tag.attributes.put("id", "bar");

        assertEquals("<div class=\"foo\" id=\"bar\">", tag.toString());
    }

    @Test
    public void testTagToStringStartTagNoAttributes() {
        Token tag = new Token.StartTag();
        tag.name("div");

        assertEquals("<div>", tag.toString());
    }

    @Test
    public void testTagToStringEndTag() {
        Token tag = new Token.EndTag();
        tag.name("div");

        assertEquals("</div>", tag.toString());
    }
}