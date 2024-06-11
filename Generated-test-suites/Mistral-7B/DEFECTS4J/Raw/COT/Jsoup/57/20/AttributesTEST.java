package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.DataAttribute;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Test class for Attributes class
 */
public class AttributesTest {

    /**
     * Test constructor, get, put, remove, hasKey, size methods
     */
    @Test
    public void testBasicFunctionality() {
        Attributes attributes = new Attributes();

        // Test getting an empty map
        assertEquals(0, attributes.size());
        assertNull(attributes.get("test"));
        assertFalse(attributes.hasKey("test"));

        // Test setting an attribute
        attributes.put("test", "value");
        assertEquals("value", attributes.get("test"));
        assertTrue(attributes.hasKey("test"));
        assertEquals(1, attributes.size());

        // Test setting the same attribute again
        attributes.put("test", "newValue");
        assertEquals("newValue", attributes.get("test"));

        // Test removing an attribute
        attributes.remove("test");
        assertNull(attributes.get("test"));
        assertFalse(attributes.hasKey("test"));
        assertEquals(0, attributes.size());

        // Test getting an attribute that does not exist
        assertNull(attributes.get("nonExistent"));
        assertFalse(attributes.hasKey("nonExistent"));

        // Test getting an attribute by case-insensitive key
        attributes.putIgnoreCase("Test", "value");
        assertEquals("value", attributes.getIgnoreCase("test"));

        // Test setting a boolean attribute
        attributes.put("booleanTest", true);
        assertTrue(attributes.hasKey("booleanTest"));
        assertFalse(attributes.get("booleanTest").isDataAttribute());
        assertNull(attributes.get("booleanTest").getValue());

        // Test removing a boolean attribute
        attributes.remove("booleanTest");
        assertFalse(attributes.hasKey("booleanTest"));
        assertNull(attributes.get("booleanTest"));

        // Test adding multiple attributes
        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "value2");
        attributes2.put("test3", "value3");
        attributes.addAll(attributes2);
        assertEquals("value2", attributes.get("test2"));
        assertEquals("value3", attributes.get("test3"));
        assertEquals(3, attributes.size());

        // Test getting an iterator and checking attribute values
        List<Attribute> attributesList = attributes.asList();
        for (Attribute attribute : attributesList) {
            if (attribute.getKey().equals("test")) {
                assertEquals("newValue", attribute.getValue());
            } else if (attribute.getKey().equals("test2")) {
                assertEquals("value2", attribute.getValue());
            } else if (attribute.getKey().equals("test3")) {
                assertEquals("value3", attribute.getValue());
            }
        }

        // Test clone method
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
        assertEquals(attributes.size(), clonedAttributes.size());
    }

    /**
     * Test dataset method
     */
    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("dataValue1", dataset.get("dataKey1"));
        assertEquals("dataValue2", dataset.get("dataKey2"));
    }

    /**
     * Test html method
     */
    @Test
    public void testHtmlMethod() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals("<test>value</test>", attributes.html());

        Document document = new Document("");
        attributes.html(document.appendElement("div").html(), document.outputSettings());
        assertEquals("<div test=\"value\"></div>", document.body().html());
    }

    /**
     * Test equals and hashCode methods
     */
    @Test
    public void testEqualsAndHashCodeMethods() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("test", "value1");
        attributes2.put("test", "value2");
        attributes3.put("test", "value1");

        assertEquals(attributes1, attributes3);
        assertNotSame(attributes1, attributes2);
        assertEquals(attributes1.hashCode(), attributes3.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    /**
     * Test getAttribute method when the attribute is not present
     */
    @Test
    public void testGetNonExistentAttribute() {
        Attributes attributes = new Attributes();
        assertNull(attributes.getAttribute("test"));
    }

    /**
     * Test getAttribute method when the attribute is present
     */
    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals("value", attributes.getAttribute("test"));
    }

    /**
     * Test getAttribute method with case-insensitive key
     */
    @Test
    public void testGetAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals("value", attributes.getAttributeIgnoreCase("test"));
        assertEquals("value", attributes.getAttributeIgnoreCase("Test"));
    }

    /**
     * Test put method with existing key
     */
    @Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");
        attributes.put("test", "value2");
        assertEquals("value2", attributes.get("test"));
    }

    /**
     * Test put method with null key
     */
    @Test(expected = NullPointerException.class)
    public void testPutWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    /**
     * Test put method with null value
     */
    @Test
    public void testPutWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("test", null);
        assertNull(attributes.get("test"));
    }

    /**
     * Test put method with data- attribute
     */
    @Test
    public void testPutWithDataAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-test", "value");
        assertTrue(attributes.get("data-test").isDataAttribute());
        assertEquals("value", attributes.get("data-test").getValue());
    }

    /**
     * Test put method with boolean attribute
     */
    @Test
    public void testPutWithBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanTest", true);
        assertTrue(attributes.hasKey("booleanTest"));
        assertFalse(attributes.get("booleanTest").isDataAttribute());
        assertNull(attributes.get("booleanTest").getValue());
    }

    /**
     * Test remove method with non-existent key
     */
    @Test
    public void testRemoveWithNonExistentKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("test"));
    }

    /**
     * Test remove method with existent key
     */
    @Test
    public void testRemoveWithExistentKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.remove("test");
        assertFalse(attributes.hasKey("test"));
    }

    /**
     * Test remove method with case-insensitive key
     */
    @Test
    public void testRemoveWithCaseInsensitiveKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.removeIgnoreCase("Test");
        assertFalse(attributes.hasKey("test"));
    }

    /**
     * Test hasKey method with non-existent key
     */
    @Test
    public void testHasKeyWithNonExistentKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("test"));
    }

    /**
     * Test hasKey method with existent key
     */
    @Test
    public void testHasKeyWithExistentKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertTrue(attributes.hasKey("test"));
    }

    /**
     * Test hasKey method with case-insensitive key
     */
    @Test
    public void testHasKeyWithCaseInsensitiveKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertTrue(attributes.hasKeyIgnoreCase("Test"));
    }

    /**
     * Test size method with empty map
     */
    @Test
    public void testSizeWithEmptyMap() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
    }

    /**
     * Test size method with non-empty map
     */
    @Test
    public void testSizeWithNonEmptyMap() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        assertEquals(2, attributes.size());
    }

    /**
     * Test addAll method with empty map to add
     */
    @Test
    public void testAddAllWithEmptyMap() {
        Attributes attributes = new Attributes();
        attributes.addAll(new Attributes());
        assertEquals(0, attributes.size());
    }

    /**
     * Test addAll method with non-empty map to add
     */
    @Test
    public void testAddAllWithNonEmptyMap() {
        Attributes attributes = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes2.put("test1", "value1");
        attributes2.put("test2", "value2");
        attributes.addAll(attributes2);
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("test1"));
        assertEquals("value2", attributes.get("test2"));
    }

    /**
     * Test clone method with empty map
     */
    @Test
    public void testCloneWithEmptyMap() {
        Attributes attributes = new Attributes();
        Attributes clonedAttributes = attributes.clone();
        assertEquals(0, attributes.size());
        assertEquals(0, clonedAttributes.size());
    }

    /**
     * Test clone method with non-empty map
     */
    @Test
    public void testCloneWithNonEmptyMap() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(2, attributes.size());
        assertEquals(2, clonedAttributes.size());
        assertEquals("value1", attributes.get("test1"));
        assertEquals("value1", clonedAttributes.get("test1"));
        assertEquals("value2", attributes.get("test2"));
        assertEquals("value2", clonedAttributes.get("test2"));
    }
}