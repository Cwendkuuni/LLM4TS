package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertNull(attribute.getParent());
        assertNotNull(attributeWithParent.getParent());
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");

        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.getValue();

        attribute.setValue(newValue);

        assertEquals(newValue, attribute.getValue());
        assertEquals(oldValue, attribute.getValueBefore());
    }

    @Test
    public void testHtmlMethod() throws Exception {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder stringBuilder = StringUtil.borrowBuilder();

        attribute.html(stringBuilder, null);

        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, stringBuilder.toString());
    }

    @Test
    public void testCreateFromEncodedMethod() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("booleanAttribute", null);
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("notBooleanAttribute", "value");
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("booleanAttribute", null);
        assertTrue(attribute.shouldCollapseAttribute());

        attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute());
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

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNotSame(attribute, clonedAttribute);
    }
}