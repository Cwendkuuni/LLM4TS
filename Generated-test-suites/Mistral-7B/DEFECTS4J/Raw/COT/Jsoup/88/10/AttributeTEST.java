package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributeTest {

    @Test
    public void testAttributeConstructor() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    @Test
    public void testAttributeConstructorWithParent() {
        String key = "testKey";
        String value = "testValue";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertEquals(attribute, parent.get(key));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyConstructor() {
        String value = "testValue";
        new Attribute(null, value);
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueConstructor() {
        String key = "testKey";
        new Attribute(key, null);
    }

    @Test
    public void testSetKey() {
        String originalKey = "testKey";
        String newKey = "newTestKey";
        Attribute attribute = new Attribute(originalKey, "testValue");
        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeySetter() {
        String originalKey = "testKey";
        Attribute attribute = new Attribute(originalKey, "testValue");
        attribute.setKey(null);
    }

    @Test
    public void testEmptyKeySetter() {
        String originalKey = "testKey";
        Attribute attribute = new Attribute(originalKey, "testValue");
        attribute.setKey("");
        assertEquals("", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        String originalValue = "testValue";
        String newValue = "newTestValue";
        Attribute attribute = new Attribute("testKey", originalValue);
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueSetter() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, "testValue");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        String key = "testKey";
        String value = "testValue";
        StringBuilder expected = StringUtil.borrowBuilder();
        Attribute attribute = new Attribute(key, value);
        attribute.html(expected, new Document("").outputSettings());
        String actual = StringUtil.releaseBuilder(expected);
        String expectedResult = key + "=\"testValue\"";
        assertEquals(expectedResult, actual);
    }

    @Test(expected = IOException.class)
    public void testHtmlWithException() throws IOException {
        String key = "testKey";
        StringBuilder expected = StringUtil.borrowBuilder();
        Attribute attribute = new Attribute(key, "testValue");
        attribute.html(expected, null);
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "testKey";
        String encodedValue = "testValue=%20test";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("test Value test", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        String key = "data-test";
        Attribute attribute = new Attribute(key, null);
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsNotDataAttribute() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        Attribute attribute = new Attribute("test", "");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        attribute.setVal("test");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        attribute.setVal("testValue");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        String key = "checked";
        Attribute attribute = new Attribute(key, null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsNotBooleanAttribute() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
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
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute3);
        Attributes attributesMap = new Attributes(attributes);
        assertEquals(attribute1, attribute3);
        assertNotEquals(attribute1, attribute2);
        assertNotEquals(attribute1, attribute4);
        assertNotEquals(attribute1, attribute5);
        assertEquals(attribute1, attribute1);
        assertEquals(attribute1, attribute3);
        assertEquals(attributesMap.get(key1), attribute1);
        assertEquals(attributesMap.get(key1), attribute3);
    }

    @Test
    public void testHashCode() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute1 = new Attribute(key1, value1);
        Attribute attribute2 = new Attribute(key2, value2);
        Attribute attribute3 = new Attribute(key1, value1);
        Attribute attribute4 = new Attribute(key1, value2);
        Attribute attribute5 = new Attribute(key2, value1);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute3);
        Attributes attributesMap = new Attributes(attributes);
        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();
        int hashCode4 = attribute4.hashCode();
        int hashCode5 = attribute5.hashCode();
        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
        assertEquals(hashCode1, hashCode1);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        Attribute clonedAttribute = attribute.clone();
        assertEquals(key, clonedAttribute.getKey());
        assertEquals(value, clonedAttribute.getValue());
    }
}