
package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

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
        assertNull(attribute.parent);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
        assertNull(attribute.parent);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
        assertNull(attribute.parent);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testIsDataAttributeTrue() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
        assertNull(attribute.parent);
    }

    @Test
    public void testIsDataAttributeFalse() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
        assertNull(attribute.parent);
    }

    @Test
    public void testShouldCollapseAttributeTrue() {
        Attribute attribute = new Attribute("key", "key");
        Document.OutputSettings out = new Document.OutputSettings();
        out.setSyntax(Document.OutputSettings.Syntax.html);
        assertTrue(attribute.shouldCollapseAttribute(out));
        assertNull(attribute.parent);
    }

    @Test
    public void testShouldCollapseAttributeFalse() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        out.setSyntax(Document.OutputSettings.Syntax.html);
        assertFalse(attribute.shouldCollapseAttribute(out));
        assertNull(attribute.parent);
    }

    @Test
    public void testIsBooleanAttributeTrue() {
        Attribute attribute = new Attribute("key", null);
        assertTrue(attribute.isBooleanAttribute());
        assertNull(attribute.parent);
    }

    @Test
    public void testIsBooleanAttributeFalse() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
        assertNull(attribute.parent);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
        assertNull(attribute1.parent);
        assertNull(attribute2.parent);
    }

    @Test
    public void testNotEqualsDifferentKey() {
        Attribute attribute1 = new Attribute("key1", "value");
        Attribute attribute2 = new Attribute("key2", "value");
        assertFalse(attribute1.equals(attribute2));
        assertNull(attribute1.parent);
        assertNull(attribute2.parent);
    }

    @Test
    public void testNotEqualsDifferentValue() {
        Attribute attribute1 = new Attribute("key", "value1");
        Attribute attribute2 = new Attribute("key", "value2");
        assertFalse(attribute1.equals(attribute2));
        assertNull(attribute1.parent);
        assertNull(attribute2.parent);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(3666007, attribute.hashCode());
        assertNull(attribute.parent);
    }

    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        assertEquals(attribute1.getKey(), attribute2.getKey());
        assertEquals(attribute1.getValue(), attribute2.getValue());
        assertNotSame(attribute1, attribute2);
        assertNull(attribute1.parent);
        assertNull(attribute2.parent);
    }
}
