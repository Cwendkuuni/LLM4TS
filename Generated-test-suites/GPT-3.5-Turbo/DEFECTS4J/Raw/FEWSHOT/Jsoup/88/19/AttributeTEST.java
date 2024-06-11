package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
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
        String oldVal = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldVal);
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
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertEquals(true, attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "");
        Document.OutputSettings out = (new Document("")).outputSettings();
        assertEquals(true, attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("booleanAttribute", null);
        assertEquals(true, attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(true, attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(3199383, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}