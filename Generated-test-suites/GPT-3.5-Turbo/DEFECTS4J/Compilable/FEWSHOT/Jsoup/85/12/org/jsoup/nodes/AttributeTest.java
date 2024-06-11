package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertNull(attribute.parent);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldVal = attribute.setValue("newValue");
        assertEquals("value", oldVal);
        assertEquals("newValue", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        String html = attribute.html();
        assertEquals("href=\"index.html\"", html);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("href", "index.html");
        String str = attribute.toString();
        assertEquals("href=\"index.html\"", str);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("href", "index.html");
        assertEquals("href", attribute.getKey());
        assertEquals("index.html", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        boolean isDataAttribute = attribute.isDataAttribute();
        assertEquals(true, isDataAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "");
        Document.OutputSettings out = new Document.OutputSettings();
        boolean shouldCollapseAttribute = attribute.shouldCollapseAttribute(out);
        assertEquals(true, shouldCollapseAttribute);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        boolean isBooleanAttribute = attribute.isBooleanAttribute();
        assertEquals(true, isBooleanAttribute);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "newValue");
        Attribute attribute4 = new Attribute("newKey", "value");
        assertEquals(true, attribute1.equals(attribute2));
        assertEquals(false, attribute1.equals(attribute3));
        assertEquals(false, attribute1.equals(attribute4));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.hashCode();
        int expectedHashCode = 31 * "key".hashCode() + "value".hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}