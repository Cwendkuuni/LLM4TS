package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;

public class AttributeTest {

    @org.junit.Test
    public void testAttributeCreation() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        Attribute attributeWithParent = new Attribute(key, value, new Attributes());

        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertEquals(key, attributeWithParent.getKey());
        assertEquals(value, attributeWithParent.getValue());

        assertEquals(attribute, attributeWithParent);
        assertEquals(attribute.hashCode(), attributeWithParent.hashCode());
    }

    @org.junit.Test
    public void testSetKey() {
        String initialKey = "initialKey";
        String value = "testValue";
        Attribute attribute = new Attribute(initialKey, value);

        String newKey = "newKey";
        attribute.setKey(newKey);

        assertEquals(newKey, attribute.getKey());
        assertNotEquals(initialKey, attribute.getKey());
    }

    @org.junit.Test
    public void testValueSetter() {
        String key = "testKey";
        String initialValue = "initialValue";
        String newValue = "newValue";
        Attribute attribute = new Attribute(key, initialValue);

        attribute.setValue(newValue);

        assertEquals(newValue, attribute.getValue());
        assertNotEquals(initialValue, attribute.getValue());
    }

    @org.junit.Test
    public void testHtml() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        StringBuilder expectedOutput = StringUtil.borrowBuilder();
        attribute.html(expectedOutput, new Document("").outputSettings());
        String actualOutput = StringUtil.releaseBuilder(expectedOutput);

        String expectedHtml = String.format("%s=\"%s\"", key, value);
        assertEquals(expectedHtml, actualOutput);
    }

    @org.junit.Test
    public void testShouldCollapseAttribute() {
        String key = "testKey";
        String value = "testValue";
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        boolean expectedResult = true; // default value is true for html syntax and empty or same value as key
        boolean result = Attribute.shouldCollapseAttribute(key, value, outputSettings);

        assertEquals(expectedResult, result);

        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        expectedResult = false;
        result = Attribute.shouldCollapseAttribute(key, value, outputSettings);

        assertEquals(expectedResult, result);

        key = "testBooleanAttribute";
        value = null;
        expectedResult = true;
        result = Attribute.shouldCollapseAttribute(key, value, outputSettings);

        assertEquals(expectedResult, result);

        value = "";
        expectedResult = true;
        result = Attribute.shouldCollapseAttribute(key, value, outputSettings);

        assertEquals(expectedResult, result);

        key = "booleanAttribute";
        value = "booleanAttribute";
        expectedResult = true;
        result = Attribute.shouldCollapseAttribute(key, value, outputSettings);

        assertEquals(expectedResult, result);

        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        expectedResult = false;
        result = Attribute.shouldCollapseAttribute(key, value, outputSettings);

        assertEquals(expectedResult, result);
    }

    @org.junit.Test
    public void testIsBooleanAttribute() {
        String key = "booleanAttribute";

        boolean expectedResult = true;
        boolean result = Attribute.isBooleanAttribute(key);

        assertEquals(expectedResult, result);

        key = "nonBooleanAttribute";
        expectedResult = false;
        result = Attribute.isBooleanAttribute(key);

        assertEquals(expectedResult, result);

        key = "";
        expectedResult = false;
        result = Attribute.isBooleanAttribute(key);

        assertEquals(expectedResult, result);
    }

    @org.junit.Test
    public void testCreateFromEncoded() {
        String unencodedKey = "unencodedKey";
        String encodedValue = "encodedValue";

        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("decodedValue", attribute.getValue()); // decoded value should be equal to the unencoded key
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

        assertTrue(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute2));
    }

    @org.junit.Test
    public void testHashCode() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";

        Attribute attribute1 = new Attribute(key1, value1);
        Attribute attribute2 = new Attribute(key2, value2);
        Attribute attribute3 = new Attribute(key1, value1);

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }

    @org.junit.Test
    public void testClone() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }
}