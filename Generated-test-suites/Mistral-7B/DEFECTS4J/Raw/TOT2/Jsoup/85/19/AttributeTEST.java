package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.Arrays;

public class AttributeTest {
    // Test cases for getKey() method
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testGetKeyNullKey() {
        Attribute attribute = new Attribute(null, "value");
        assertNull(attribute.getKey());
    }

    @Test
    public void testGetKeyEmptyKey() {
        Attribute attribute = new Attribute("", "value");
        assertEquals("", attribute.getKey());
    }

    @Test
    public void testGetKeySpecialCharacters() {
        Attribute attribute = new Attribute("key%^&*()_+|{}", "value");
        assertEquals("key%^&*()_+|{}", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testGetKeyNullReturnValue() {
        Attribute attribute = new Attribute("key", null);
        assertNull(attribute.getKey());
        Attribute parent = new Attributes();
        Attribute child = new Attribute("key", null, parent);
        assertNotNull(parent.get(child.getKey()));
    }

    // Test cases for setKey() method
    @Test
    public void testSetKeySameValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("key");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKeyDifferentValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    // Test cases for getValue() method
    @Test
    public void testgetValueSameValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testgetValueNullValue() {
        Attribute attribute = new Attribute("key", null);
        assertNull(attribute.getValue());
    }

    // Test cases for setValue() method
    @Test
    public void testsetValueSameValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testsetValueDifferentValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testsetValueNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
        assertNull(attribute.getValue());
        Attribute parent = new Attributes();
        Attribute child = new Attribute("key", null, parent);
        assertNotNull(parent.get(child.getKey()));
    }

    // Test cases for html() method
    @Test
    public void testhtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        String expected = "key=\"value\"";
        String actual = attribute.html();
        assertEquals(expected, actual);
    }

    @Test(expected = SerializationException.class)
    public void testhtmlIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuffer sb = StringUtil.borrowBuilder();
        // Intentionally throw an IOException here
        attribute.html(sb, null);
        fail("IOException should have been thrown");
    }

    // Test cases for createFromEncoded() method
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedNullKey() {
        String nullKey = null;
        String encodedValue = "key=\"value\"";
        try {
            Attribute.createFromEncoded(nullKey, encodedValue);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedInvalidEncodedValue() {
        String key = "key";
        String invalidEncodedValue = "key=\"value\"<script>alert('XSS')</script>";
        try {
            Attribute.createFromEncoded(key, invalidEncodedValue);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test cases for isDataAttribute() method
    @Test
    public void testIsDataAttributeTrue() {
        Attribute attribute = new Attribute("data-key", null);
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeFalse() {
        Attribute attribute = new Attribute("key", null);
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeWithPrefix() {
        Attribute attribute = new Attribute("data-key", null);
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeWithoutPrefix() {
        Attribute attribute = new Attribute("key", null);
        assertFalse(attribute.isDataAttribute());
    }

    // Test cases for shouldCollapseAttribute() method
    @Test
    public void testShouldCollapseAttributeTrue() throws IOException {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeFalse() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithEmptyValue() throws IOException {
        Attribute attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithSameValueAsKey() throws IOException {
        Attribute attribute = new Attribute("key", "key");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttribute() throws IOException {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttributeValue() throws IOException {
        Attribute attribute = new Attribute("checked", "checked");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    // Test cases for isBooleanAttribute() method
    @Test
    public void testIsBooleanAttributeTrue() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeFalse() {
        Attribute attribute = new Attribute("key", null);
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithBooleanValue() {
        Attribute attribute = new Attribute("checked", "checked");
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithNonBooleanValue() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
    }

    // Test cases for equals() method
    @Test
    public void testEqualsSameObjects() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        assertTrue(attribute1.equals(attribute2));
        assertTrue(attribute2.equals(attribute1));
    }

    @Test
    public void testEqualsDifferentObjects() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute2.equals(attribute1));
    }

    @Test
    public void testEqualsNullObject() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.equals(null));
    }

    // Test cases for hashCode() method
    @Test
    public void testHashCodeSameObjects() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeDifferentObjects() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
    }

    // Test cases for clone() method
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}