package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype token = new Doctype();
        token.getName().equals("html");
        token.pubSysKey = "pubsys";
        token.publicIdentifier.append("public");
        token.systemIdentifier.append("system");
        token.forceQuirks = true;

        Doctype resetToken = token.reset();

        assertEquals(token.getClass(), resetToken.getClass());
        assertEquals(TokenType.Doctype, resetToken.type);
        assertNull(resetToken.getName());
        assertNull(resetToken.pubSysKey);
        assertNull(resetToken.publicIdentifier);
        assertNull(resetToken.systemIdentifier);
        assertFalse(resetToken.isForceQuirks());
    }

    @Test
    public void testDoctypeMethods() {
        Doctype token = new Doctype();

        token.getName().Equals("html");
        token.pubSysKey = "pubsys";
        token.publicIdentifier.append("public");
        token.systemIdentifier.append("system");
        token.forceQuirks = true;

        assertEquals("html", token.getName());
        assertEquals("pubsys", token.getPubSysKey());
        assertEquals("public", token.getPublicIdentifier());
        assertEquals("system", token.getSystemIdentifier());
        assertTrue(token.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag token = new StartTag();
        token.tagName = "div";
        token.normalName = "div";
        token.pendingAttributeName = "class";
        token.pendingAttributeValue = new StringBuilder("class1 class2");
        token.selfClosing = false;
        token.attributes = new Attributes();
        token.attributes.put(new Attribute("id", "id1"));

        Tag resetToken = token.reset();

        assertEquals(token.getClass(), resetToken.getClass());
        assertEquals(TokenType.StartTag, resetToken.type);
        assertNull(resetToken.tagName);
        assertNull(resetToken.normalName);
        assertNull(resetToken.pendingAttributeName);
        assertNull(resetToken.pendingAttributeValue);
        assertFalse(resetToken.isSelfClosing());
        assertNull(resetToken.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Tag token = new StartTag();
        token.tagName = "div";
        token.newAttribute();

        assertEquals(TokenType.StartTag, token.type);
        assertTrue(token.hasPendingAttributeValue);
        assertTrue(token.attributes != null);
        assertSame(token.attributes.getClass(), Attributes.class);
        assertEquals(1, token.attributes.size());
        Attribute attribute = token.attributes.get("class");
        assertEquals("", attribute.getValue());
    }

    @Test
    public void testTagFinaliseTag() {
        Tag token = new StartTag();
        token.tagName = "div";
        token.newAttribute();
        token.finaliseTag();

        assertEquals(TokenType.StartTag, token.type);
        assertFalse(token.hasPendingAttributeValue);
        assertNull(token.pendingAttributeValue);
        assertNull(token.pendingAttributeName);
        assertNull(token.pendingAttributeValueS);
        assertTrue(token.attributes != null);
        assertTrue(token.attributes.size() > 0);
    }

    @Test
    public void testTagMethods() {
        Tag token = new StartTag();

        token.tagName = "div";
        token.normalName = "div";
        token.pendingAttributeName = "class";
        token.pendingAttributeValue = new StringBuilder("class1 class2");
        token.selfClosing = false;
        token.attributes = new Attributes();
        token.attributes.put(new Attribute("id", "id1"));

        assertEquals("div", token.name());
        assertEquals("div", token.normalName());
        assertFalse(token.isSelfClosing());
        assertTrue(token.getAttributes() != null);
        assertEquals(1, token.getAttributes().size());
        Attribute attribute = token.getAttributes().get("id");
        assertEquals("id1", attribute.getValue());
    }
}