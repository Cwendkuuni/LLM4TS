package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 A single key + value attribute. (Only used for presentation.)
 */
public class AttributeTest {
    
    @Test
    public void testAttributeConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testAttributeConstructorWithKeyAndValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.getParent());
    }
    
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }
    
    @Test
    public void testHtmlWithEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        assertEquals("key", attribute.html());
    }
    
    @Test
    public void testHtmlWithNullValue() {
        Attribute attribute = new Attribute("key", null);
        assertEquals("key", attribute.html());
    }
    
    @Test
    public void testHtmlWithOutputSettings() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals("key=\"value\"", attribute.html(outputSettings));
    }
    
    @Test
    public void testHtmlWithOutputSettingsAndEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals("key", attribute.html(outputSettings));
    }
    
    @Test
    public void testHtmlWithOutputSettingsAndNullValue() {
        Attribute attribute = new Attribute("key", null);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals("key", attribute.html(outputSettings));
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&amp;value");
        assertEquals("key", attribute.getKey());
        assertEquals("&value", attribute.getValue());
    }
    
    @Test
    public void testCreateFromEncodedWithEmptyValue() {
        Attribute attribute = Attribute.createFromEncoded("key", "");
        assertEquals("key", attribute.getKey());
        assertEquals("", attribute.getValue());
    }
    
    @Test
    public void testCreateFromEncodedWithNullValue() {
        Attribute attribute = Attribute.createFromEncoded("key", null);
        assertEquals("key", attribute.getKey());
        assertNull(attribute.getValue());
    }
    
    @Test
    public void testCreateFromEncodedWithUnencodedKey() {
        Attribute attribute = Attribute.createFromEncoded("key&", "&amp;value");
        assertEquals("key&", attribute.getKey());
        assertEquals("&value", attribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }
    
    @Test
    public void testIsDataAttributeWithEmptyKey() {
        Attribute attribute = new Attribute("", "value");
        assertFalse(attribute.isDataAttribute());
    }
    
    @Test
    public void testIsDataAttributeWithNullKey() {
        Attribute attribute = new Attribute(null, "value");
        assertFalse(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }
    
    @Test
    public void testShouldCollapseAttributeWithEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }
    
    @Test
    public void testShouldCollapseAttributeWithNullValue() {
        Attribute attribute = new Attribute("key", null);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testIsBooleanAttributeWithNullValue() {
        Attribute attribute = new Attribute("key", null);
        assertTrue(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testIsBooleanAttributeWithBooleanAttributeKey() {
        Attribute attribute = new Attribute("checked", "value");
        assertTrue(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testEqualsWithSameObject() {
        Attribute attribute = new Attribute("key", "value");
        assertTrue(attribute.equals(attribute));
    }
    
    @Test
    public void testEqualsWithEqualObjects() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testEqualsWithDifferentObjects() {
        Attribute attribute1 = new Attribute("key1", "value");
        Attribute attribute2 = new Attribute("key2", "value");
        assertFalse(attribute1.equals(attribute2));
    }
    
    @Test
    public void testEqualsWithNull() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.equals(null));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(60980, attribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}