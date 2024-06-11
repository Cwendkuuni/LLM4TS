package org.jsoup.nodes.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String key = attribute.getKey();
        String value = attribute.getValue();
        assertEquals("testKey", key);
        assertEquals("testValue", value);
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("testKey", "testValue", parentAttributes);
        String key = attribute.getKey();
        String value = attribute.getValue();
        assertEquals("testKey", key);
        assertEquals("testValue", value);
        assertEquals(1, parentAttributes.size());
        assertEquals(attribute, parentAttributes.valueAt(0));
    }

    // Test getKey(), setKey(), getValue(), and setValue() methods
    // Test html() method with a valid key and value

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyStringKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("");
    }

    @Test
    public void testSetKeyWithDifferentValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("newKey");
        String newKey = attribute.getKey();
        String newValue = attribute.getValue();
        assertEquals("newKey", newKey);
        assertEquals("testValue", value); // value should not be changed
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueWithEmptyStringValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setValue("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueWithParentAlreadyContainsKey() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute1 = new Attribute("testKey1", "testValue1", parentAttributes);
        Attribute attribute2 = new Attribute("testKey1", "testValue2", parentAttributes);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromEncodedWithNullParent() {
        Attribute attribute = Attribute.createFromEncoded("testKey", "<script>alert('XSS')</script>");
    }

    @Test
    public void testIsDataAttributeWithDataAttributeKey() {
        assertTrue(Attribute.isDataAttribute("data-test"));
    }

    @Test
    public void testIsDataAttributeWithNonDataAttributeKey() {
        assertFalse(Attribute.isDataAttribute("test"));
    }

    @Test
    public void testShouldCollapseAttributeWithHtmlOutputSettings() {
        assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.html()));
    }

    @Test
    public void testShouldCollapseAttributeWithNonHtmlOutputSettings() {
        assertFalse(attribute.shouldCollapseAttribute(Document.OutputSettings.xml()));
    }

    
}