package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.serialization.SerializationException;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    private Attributes attributes;
    private Attributes emptyAttributes;
    private Attributes attributesWithDataAttribute;
    private Attributes attributesWithBooleanAttribute;

    @Before
    public void setUp() {
        attributes = new Attributes();
        emptyAttributes = new Attributes();
        attributesWithDataAttribute = new Attributes();
        attributesWithDataAttribute.put("data-test", "test");
        attributesWithBooleanAttribute = new Attributes();
        attributesWithBooleanAttribute.put("test", true);
    }

    @Test
    public void testGetMethod() {
        String expected = "test";
        String result = attributes.get(expected);
        assertEquals(expected, result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        String key = "Test";
        String expected = "test";
        String result = attributes.getIgnoreCase(key);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMethodWithNullKey() {
        assertNull(attributes.get(null));
    }

    @Test
    public void testGetMethodWithEmptyKey() {
        assertNull(attributes.get(""));
    }

    @Test
    public void testGetMethodWithNullAttributes() {
        assertNull(emptyAttributes.get("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetMethodWithNull() {
        Attributes nullAttributes = null;
        nullAttributes.get("test");
    }

    @Test
    public void testPutMethod() {
        String key = "test";
        String value = "value";
        attributes.put(key, value);
        assertEquals(value, attributes.get(key));
    }

    @Test
    public void testPutMethodWithNullKey() {
        Validate.notNull(attributes);
        assertNull(attributes.get(null));
        attributes.put(null, "test");
        assertNull(attributes.get(null));
    }

    @Test(expected = NullPointerException.class)
    public void testPutMethodWithNullValue() {
        Validate.notNull(attributes);
        attributes.put(null, "test");
    }

    @Test
    public void testPutMethodWithNullAttributes() {
        Validate.notNull(attributes);
        Attributes nullAttributes = null;
        attributes.put("test", nullAttributes);
        assertNull(attributes.get("test"));
    }

    @Test
    public void testPutMethodWithSameKey() {
        String key = "test";
        String value1 = "value1";
        String value2 = "value2";
        attributes.put(key, value1);
        attributes.put(key, value2);
        assertEquals(value2, attributes.get(key));
    }

    @Test
    public void testPutMethodWithBooleanValue() {
        attributes.put("test", true);
        assertTrue(attributes.get("test").isBooleanAttribute());
    }

    @Test
    public void testPutMethodWithDataAttribute() {
        String key = "data-test";
        String value = "test";
        attributesWithDataAttribute.put(key, value);
        assertEquals(value, attributesWithDataAttribute.get(key));
        assertEquals("data-test", attributesWithDataAttribute.getIgnoreCase(key));
    }

    @Test
    public void testPutMethodWithBooleanAndDataAttribute() {
        attributesWithBooleanAttribute.put("test", "value");
        assertFalse(attributesWithBooleanAttribute.hasKey("test"));
        assertNotNull(attributesWithBooleanAttribute.getIgnoreCase("test"));
        assertEquals("value", attributesWithBooleanAttribute.getIgnoreCase("test"));
    }

    @Test
    public void testRemoveMethod() {
        attributes.put("test", "value");
        assertEquals("value", attributes.get("test"));
        attributes.remove("test");
        assertNull(attributes.get("test"));
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        attributes.put("test", "value");
        assertEquals("value", attributes.getIgnoreCase("test"));
        attributes.removeIgnoreCase("test");
        assertNull(attributes.get("test"));
    }

    @Test
    public void testHasKeyMethod() {
        assertTrue(attributes.hasKey("test"));
        assertFalse(attributes.hasKeyIgnoreCase("Test"));
        assertFalse(emptyAttributes.hasKey("test"));
        assertFalse(emptyAttributes.hasKeyIgnoreCase("Test"));
    }

    @Test
    public void testSizeMethod() {
        assertEquals(0, emptyAttributes.size());
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributesToAdd = new Attributes();
        attributesToAdd.put("test", "value");

        attributes.addAll(attributesToAdd);
        assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testAddAllMethodWithEmptyAttributes() {
        Attributes attributesToAdd = new Attributes();

        attributes.addAll(attributesToAdd);
        assertEquals(0, attributes.size());
    }

    @Test
    public void testIteratorMethod() {
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        List<Attribute> attributesList = attributes.asList();
        assertEquals("value1", attributesList.get(0).getValue());
        assertEquals("value2", attributesList.get(1).getValue());
    }

    @Test
    public void testAsListMethod() {
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        List<Attribute> attributesList = attributes.asList();
        assertEquals("value1", attributesList.get(0).getValue());
        assertEquals("value2", attributesList.get(1).getValue());
        assertFalse(attributesList.get(0).isDataAttribute());
        assertTrue(attributesList.get(1).isDataAttribute());
    }

    @Test
    public void testDatasetMethod() {
        attributes.put("data-test1", "test1");
        attributes.put("data-test2", "test2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals("test1", dataset.get("test1"));
        assertEquals("test2", dataset.get("test2"));
    }

    @Test
    public void testHtmlMethod() throws SerializationException {
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        String expectedHtml = " test1=\"value1\" test2=\"value2\"";
        String resultHtml = attributes.html();
        assertEquals(expectedHtml, resultHtml);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("test1", "value1");
        attributes2.put("test1", "value1");
        attributes3.put("test2", "value2");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(new Object()));
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");

        Attributes attributes2 = attributes1.clone();
        assertEquals("value1", attributes2.get("test1"));
    }

    @Test
    public void testDataSetIterator() {
        attributes.put("data-test1", "test1");
        attributes.put("data-test2", "test2");

        Map<String, String> dataset = attributes.dataset();
        Iterator<Map.Entry<String, String>> datasetIterator = dataset.entrySet().iterator();

        assertEquals("test1", datasetIterator.next().getValue());
        assertEquals("test2", datasetIterator.next().getValue());
        assertFalse(datasetIterator.hasNext());
    }

    @Test
    public void testCloneAndPutMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");

        Attributes attributes2 = attributes1.clone();
        attributes2.put("test2", "value2");

        assertEquals("value1", attributes1.get("test1"));
        assertEquals("value2", attributes2.get("test2"));
        assertFalse(attributes1.hasKey("test2"));
    }
}