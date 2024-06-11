package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.pubSysKey("DTD XHTML 1.0");
        doctype.publicIdentifier("-//W3C//DTD XHTML 1.0//EN");
        doctype.systemIdentifier("xhtml1-strict.dtd");
        doctype.forceQuirks(true);

        Doctype resetDoctype = doctype.reset();

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
        tag.name("html");
        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("xmlns:xml", "http://www.w3.org/XML/1998/namespace"));

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
    public void testTokenTypeIdentifier() {
        Doctype doctype = new Doctype();
        StartTag startTag = new StartTag();
        EndTag endTag = new EndTag();
        Comment comment = new Comment();
        Character character = new Character();
        EOF eof = new EOF();

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testTagName() {
        StartTag tag = new StartTag();
        tag.name("html");

        assertEquals("html", tag.name());
        assertEquals("html", tag.normalName());
    }

    @Test
    public void testTagNameLowercase() {
        StartTag tag = new StartTag();
        tag.name("html");

        assertEquals("html", tag.name());
        assertEquals("html", tag.normalName());

        tag.name("HTML");

        assertEquals("html", tag.name());
        assertEquals("html", tag.normalName());
    }

    @Test
    public void testSelfClosingTag() {
        StartTag tag = new StartTag();
        tag.selfClosing = true;

        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testGetAttributes() {
        StartTag tag = new StartTag();
        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("id", "root"));
        tag.attributes.put(new Attribute("class", "container"));

        Attributes attributes = tag.getAttributes();

        assertEquals(2, attributes.size());
        Attribute idAttribute = attributes.get("id");
        Attribute classAttribute = attributes.get("class");

        assertEquals("id", idAttribute.getName());
        assertEquals("root", idAttribute.getValue());

        assertEquals("class", classAttribute.getName());
        assertEquals("container", classAttribute.getValue());
    }

    @Test
    public void testBooleanAttribute() {
        StartTag tag = new StartTag();
        tag.attributes = new Attributes();
        tag.attributes.put(new BooleanAttribute("checked"));

        Attribute booleanAttribute = tag.attributes.get("checked");

        assertTrue(booleanAttribute.getValue().equals("true"));
    }

    @Test
    public void testEmptyAttributeValue() {
        StartTag tag = new StartTag();
        tag.attributes = new Attributes();
        tag.hasEmptyAttributeValue = true;

        assertEquals("", tag.getAttributes().get("")
            .getValue());
    }
}