package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

import java.util.Arrays;

public class AttributeTest {

    @org.junit.Test
    public void testConstructorWithKeyAndValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    @org.junit.Test
    public void testConstructorWithKeyValueAndParent() {
        String key = "testKey";
        String value = "testValue";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertEquals(parent, attribute.parent);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testConstructorWithNullKey() {
        String key = null;
        String value = "testValue";
        new Attribute(key, value);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testConstructorWithEmptyKey() {
        String key = "";
        String value = "testValue";
        new Attribute(key, value);
    }

    @org.junit.Test
    public void testSetKey() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, "testValue");
        String newKey = "newKey";
        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSetKeyWithNullKey() {
        String key = null;
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey(key);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyKey() {
        String key = "";
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey(key);
    }

    @org.junit.Test
    public void testSetKeyAndParent() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, "testValue");
        Attributes parent = new Attributes();
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals(parent, attribute.parent);
    }

    @org.junit.Test
    public void testGetValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(value, attribute.getValue());
    }

    @org.junit.Test
    public void testSetValueAndGetOldValue() {
        String key = "testKey";
        String oldValue = "oldValue";
        String newValue = "newValue";
        Attribute attribute = new Attribute(key, oldValue);
        String returnedOldValue = attribute.setValue(newValue);
        assertEquals(oldValue, returnedOldValue);
        assertEquals(newValue, attribute.getValue());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        String key = "testKey";
        String value = null;
        Attribute attribute = new Attribute(key, "oldValue");
        attribute.setValue(value);
    }

    @org.junit.Test
    public void testHtml() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String result = StringUtil.releaseBuilder(sb);
        String expected = key + "=\"testValue\"";
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = IOException.class)
    public void testHtmlWithIOException() throws IOException {
        String key = "testKey";
        Attribute attribute = new Attribute(key, "testValue");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, null);
    }

    @org.junit.Test
    public void testIsDataAttribute() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, "testValue");
        assertTrue(attribute.isDataAttribute());
        key = "nonDataKey";
        attribute = new Attribute(key, "testValue");
        assertFalse(attribute.isDataAttribute());
    }

    @org.junit.Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("testKey", "testValue");
        assertTrue(attribute.shouldCollapseAttribute(out));
        attribute = new Attribute("booleanAttribute", null);
        assertTrue(attribute.shouldCollapseAttribute(out));
        attribute = new Attribute("nonBooleanAttribute", "testValue");
        assertFalse(attribute.shouldCollapseAttribute(out));
        out.syntax(Document.OutputSettings.Syntax.xml);
        attribute = new Attribute("booleanAttribute", null);
        assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @org.junit.Test
    public void testEquals() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute1 = new Attribute(key1, value1);
        Attribute attribute2 = new Attribute(key2, value2);
        Attribute attribute3 = new Attribute(key1, value1);
        Attribute attribute4 = new Attribute(key1, value2);
        Attribute attribute5 = new Attribute(key2, value1);
        Attribute attribute6 = new Attribute(key2, value2);

        assertTrue(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute4));
        assertFalse(attribute1.equals(attribute5));
        assertFalse(attribute1.equals(attribute6));
    }

    @org.junit.Test
    public void testClone() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
        assertEquals(key, clonedAttribute.getKey());
        assertEquals(value, clonedAttribute.getValue());
    }
}