package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for the Attribute class.
 */
public class AttributeTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());
    }

    /**
     * Test constructor with valid arguments and parent.
     */
    @Test
    public void testValidConstructorWithParent() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", attributes);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertSame(attributes, attribute.getParent());
    }

    /**
     * Test setting key.
     */
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.setKey("key2");
        assertEquals("key2", attribute.getKey());
    }

    /**
     * Test setting value.
     */
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value1");
        String newValue = "value2";
        String oldValue = attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals("value1", oldValue);
    }

    /**
     * Test HTML representation.
     */
    @Test
    public void testHtml() throws Exception {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();
        assertTrue(hashCode1 == hashCode2);
        assertFalse(hashCode1 == hashCode3);
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertSame(attribute.getParent(), clonedAttribute.getParent());
    }

    /**
     * Test isBooleanAttribute method.
     */
    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("class"));
        assertNull(Attribute.isBooleanAttribute(null));
    }

    /**
     * Test shouldCollapseAttribute method.
     */
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(Attribute.shouldCollapseAttribute("checked", null, outputSettings));
        assertFalse(Attribute.shouldCollapseAttribute("class", "value", outputSettings));
    }
}