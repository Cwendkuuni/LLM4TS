package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeConstructorWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, parent.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyInAttributeConstructor() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueInAttributeConstructor() {
        new Attribute("key", null);
    }

    @Test
    public void testSetKeyMethod() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyInSetKeyMethod() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValueMethod() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueInSetValueMethod() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        expected.append("key=\"value\"");
        assertEquals(expected.toString(), attribute.html());
    }

    @Test(expected = IOException.class)
    public void testHtmlMethodWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        throw new IOException();
    }

    @Test
    public void testCreateFromEncodedMethod() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullUnencodedKeyInCreateFromEncodedMethod() {
        Attribute.createFromEncoded(null, "value");
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attribute = new Attribute("data-key", null);
        assertTrue(attribute.isDataAttribute());
        Attribute attributeWithoutPrefix = new Attribute("key", null);
        assertFalse(attributeWithoutPrefix.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "key");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        Attribute attributeWithValue = new Attribute("key", "value");
        assertFalse(attributeWithValue.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttributeDeprecatedMethod() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
        Attribute attributeWithoutBooleanValue = new Attribute("key", null);
        assertFalse(attributeWithoutBooleanValue.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeMethod() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(Attribute.isBooleanAttribute("checked"));
        Attribute attributeWithoutBooleanValue = new Attribute("key", null);
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}