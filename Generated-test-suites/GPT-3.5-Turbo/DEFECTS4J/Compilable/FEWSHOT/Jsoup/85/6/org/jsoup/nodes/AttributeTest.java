package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testConstructorWithKeyAndValueAndParent() {
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

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyKey() {
        new Attribute("", "value");
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
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
        Attribute attribute = new Attribute("href", "index.html");
        String html = attribute.html();
        assertEquals("href=\"index.html\"", html);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("href", "index.html");
        String output = attribute.toString();
        assertEquals("href=\"index.html\"", output);
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
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("checked", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean shouldCollapse = attribute.shouldCollapseAttribute(outputSettings);
        assertTrue(shouldCollapse);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "newValue");

        assertEquals(attribute1, attribute2);
        assertNotEquals(attribute1, attribute3);
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");

        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();

        assertEquals(attribute, clone);
        assertNotSame(attribute, clone);
    }
}