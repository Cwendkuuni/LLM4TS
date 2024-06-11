package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {
    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testConstructorWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKey() {
        new Attribute(null, "value");
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value");
        attribute.setKey("key2");
        assertEquals("key2", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value1");
        String oldValue = attribute.setValue("value2");
        assertEquals("value2", attribute.getValue());
        assertEquals("value1", oldValue);
    }

    @Test
    public void testSetValueWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value1", parent);
        String oldValue = attribute.setValue("value2");
        assertEquals("value2", attribute.getValue());
        assertEquals("value1", oldValue);
        assertEquals("value2", parent.get("key"));
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testHtmlWithEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        assertEquals("key", attribute.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        attribute.html(null, null);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&#x61;&#x74;&#x74;&#x72;&#x69;&#x62;&#x75;&#x74;&#x65;&#x3D;&#x76;&#x61;&#x6C;&#x75;&#x65;");
        assertEquals("attribute", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeWithNonDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWithBlankValue() {
        Attribute attribute = new Attribute("key", "");
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWithSameValueAsKey() {
        Attribute attribute = new Attribute("key", "key");
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWithDifferentValue() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1, attribute2);
    }

    @Test
    public void testEqualsWithDifferentKey() {
        Attribute attribute1 = new Attribute("key1", "value");
        Attribute attribute2 = new Attribute("key2", "value");
        assertNotEquals(attribute1, attribute2);
    }

    @Test
    public void testEqualsWithDifferentValue() {
        Attribute attribute1 = new Attribute("key", "value1");
        Attribute attribute2 = new Attribute("key", "value2");
        assertNotEquals(attribute1, attribute2);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(-223796847, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assertEquals(attribute, clone);
    }
}