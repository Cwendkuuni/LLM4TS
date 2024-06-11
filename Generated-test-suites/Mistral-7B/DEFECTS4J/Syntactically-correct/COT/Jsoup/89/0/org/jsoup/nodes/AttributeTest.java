package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

import org.junit.Test;

/**
 * Test class for Attribute.
 */
public class AttributeTest {

    // Basic test cases for each method

    @Test
    public void testAttributeWithKeyAndValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    @Test
    public void testAttributeWithKeyAndValueAndParent() {
        String key = "testKey";
        String value = "testValue";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);

        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertSame(parent, attribute.parent);
    }

    @Test
    public void testSetKey() {
        String originalKey = "originalKey";
        String key = "newKey";
        String value = "testValue";
        Attribute attribute = new Attribute(originalKey, value);

        attribute.setKey(key);

        assertEquals(key, attribute.getKey());
        assertNotSame(originalKey, attribute.getKey());
    }

    @Test
    public void testSetValue() {
        String key = "testKey";
        String originalValue = "originalValue";
        String value = "newValue";
        Attribute attribute = new Attribute(key, originalValue);

        String newValue = attribute.setValue(value);

        assertEquals(value, attribute.getValue());
        assertNotSame(originalValue, attribute.getValue());
        assertEquals(originalValue, newValue);
    }

    @Test
    public void testHtml() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        StringBuilder expected = StringUtil.borrowBuilder();
        attribute.html(expected, new Document("").outputSettings());

        String actual = attribute.html();

        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testToString() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        assertEquals(attribute.html(), attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "unencodedKey";
        String encodedValue = "test%20Value";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("test Value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        String key = "testKey";
        boolean expected = false;
        Attribute attribute = new Attribute(key, null);

        boolean actual = attribute.isDataAttribute();

        assertEquals(expected, actual);

        key = "data-test";
        expected = true;
        attribute = new Attribute(key, null);

        actual = attribute.isDataAttribute();

        assertEquals(expected, actual);
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Document.OutputSettings out = new Document.OutputSettings();

        boolean expected = false;
        Attribute attribute = new Attribute(key, value);

        boolean actual = attribute.shouldCollapseAttribute(out);

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        String key = "testKey";
        String value1 = "testValue1";
        String value2 = "testValue2";

        Attribute attribute1 = new Attribute(key, value1);
        Attribute attribute2 = new Attribute(key, value2);
        Attribute attribute3 = new Attribute(key, value1);

        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";

        Attribute attribute1 = new Attribute(key1, value1);
        Attribute attribute2 = new Attribute(key2, value2);

        int expected1 = attribute1.hashCode();
        int expected2 = attribute2.hashCode();

        assertNotSame(expected1, expected2);

        Attribute attribute3 = new Attribute(key1, value1);
        int actual = attribute3.hashCode();

        assertEquals(expected1, actual);
    }

    @Test
    public void testClone() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Attribute clonedAttribute = attribute.clone();

        assertEquals(key, clonedAttribute.getKey());
        assertEquals(value, clonedAttribute.getValue());
    }

    // Test cases for edge cases and exception handling

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNullKey() {
        String value = "testValue";
        Attribute attribute = new Attribute(null, value);
        attribute.setKey("newKey");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyKey() {
        String key = "";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        attribute.setKey("newKey");
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);
        attribute.setValue("newValue");
    }

    @Test(expected = NullPointerException.class)
    public void testParentWithNullParent() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedWithNullUnencodedKey() {
        String encodedValue = "test%20Value";
        Attribute.createFromEncoded(null, encodedValue);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromEncodedWithNullEncodedValue() {
        String unencodedKey = "unencodedKey";
        Attribute.createFromEncoded(unencodedKey, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithLongKey() {
        String key = StringUtil.repeat("a", 256);
        Attribute attribute = new Attribute(key, "testValue");
        try {
            attribute.setKey("newKey");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().startsWith("Key too long: "));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueWithLongValue() {
        String key = "testKey";
        String longValue = StringUtil.repeat("a", 256 * 1024);
        Attribute attribute = new Attribute(key, "testValue");
        try {
            attribute.setValue(longValue);
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().startsWith("Value too long: "));
        }
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithIOException() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        IOException exception = new IOException();
        StringBuilder accum = StringUtil.borrowBuilder();

        attribute.html(accum, new Document("").outputSettings());
    }
}