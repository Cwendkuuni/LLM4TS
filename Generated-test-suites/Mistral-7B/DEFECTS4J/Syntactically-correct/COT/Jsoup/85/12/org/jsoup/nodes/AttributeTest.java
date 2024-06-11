package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;

import java.util.Arrays;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

    @org.junit.Test
    public void testCreateAttribute() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    @org.junit.Test
    public void testCreateAttributeWithParent() {
        String key = "testKey";
        String value = "testValue";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertEquals(attribute, parent.get(key));
    }

    @org.junit.Test
    public void testSetKey() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        String newKey = "newTestKey";
        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSetKeyNull() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        attribute.setKey(null);
    }

    @org.junit.Test
    public void testSetValue() {
        String key = "testKey";
        String oldValue = "oldTestValue";
        String newValue = "newTestValue";
        Attribute attribute = new Attribute(key, oldValue);
        assertEquals(oldValue, attribute.getValue());
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals(oldValue, attribute.getOldValue());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSetValueNull() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);
        attribute.setValue(null);
    }

    @org.junit.Test
    public void testHtml() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb);
        assertEquals("testKey=\"testValue\"", StringUtil.releaseBuilder(sb).toString());
    }

    @org.junit.Test
    public void testHtmlWithOutputSettings() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        Document document = new Document();
        attribute.html(document.outputSettings());
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, document.outputSettings());
        assertEquals("testKey=\"testValue\"", StringUtil.releaseBuilder(sb).toString());
    }

    @org.junit.Test
    public void testHtmlCollapsed() throws IOException {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document.OutputSettings());
        assertEquals(key, StringUtil.releaseBuilder(sb).toString());
    }

    @org.junit.Test
    public void testCreateFromEncoded() {
        String unencodedKey = "testKey";
        String encodedValue = "testValue=\"testValue\"";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    @org.junit.Test
    public void testIsDataAttribute() {
        String dataKey = "data-test";
        Attribute attribute = new Attribute(dataKey, null);
        assertTrue(attribute.isDataAttribute());

        String nonDataKey = "testKey";
        attribute = new Attribute(nonDataKey, null);
        assertFalse(attribute.isDataAttribute());
    }

    @org.junit.Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(Attribute.shouldCollapseAttribute("test", null, outputSettings));
        assertTrue(Attribute.shouldCollapseAttribute("test", "", outputSettings));
        assertFalse(Attribute.shouldCollapseAttribute("test", "test", outputSettings));
        assertFalse(Attribute.shouldCollapseAttribute("test", "testtest", outputSettings));
        assertFalse(Attribute.shouldCollapseAttribute("test", "Test", outputSettings));
    }

    @org.junit.Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("test", null);
        assertFalse(attribute.isBooleanAttribute());
    }

    @org.junit.Test
    public void testEquals() {
        String key1 = "test1";
        String value1 = "test1Value";
        Attribute attribute1 = new Attribute(key1, value1);

        String key2 = "test2";
        String value2 = "test2Value";
        Attribute attribute2 = new Attribute(key2, value2);

        Attribute attribute3 = new Attribute(key1, value1);

        assertEquals(attribute1, attribute3);
        assertNotEquals(attribute1, attribute2);
    }

    @org.junit.Test
    public void testClone() throws CloneNotSupportedException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        Attribute clonedAttribute = attribute.clone();
        assertEquals(key, clonedAttribute.getKey());
        assertEquals(value, clonedAttribute.getValue());
    }
}