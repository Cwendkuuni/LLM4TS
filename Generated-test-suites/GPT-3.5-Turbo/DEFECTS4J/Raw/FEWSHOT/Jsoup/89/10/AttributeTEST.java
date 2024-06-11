package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

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
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String toString = attribute.toString();
        assertEquals("key=\"value\"", toString);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        assertEquals("key", attribute.getKey());
        assertEquals("\"value\"", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(Attribute.isDataAttribute("key"));
        assertTrue(Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute1 = new Attribute("key", "");
        assertTrue(attribute1.shouldCollapseAttribute(out));

        Attribute attribute2 = new Attribute("key", "key");
        assertTrue(attribute2.shouldCollapseAttribute(out));

        Attribute attribute3 = new Attribute("key", "value");
        assertFalse(attribute3.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(Attribute.isBooleanAttribute("key"));
        assertTrue(Attribute.isBooleanAttribute("autofocus"));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("newKey", "value");
        Attribute attribute4 = new Attribute("key", "newValue");

        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.hashCode();
        assertEquals(4293526, hashCode);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assertNotSame(attribute, clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
        assertNull(clone.parent);
    }

}