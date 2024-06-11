package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeConstructor() {
        String key = "key";
        String value = "value";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertSame(parent, attribute.parent);
    }

    @Test
    public void testSetKey() {
        String key = "key";
        String value = "value";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        String newKey = "newKey";
        attribute.setKey(newKey);
        
        assertEquals(newKey, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertEquals(newKey, parent.getKey(0));
    }

    @Test
    public void testSetValue() {
        String key = "key";
        String value = "value";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        
        assertEquals(key, attribute.getKey());
        assertEquals(newValue, attribute.getValue());
        assertEquals(newValue, parent.getValue(0));
        assertEquals(value, oldValue);
    }

    @Test
    public void testHtml() {
        String key = "key";
        String value = "value";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        String expectedHtml = key + "=\"" + value + "\"";
        
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testToString() {
        String key = "key";
        String value = "value";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        String expectedString = key + "=\"" + value + "\"";
        
        assertEquals(expectedString, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "key";
        String encodedValue = "value";
        
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        
        assertEquals(unencodedKey, attribute.getKey());
        assertEquals(encodedValue, attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        String key = "data-key";
        Attribute attribute = new Attribute(key, "value");
        
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        String key = "key";
        String value = "value";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        assertFalse(Attribute.shouldCollapseAttribute(key, value, outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        String[] booleanAttributes = {
            "allowfullscreen", "async", "autofocus", "checked", "compact", "declare", "default", "defer", "disabled",
            "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple", "muted", "nohref", "noresize",
            "noshade", "novalidate", "nowrap", "open", "readonly", "required", "reversed", "seamless", "selected",
            "sortable", "truespeed", "typemustmatch"
        };
        
        for (String attributeName : booleanAttributes) {
            assertTrue(Attribute.isBooleanAttribute(attributeName));
        }
        
        assertFalse(Attribute.isBooleanAttribute("notBooleanAttribute"));
    }

    @Test
    public void testEquals() {
        String key = "key";
        String value = "value";
        Attribute attribute1 = new Attribute(key, value);
        Attribute attribute2 = new Attribute(key, value);
        Attribute attribute3 = new Attribute(key, "differentValue");
        
        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCode() {
        String key = "key";
        String value = "value";
        Attribute attribute1 = new Attribute(key, value);
        Attribute attribute2 = new Attribute(key, value);
        
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);
        
        Attribute clonedAttribute = attribute.clone();
        
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}