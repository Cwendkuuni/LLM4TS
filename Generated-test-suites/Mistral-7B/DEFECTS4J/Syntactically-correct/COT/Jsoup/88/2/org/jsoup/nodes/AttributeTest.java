package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

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

    @Test
    public void testSetKeyMethod() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.setKey("key2");
        assertEquals("key2", attribute.getKey());
    }

    @Test(expected = AssertionError.class)
    public void testSetKeyMethodWithNullKey() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.setKey(null);
    }

    @Test
    public void testSetValueMethod() {
        Attribute attribute = new Attribute("key", "value1");
        String newValue = "value2";
        String oldValue = attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals("value1", oldValue);
    }

    @Test(expected = AssertionError.class)
    public void testSetValueMethodWithNullValue() {
        Attribute attribute = new Attribute("key", "value1");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());

        attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(out));

        attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(out));

        attribute = new Attribute("key", "key");
        assertTrue(attribute.shouldCollapseAttribute(out));

        out.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttributeMethod() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeStaticMethod() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        assertFalse(attribute1.equals(null));
        assertFalse(attribute1.equals(attribute2));
        assertTrue(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        assertEquals(attribute1.hashCode(), attribute3.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}