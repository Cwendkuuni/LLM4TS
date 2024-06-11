package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Attributes.BooleanAttribute;
import org.jsoup.nodes.Attributes.Dataset;
import org.jsoup.nodes.Attributes.Dataset.DatasetEntry;
import org.jsoup.nodes.Attributes.Dataset.DatasetIterator;
import org.jsoup.nodes.Attributes.Attribute;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    // Get method tests

    @Test
    public void testGetEmpty() {
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGet() {
        attributes.put("testKey", "testValue");
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("TestKey", "TestValue");
        assertEquals("TestValue", attributes.getIgnoreCase("testKey"));
    }

    // Put method tests

    @Test
    public void testPut() {
        attributes.put("testKey", "testValue");
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("testKey", true);
        assertTrue(attributes.hasKey("testKey"));
        assertFalse(attributes.get("testKey").isStringValue());
        assertNull(attributes.get("testKey").getValue());
    }

    @Test
    public void testPutNull() {
        attributes.put("testKey", null);
        assertNull(attributes.get("testKey"));
    }

    // Put method with existing key tests

    @Test
    public void testPutWithExistingKeyStringValue() {
        attributes.put("testKey", "testValue");
        attributes.put("testKey", "newTestValue");
        assertEquals("newTestValue", attributes.get("testKey"));
    }

    @Test
    public void testPutWithExistingKeyBooleanValue() {
        attributes.put("testKey", true);
        attributes.put("testKey", false);
        assertFalse(attributes.get("testKey").isStringValue());
        assertNull(attributes.get("testKey").getValue());
    }

    // Remove method tests

    @Test
    public void testRemoveEmpty() {
        assertFalse(attributes.remove("nonExistentKey"));
    }

    @Test
    public void testRemove() {
        attributes.put("testKey", "testValue");
        assertTrue(attributes.remove("testKey"));
        assertFalse(attributes.hasKey("testKey"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("TestKey", "TestValue");
        assertTrue(attributes.removeIgnoreCase("testKey"));
        assertFalse(attributes.hasKey("TestKey"));
    }

    // HasKey method tests

    @Test
    public void testHasKeyEmpty() {
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKey() {
        attributes.put("testKey", "testValue");
        assertTrue(attributes.hasKey("testKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("TestKey", "TestValue");
        assertTrue(attributes.hasKeyIgnoreCase("testKey"));
    }

    // Size method test

    @Test
    public void testSizeEmpty() {
        assertEquals(0, attributes.size());
    }

    @Test
    public void testSizeWithAttributes() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        assertEquals(2, attributes.size());
    }

    // AddAll method tests

    @Test
    public void testAddAllEmpty() {
        Attributes emptyAttributes = new Attributes();
        attributes.addAll(emptyAttributes);
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAllWithAttributes() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("testKey1", "testValue1");
        otherAttributes.put("testKey2", "testValue2");

        attributes.addAll(otherAttributes);
        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("testKey1"));
        assertTrue(attributes.hasKey("testKey2"));
    }

    // Iterator method tests

    @Test
    public void testIteratorEmpty() {
        List<Attribute> emptyList = attributes.asList();
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testIteratorWithAttributes() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        Attribute first = list.get(0);
        assertEquals("testKey1", first.getKey());
        assertEquals("testValue1", first.getValue());

        Attribute second = list.get(1);
        assertEquals("testKey2", second.getKey());
        assertEquals("testValue2", second.getValue());
    }

    // Dataset method tests

    @Test
    public void testDatasetEmpty() {
        Map<String, String> dataset = attributes.dataset();
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testDatasetWithDataAttributes() {
        attributes.put(new Attribute("dataKey1", "dataValue1"));
        attributes.put(new Attribute("dataKey2", "dataValue2"));

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());

        String firstKey = dataset.keySet().iterator().next();
        String firstValue = dataset.get(firstKey);
        assertEquals("dataKey1", firstKey);
        assertEquals("dataValue1", firstValue);

        String secondKey = dataset.keySet().iterator().next();
        String secondValue = dataset.get(secondKey);
        assertEquals("dataKey2", secondKey);
        assertEquals("dataValue2", secondValue);
    }

    // HTML method tests

    @Test
    public void testHtmlEmpty() {
        assertEquals("", attributes.html());
    }

    @Test
    public void testHtmlWithAttributes() {
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        StringBuilder sb = new StringBuilder();
        attributes.html(sb, null);

        String expectedHtml = " testKey1=\"testValue1\" testKey2=\"testValue2\"";
        assertEquals(expectedHtml, sb.toString());
    }

    // Clone method tests

    @Test
    public void testCloneEmpty() {
        Attributes original = new Attributes();
        Attributes cloned = original.clone();
        assertEquals(0, cloned.size());
    }

    @Test
    public void testCloneWithAttributes() {
        Attributes original = new Attributes();
        original.put("testKey1", "testValue1");
        original.put("testKey2", "testValue2");

        Attributes cloned = original.clone();
        assertEquals(2, cloned.size());
        assertTrue(cloned.hasKey("testKey1"));
        assertTrue(cloned.hasKey("testKey2"));
    }
}