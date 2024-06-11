package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("oldKey", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "oldValue");
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("oldValue", oldValue);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertEquals("unencodedKey", attribute.getKey());
        assertEquals("encodedValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = (new Document("")).outputSettings();
        assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("booleanAttribute", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "differentValue");
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.hashCode();
        assertEquals(184222641, hashCode);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}