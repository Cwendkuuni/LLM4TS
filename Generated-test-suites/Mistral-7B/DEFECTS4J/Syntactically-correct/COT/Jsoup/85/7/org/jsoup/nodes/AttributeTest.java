package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeCreationNullKey() {
        String key = null;
        String value = "testValue";
        new Attribute(key, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeCreationEmptyKey() {
        String key = "";
        String value = "testValue";
        new Attribute(key, value);
    }

    @Test
    public void testAttributeCreationWithParent() {
        String key = "testKey";
        String value = "testValue";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertSame(parent, attribute.parent);
    }

    @Test
    public void testSettingAttributeKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("newTestKey");
        assertEquals("newTestKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSettingAttributeKeyNull() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingAttributeKeyEmpty() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("");
    }

    @Test
    public void testSettingAttributeValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String newValue = "newTestValue";
        assertEquals("testValue", attribute.getValue());
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSettingAttributeValueNull() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setValue(null);
    }

    @Test
    public void testAttributeEncodedCreation() {
        String unencodedKey = "testKey";
        String encodedValue = "test%20Value";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("test Value", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeEncodedCreationNullUnencodedKey() {
        String encodedValue = "test%20Value";
        Attribute.createFromEncoded(null, encodedValue);
    }

    @Test
    public void testAttributeEquals() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute1 = new Attribute(key1, value1);
        Attribute attribute2 = new Attribute(key2, value2);
        Attribute attribute3 = new Attribute(key1, value1);
        Attribute attribute4 = new Attribute(key1, value2);
        Attribute attribute5 = new Attribute(key2, value1);

        assertTrue(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute4));
        assertFalse(attribute1.equals(attribute5));
        assertFalse(attribute1.equals(null));
        assertFalse(attribute1.equals(""));
    }

    @Test
    public void testAttributeClone() {
        String key = "testKey";
        String value = "testValue";
        Attribute original = new Attribute(key, value);
        Attribute cloned = original.clone();
        assertEquals(key, cloned.getKey());
        assertEquals(value, cloned.getValue());
        assertNotSame(original.parent, cloned.parent);
    }

    @Test
    public void testBooleanAttributeCollapsing() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute booleanAttribute = new Attribute("checked", "");
        assertTrue(booleanAttribute.shouldCollapseAttribute(out));
        out.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(booleanAttribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testNonBooleanAttributeCollapsing() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute nonBooleanAttribute = new Attribute("test", "test");
        assertFalse(nonBooleanAttribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute(""));
        assertFalse(Attribute.isBooleanAttribute("test"));
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(new Attribute("data-test", "").isDataAttribute());
        assertFalse(new Attribute("test", "").isDataAttribute());
    }
}