package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        Attribute attributeWithParent = new Attribute("key", "value", null);
        Attribute attributeWithParentAndKeyChange = new Attribute("newKey", "value", null);
        Attribute attributeWithParentAndValueChange = new Attribute("key", "newValue", null);

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertEquals("newKey", attributeWithParentAndKeyChange.getKey());
        assertEquals("value", attributeWithParentAndKeyChange.getValue());
        assertEquals("key", attributeWithParentAndValueChange.getKey());
        assertEquals("newValue", attributeWithParentAndValueChange.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");

        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyEmpty() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        attribute.setValue(newValue);

        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Attribute dataAttribute = new Attribute("data-key", "value");

        assertFalse(attribute.isDataAttribute());
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Attribute emptyBooleanAttribute = new Attribute("checked", "");
        Attribute nullBooleanAttribute = new Attribute("checked", null);

        assertFalse(attribute.shouldCollapseAttribute(null));
        assertTrue(emptyBooleanAttribute.shouldCollapseAttribute(null));
        assertTrue(nullBooleanAttribute.shouldCollapseAttribute(null));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Attribute booleanAttribute = new Attribute("checked", null);

        assertFalse(attribute.isBooleanAttribute());
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithEmptyStringValue() {
        Attribute attribute = new Attribute("checked", "");

        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value1");
        Attribute attribute4 = new Attribute("key1", "value2");

        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}