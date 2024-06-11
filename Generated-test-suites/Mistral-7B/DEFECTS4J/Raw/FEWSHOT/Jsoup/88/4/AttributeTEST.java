package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.helper.Validate;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertSame(parentAttributes, attribute.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");

        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetKeyEmpty() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");

        assertEquals("", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");

        assertEquals("value", attribute.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");

        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, null);

        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings settings = new Document.OutputSettings();

        assertTrue(Attribute.shouldCollapseAttribute("key", "", settings));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", settings));
        assertTrue(Attribute.shouldCollapseAttribute("key", null, settings));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", settings));
        assertFalse(Attribute.shouldCollapseAttribute("key", "otherValue", settings));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNull(clonedAttribute.getParent());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromEncodedNullKey() {
        Attribute.createFromEncoded(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedEmptyKey() {
        Attribute.createFromEncoded("", "");
    }

    @Test
    public void testCreateFromEncodedWithParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"", null);

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertSame(parentAttributes, attribute.getParent());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");

        assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");

        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAttributeWithNullKey() {
        Attributes parent = new Attributes();
        Validate.notNull(parent);
        try {
            new Attribute(null, "value", parent);
        } catch (IllegalArgumentException e) {
            assertEquals("key is marked non-null but is null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAttributeWithEmptyKey() {
        Attributes parent = new Attributes();
        Validate.notNull(parent);
        try {
            new Attribute("", "value", parent);
        } catch (IllegalArgumentException e) {
            assertEquals("key is marked non-empty but is empty", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCreateAttributeWithInvalidKey() {
        Attributes parent = new Attributes();
        Validate.notNull(parent);
        assertThrows(IllegalArgumentException.class, () -> new Attribute(" ", "value", parent));
    }

    @Test
    public void testCreateAttributeWithNullValue() {
        Attributes parent = new Attributes();
        Validate.notNull(parent);
        try {
            new Attribute("key", null, parent);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("value is marked non-null but is null", e);
        }
    }
}