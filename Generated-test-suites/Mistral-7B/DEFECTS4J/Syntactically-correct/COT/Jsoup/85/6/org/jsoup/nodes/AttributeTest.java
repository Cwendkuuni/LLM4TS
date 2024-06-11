package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Map;

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

    @Test
    public void testSetKeyMethod() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyMethodWithNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValueMethod() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueMethodWithNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expectedOutput = StringUtil.borrowBuilder();
        Attribute.html("key", "value", expectedOutput, null);
        String result = StringUtil.releaseBuilder(expectedOutput);
        assertEquals("<key value=\"value\"></key>", result);
    }

    @Test(expected = IOException.class)
    public void testHtmlMethodWithNullOutputSettings() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder output = StringUtil.borrowBuilder();
        Attribute.html(output, null);
    }

    @Test
    public void testIsDataAttributeMethod() {
        assertTrue(new Attribute("data-key", null).isDataAttribute());
        assertFalse(new Attribute("key", null).isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(new Attribute("booleanAttribute", null).shouldCollapseAttribute(outputSettings));
        assertFalse(new Attribute("key", "value").shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttributeDeprecatedMethod() {
        assertTrue(new Attribute("booleanAttribute", null).isBooleanAttribute());
        assertFalse(new Attribute("key", null).isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeStaticMethod() {
        assertTrue(Attribute.isBooleanAttribute("booleanAttribute"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(null));
        assertFalse(attribute1.equals("someString"));
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }
}