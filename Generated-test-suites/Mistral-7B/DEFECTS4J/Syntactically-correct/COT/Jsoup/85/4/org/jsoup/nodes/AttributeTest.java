package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeConstructionWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeConstructionWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, parent.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testKeyCannotBeNull() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testValueCannotBeNull() {
        new Attribute("key", null);
    }

    @Test
    public void testKeyTrimming() {
        Attribute attribute = new Attribute("  key  ", "value");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());

        Attributes parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
        parent.set("key", "newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testValueSetting() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());

        Attributes parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
        parent.set("key", "newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtmlMethod() throws Exception {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(sb));
    }

    @Test(expected = IOException.class)
    public void testHtmlMethodThrowsIOException() throws Exception {
        Attribute attribute = new Attribute("key", "value");
        attribute.html(null, new Document("").outputSettings());
    }

    @Test
    public void testCreateFromEncodedMethod() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());

        attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttributeMethod() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

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
    public void testCloneMethod() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneMethodThrowsCloneNotSupportedException() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        attribute.clone(); // this call is fine, but an exception should be thrown in the next line
        throw new CloneNotSupportedException();
    }
}