package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.parser.Token.*;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pubsyskey";
        doctype.publicIdentifier.append("publicid");
        doctype.systemIdentifier.append("systemid");
        doctype.forceQuirks = true;

        Doctype resetDoctype = doctype.reset();
        assertEquals(Doctype.class, resetDoctype.getClass());
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.tagName = "div";
        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("class", "test"));
        tag.attributes.put(new BooleanAttribute("disabled", true));

        Tag resetTag = tag.reset();
        assertEquals(StartTag.class, resetTag.getClass());
        assertEquals(TokenType.StartTag, resetTag.type);
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertNull(resetTag.pendingAttributeValue);
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertNull(resetTag.getAttributes());
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();

        tag.appendTagName("div");
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");

        tag.newAttribute();

        tag.appendAttributeName("id");
        tag.appendAttributeValue("123");

        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("test", attributes.get("class").getValue());
        assertEquals("123", attributes.get("id").getValue());
    }

    @Test
    public void testTagFinaliseTag() {
        Tag tag = new StartTag();
        tag.appendTagName("div");
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");
        tag.finaliseTag();

        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertEquals("test", tag.getAttributes().get("class").getValue());
    }

    // Add more test cases for other functionalities of the Token class.

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();

        EOF resetEof = eof.reset();
        assertEquals(EOF.class, resetEof.getClass());
        assertEquals(TokenType.EOF, resetEof.type);
    }
}