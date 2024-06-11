package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;
    private Attributes parent;

    @Test
    public void testConstructor() {
        attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        attribute = new Attribute("    key   ", "    value   ");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        attribute.setKey("   newKey   ");
        assertEquals("newKey", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("key", attribute.getKey());
        assertEquals("newValue", attribute.getValue());
        assertNull(attribute.parent);
        attribute.setValue("   newValue   ");
        assertEquals("key", attribute.getKey());
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
        attribute = new Attribute("key", "   value   ");
        assertEquals("key=\"   value   \"", attribute.html());
    }

    @Test
    public void testToString() {
        attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
        attribute = new Attribute("key", "   value   ");
        assertEquals("key=\"   value   \"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        assertEquals("key", attribute.getKey());
        assertEquals("\"value\"", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-key"));
        assertFalse(Attribute.isDataAttribute("key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(Attribute.shouldCollapseAttribute("key", null, out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("allowfullscreen"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testEquals() {
        attribute = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute.equals(attribute2));
        attribute2 = new Attribute("key", "newValue");
        assertFalse(attribute.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        attribute = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), attribute2.hashCode());
        attribute2 = new Attribute("key", "newValue");
        assertNotEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertNotNull(clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}