package org.jsoup.parser; // ensure the package is correct for imports

import org.junit.Assert;
import org.junit.Test;

import java.util.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {
    // Basic JUnit test cases for each method

    @Test
    public void testTokenType() {
        Token token = new Token().tokenType();
        Assert.assertEquals("Token", token);
    }

    @Test
    public void testResetDoctype() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.reset();
        assertEquals(null, doctype.getName());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testResetTag() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertFalse(tag.isSelfClosing());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testNewAttribute() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().setAttribute("class", "test");
        Attributes attributes = tag.getAttributes();
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("test", attributes.getValue("class"));
    }

    @Test
    public void testFinaliseTag() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().setAttribute("class", "test");
        tag.finaliseTag();
        assertEquals("div", tag.tagName);
        assertEquals("div", tag.normalName());
        assertFalse(tag.isSelfClosing());
        assertSame(attributes, tag.getAttributes());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new StartTag();
        tag.name("img").isSelfClosing();
        assertTrue(tag.isSelfClosing());

        tag = new StartTag();
        tag.name("div");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testGetAttributes() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().setAttribute("class", "test");
        tag.newAttribute().setAttribute("id", "testID");
        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("test", attributes.getValue("class"));
        assertEquals("id", attributes.keySet().iterator().next());
        assertEquals("testID", attributes.getValue("id"));
    }

    // Test edge cases and exception handling scenarios

    @Test(expected = NullPointerException.class)
    public void testResetNullToken() {
        new Token().reset(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNewAttributeNullTag() {
        new Tag().newAttribute();
    }

    @Test(expected = NullPointerException.class)
    public void testNewAttributeNullAttributeName() {
        Tag tag = new StartTag();
        tag.newAttribute().newAttribute();
    }

    @Test(expected = NullPointerException.class)
    public void testNewAttributeNullAttributeValue() {
        Tag tag = new StartTag();
        tag.newAttribute().setAttribute(null);
    }

    @Test
    public void testNewAttributeEmptyStringValue() {
        Tag tag = new StartTag();
        tag.newAttribute().setAttribute("empty", "");
        Attributes attributes = tag.getAttributes();
        assertEquals("empty", attributes.keySet().iterator().next());
        assertEquals("", attributes.getValue("empty"));
    }

    @Test
    public void testNewAttributeEmptyAttributeValueBuilder() {
        Tag tag = new StartTag();
        tag.newAttribute().setAttribute("empty");
        Attributes attributes = tag.getAttributes();
        assertEquals("empty", attributes.keySet().iterator().next());
        assertNull(attributes.getValue("empty"));
    }

    @Test
    public void testNewAttributeEmptyAttributeValueString() {
        Tag tag = new StartTag();
        tag.newAttribute().setAttribute("empty", " ");
        Attributes attributes = tag.getAttributes();
        assertEquals("empty", attributes.keySet().iterator().next());
        assertEquals("", attributes.getValue("empty"));
    }

    @Test
    public void testNewAttributeMultipleAttributes() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().setAttribute("class", "test1");
        tag.newAttribute().setAttribute("id", "testID");
        tag.newAttribute().setAttribute("title", "testTitle");
        Attributes attributes = tag.getAttributes();
        assertEquals(3, attributes.size());
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("test1", attributes.getValue("class"));
        assertEquals("id", attributes.keySet().iterator().next());
        assertEquals("testID", attributes.getValue("id"));
        assertEquals("title", attributes.keySet().iterator().next());
        assertEquals("testTitle", attributes.getValue("title"));
    }

    @Test
    public void testNewAttributeDuplicateAttribute() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().setAttribute("class", "test1");
        tag.newAttribute().setAttribute("class", "test2");
        Attributes attributes = tag.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("test1 test2", attributes.getValue("class"));
    }

    @Test
    public void testFinaliseTagNullTag() {
        Tag tag = new Tag();
        tag.finaliseTag();
        assertNull(tag);
    }

    @Test
    public void testFinaliseTagEmptyTag() {
        Tag tag = new StartTag();
        tag.finaliseTag();
        assertFalse(tag.isSelfClosing());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testFinaliseTagNonEmptyTag() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().setAttribute("class", "test");
        tag.finaliseTag();
        assertEquals("div", tag.tagName);
        assertEquals("div", tag.normalName());
        assertFalse(tag.isSelfClosing());
        assertSame(attributes, tag.getAttributes());
    }
}