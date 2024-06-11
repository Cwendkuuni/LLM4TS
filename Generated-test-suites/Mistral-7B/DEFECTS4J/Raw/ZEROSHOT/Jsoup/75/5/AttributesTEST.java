package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.After
    public void tearDown() {
    }

    @org.junit.Test
    public void testInitialization() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
    }

    @org.junit.Test
    public void testGetAndPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @org.junit.Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));
        assertEquals(1, attributes.size());
    }

    @org.junit.Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "value");
        assertEquals("value", attributes.get("Key"));
        assertEquals("value", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @org.junit.Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @org.junit.Test
    public void testPutWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.hasKey("booleanKey") && attributes.get("booleanKey").isEmpty());
    }

    @org.junit.Test
    public void testPutAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.putAll(attributes1);
        attributes3.putAll(attributes2);

        assertEquals(2, attributes3.size());
        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));

        Attributes attributes4 = new Attributes();
        attributes4.putAll(Collections.emptyMap());
        assertEquals(0, attributes4.size());
    }

    @org.junit.Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedEntries = new ArrayList<>();
        expectedEntries.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        expectedEntries.add(new AbstractMap.SimpleEntry<>("key2", "value2"));

        for (Map.Entry<String, String> entry : attributes) {
            assertEquals(expectedEntries.remove(0).getKey(), entry.getKey());
            assertEquals(expectedEntries.remove(0).getValue(), entry.getValue());
        }
        assertTrue(expectedEntries.isEmpty());
    }

    @org.junit.Test
    public void testListView() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();

        assertEquals(2, attributesList.size());
        assertEquals("key1", attributesList.get(0).getKey());
        assertEquals("value1", attributesList.get(0).getValue());
        assertEquals("key2", attributesList.get(1).getKey());
        assertEquals("value2", attributesList.get(1).getValue());
    }

    @org.junit.Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("data-key1", dataset.keySet().iterator().next());
        assertEquals("data-value1", dataset.get("data-key1"));
        assertEquals("data-key2", dataset.keySet().iterator().next());
        assertEquals("data-value2", dataset.get("data-key2"));
    }
}