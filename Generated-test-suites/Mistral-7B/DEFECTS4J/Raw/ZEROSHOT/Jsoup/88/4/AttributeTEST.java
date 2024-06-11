package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import org.junit.Test;

/**
 * Test class for Attribute.
 */
public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertNull(attributeWithParent.getParent());
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
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
    }

    @Test
    public void testHtmlMethod() throws Exception {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder stringBuilder = StringUtil.borrowBuilder();
        attribute.html(stringBuilder, new Document("").outputSettings());
        String result = StringUtil.releaseBuilder(stringBuilder);
        assertEquals("key=\"value\"", result);
    }

    @Test
    public void testToStringMethod() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attribute attribute = new Attribute("key", "value");
        attribute.setParent(attributes);

        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.html);

        assertTrue(attribute.shouldCollapseAttribute(settings));
        settings.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(attribute.shouldCollapseAttribute(settings));
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

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNull(clonedAttribute.getParent());
    }

    @Test
    public void testMapEntryMethods() {
        Attribute attribute = new Attribute("key", "value");
        Map<String, Attribute> map = new HashMap<>();
        map.put(attribute.getKey(), attribute);

        assertEquals(attribute.getKey(), map.keySet().toArray()[0]);
        assertEquals(attribute, map.values().toArray()[0]);

        assertEquals(attribute.getKey(), attribute.getKey());
        assertEquals(attribute.getValue(), attribute.getValue());
    }
}