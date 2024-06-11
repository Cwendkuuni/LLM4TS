package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithParent() {
        String key = "key";
        String value = "value";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
        Assert.assertNotNull(parent);
    }

    @Test
    public void testSetKey() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        String newKey = "newKey";
        attribute.setKey(newKey);

        Assert.assertEquals(newKey, attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyEmpty() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        attribute.setKey("");
    }

    @Test
    public void testSetValue() {
        String key = "key";
        String oldValue = "oldValue";
        String newValue = "newValue";
        Attribute attribute = new Attribute(key, oldValue);

        String result = attribute.setValue(newValue);

        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals(oldValue, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        String expected = String.format("%s=\"%s\"", key, value);
        Assert.assertEquals(expected, StringUtil.releaseBuilder(sb).trim());
    }

    @Test(expected = IOException.class)
    public void testHtmlIOException() throws IOException {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        throw new IOException();
    }

    @Test
    public void testIsDataAttribute() {
        String key = "data-key";
        Attribute attribute = new Attribute(key, null);

        assertTrue(attribute.isDataAttribute());

        key = "key";
        attribute = new Attribute(key, null);

        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();

        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        assertTrue(attribute.shouldCollapseAttribute(out));

        key = "booleanAttribute";
        attribute = new Attribute(key, null);

        assertTrue(attribute.shouldCollapseAttribute(out));

        value = "value";
        attribute = new Attribute(key, value);

        assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        String key = "booleanAttribute";
        Attribute attribute = new Attribute(key, null);

        assertTrue(attribute.isBooleanAttribute());

        key = "key";
        attribute = new Attribute(key, null);

        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String value1 = "value1";
        Attribute attribute1 = new Attribute(key1, value1);

        String key2 = "key2";
        String value2 = "value2";
        Attribute attribute2 = new Attribute(key2, value2);

        Attribute attribute3 = attribute1;

        Attribute attribute4 = new Attribute(key1, value1);
        Attribute attribute5 = new Attribute(key1, value2);

        Attribute attribute6 = new Attribute(key2, value1);
        Attribute attribute7 = new Attribute(key2, value2);

        Attribute attribute8 = attribute1.clone();

        assertEquals(attribute1, attribute3);
        assertEquals(attribute1, attribute4);
        assertNotEquals(attribute1, attribute5);
        assertNotEquals(attribute1, attribute6);
        assertNotEquals(attribute1, attribute7);
        assertEquals(attribute1, attribute8);
    }

    @Test
    public void testHashCode() {
        String key1 = "key1";
        String value1 = "value1";
        Attribute attribute1 = new Attribute(key1, value1);

        String key2 = "key2";
        String value2 = "value2";
        Attribute attribute2 = new Attribute(key2, value2);

        Attribute attribute3 = new Attribute(key1, value1);
        Attribute attribute4 = new Attribute(key2, value1);

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();
        int hashCode4 = attribute4.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
        Assert.assertEquals(hashCode1, hashCode3);
        Assert.assertEquals(hashCode2, hashCode4);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute, clonedAttribute);
    }
}