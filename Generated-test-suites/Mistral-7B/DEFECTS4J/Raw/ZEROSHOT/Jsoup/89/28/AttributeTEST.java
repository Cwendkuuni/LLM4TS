package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import org.junit.Test;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        // Test creating an Attribute with a key and value
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());

        // Test creating an Attribute with a key, value, and parent
        Attribute attributeParent = new Attributes();
        Attribute attributeWithParent = new Attribute("keyWithParent", "valueWithParent", attributeParent);
        assertEquals("keyWithParent", attributeWithParent.getKey());
        assertEquals("valueWithParent", attributeWithParent.getValue());
        assertEquals(attributeParent, attributeWithParent.getParent());

        // Test creating an Attribute from encoded key and value
        Attribute encodedAttribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertEquals("unencodedKey", encodedAttribute.getKey());
        assertEquals("encodedValue", encodedAttribute.getValue());
    }

    @Test
    public void testAttributeSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeSetKeyNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeSetKeyEmpty() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test
    public void testAttributeSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testAttributeIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        assertTrue(Attribute.shouldCollapseAttribute("checked", null, new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings()));
    }

    @Test
    public void testAttributeEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        assertEquals(attribute1, attribute1);
        assertEquals(attribute1, attribute2);
        assertNotEquals(attribute1, attribute3);
    }

    @Test
    public void testAttributeClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}