package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import org.junit.Test;

/**
 * Test class for the Token abstract class.
 */
public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Doctype();
        assertEquals("Doctype", token.tokenType());
        token = new StartTag();
        assertEquals("StartTag", token.tokenType());
        token = new EndTag();
        assertEquals("EndTag", token.tokenType());
        token = new Comment();
        assertEquals("Comment", token.tokenType());
        token = new Character();
        assertEquals("Character", token.tokenType());
        token = new CData();
        assertEquals("Character", token.tokenType()); // CData is treated as an extension of Character in the test
        token = new EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        String name = "html";
        doctype.name(name);
        assertEquals(name, doctype.getName());
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        String publicIdentifier = "public_identifier";
        doctype.setPublicIdentifier(publicIdentifier);
        assertEquals(publicIdentifier, doctype.getPublicIdentifier());
        doctype.reset();
        assertNull(doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        String systemIdentifier = "system_identifier";
        doctype.setSystemIdentifier(systemIdentifier);
        assertEquals(systemIdentifier, doctype.getSystemIdentifier());
        doctype.reset();
        assertNull(doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeInvalidReset() {
        Doctype doctype = new Doctype();
        doctype.setName("name");
        doctype.reset();
        assertEquals("name", doctype.getName()); // should not be reset
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name("tag");
        assertEquals("tag", tag.name());
        tag.reset();
        assertNull(tag.name());
    }

    @Test
    public void testTagName() {
        Tag tag = new StartTag();
        tag.name("tag");
        tag.name("Tag");
        assertEquals("Tag", tag.name());
        tag.reset();
        assertNull(tag.name());
    }

    @Test
    public void testTagNormalName() {
        Tag tag = new StartTag();
        tag.name("Tag");
        tag.name("tag");
        assertEquals("tag", tag.normalName());
        tag.reset();
        assertNull(tag.normalName());
    }

    @Test
    public void testTagSelfClosing() {
        Tag tag = new StartTag();
        tag.isSelfClosing(true);
        assertTrue(tag.isSelfClosing());
        tag.reset();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagAttributes() {
        Tag tag = new StartTag();
        tag.newAttribute();
        tag.appendAttributeName("name");
        tag.appendAttributeValue("value");
        Attributes attributes = tag.getAttributes();
        assertEquals("name", attributes.getValue("name"));
        assertEquals("value", attributes.getValue("name"));
        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagAttributesCaseInsensitive() {
        Tag tag = new StartTag();
        tag.newAttribute();
        tag.appendAttributeName("Name");
        tag.appendAttributeValue("value");
        Attributes attributes = tag.getAttributes();
        assertEquals("name", attributes.getName("Name"));
        assertEquals("value", attributes.getValue("Name"));
        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagAttributesEmptyValue() {
        Tag tag = new StartTag();
        tag.newAttribute();
        tag.appendAttributeName("name");
        tag.setEmptyAttributeValue();
        Attributes attributes = tag.getAttributes();
        assertEquals("", attributes.getValue("name"));
        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagAttributesMultipleValues() {
        Tag tag = new StartTag();
        tag.newAttribute();
        tag.appendAttributeName("name");
        tag.appendAttributeValue("value1");
        tag.newAttribute();
        tag.appendAttributeName("name");
        tag.appendAttributeValue("value2");
        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.getValue("name"));
        assertEquals("value2", attributes.getValue("name"));
        tag.reset();
        assertNull(tag.getAttributes());
    }

}