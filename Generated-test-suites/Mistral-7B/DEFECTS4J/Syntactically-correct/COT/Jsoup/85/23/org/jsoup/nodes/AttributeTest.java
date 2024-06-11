package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.Arrays;

/**
 * Tests for the Attribute class
 */
public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, parent.valueAt(0));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyOnCreation() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueOnCreation() {
        new Attribute("key", null);
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyOnSetting() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueOnSetting() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        OutputSettings out = new Document.OutputSettings();
        attribute.html(sb, out);
        String expected = String.format("%s=\"%s\"", attribute.getKey(), Entities.escape("value", true));
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneNotSupported() {
        new Attribute("key", "value").clone();
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", null);
        assertTrue(attribute.isDataAttribute());
        Attribute attribute1 = new Attribute("key", null);
        assertFalse(attribute1.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("boolean", "false");
        assertTrue(attribute.shouldCollapseAttribute(out));
        Attribute attribute1 = new Attribute("boolean", "true");
        assertFalse(attribute1.shouldCollapseAttribute(out));
        Attribute attribute2 = new Attribute("key", "value");
        assertFalse(attribute2.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("boolean", null);
        assertTrue(attribute.isBooleanAttribute());
        Attribute attribute1 = new Attribute("key", null);
        assertFalse(attribute1.isBooleanAttribute());
    }
}