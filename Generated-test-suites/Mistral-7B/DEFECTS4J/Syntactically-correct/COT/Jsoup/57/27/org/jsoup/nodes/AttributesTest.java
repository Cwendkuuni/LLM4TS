package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Attributes class
 */
public class AttributesTest {

    /**
     * Test method for {@link Attributes#get(String)}
     */
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);

        attributes = new Attributes();
        result = attributes.get("key");
        assertEquals("", result);
    }

    /**
     * Test method for {@link Attributes#getIgnoreCase(String)}
     */
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.getIgnoreCase("key");
        assertEquals("value", result);

        attributes = new Attributes();
        result = attributes.getIgnoreCase("Key");
        assertEquals("", result);
    }

    /**
     * Test method for {@link Attributes#put(String, String)}
     */
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);

        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<String> keys = new ArrayList<>();
        keys.add("key1");
        keys.add("key2");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        assertEquals(keys, attributes.asList().stream().map(Attribute::getKey).map(s -> s.toString()).toList());
        assertEquals(values, attributes.asList().stream().map(Attribute::getValue).toList());
    }

    /**
     * Test method for {@link Attributes#put(String, boolean)}
     */
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key1"));
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key"));
        assertTrue(attributes.hasKey("key1"));
    }

    /**
     * Test method for {@link Attributes#put(Attribute)}
     */
    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals(attributes.get("key"), attribute.getValue());

        attributes = new Attributes();
        attribute = new Attribute("key", "value1");
        attributes.put(attribute);
        Attribute newAttribute = new Attribute("key", "value2");
        attributes.put(newAttribute);
        List<String> keys = new ArrayList<>();
        keys.add("key");
        List<String> values = new ArrayList<>();
        values.add("value2");
        assertEquals(keys, attributes.asList().stream().map(Attribute::getKey).map(s -> s.toString()).toList());
        assertEquals(values, attributes.asList().stream().map(Attribute::getValue).toList());
    }

    /**
     * Test method for {@link Attributes#remove(String)}
     */
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    /**
     * Test method for {@link Attributes#removeIgnoreCase(String)}
     */
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKey("Key2"));
    }

    /**
     * Test method for {@link Attributes#hasKey(String)}
     */
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));

        attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));

        attributes = new Attributes();
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    /**
     * Test method for {@link Attributes#size()}
     */
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));

        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    /**
     * Test method for {@link Attributes#addAll(Attributes)}
     */
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        List<String> keys = new ArrayList<>();
        keys.add("key1");
        keys.add("key2");
        keys.add("key3");
        keys.add("key4");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        values.add("value3");
        values.add("value4");
        assertEquals(keys, attributes3.asList().stream().map(Attribute::getKey).map(s -> s.toString()).toList());
        assertEquals(values, attributes3.asList().stream().map(Attribute::getValue).toList());
    }

    /**
     * Test method for {@link Attributes#iterator()}
     */
    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> it = attributes.iterator();
        List<Attribute> actual = new ArrayList<>();
        while (it.hasNext()) {
            actual.add(it.next());
        }

        List<String> keys = new ArrayList<>();
        keys.add("key1");
        keys.add("key2");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        assertEquals(keys, actual.stream().map(Attribute::getKey).map(s -> s.toString()).toList());
        assertEquals(values, actual.stream().map(Attribute::getValue).toList());
    }

    /**
     * Test method for {@link Attributes#asList()}
     */
    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> actual = attributes.asList();

        List<String> keys = new ArrayList<>();
        keys.add("key1");
        keys.add("key2");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        assertEquals(keys, actual.stream().map(Attribute::getKey).map(s -> s.toString()).toList());
        assertEquals(values, actual.stream().map(Attribute::getValue).toList());
    }

    /**
     * Test method for {@link Attributes#dataset()}
     */
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        List<String> datasetKeys = new ArrayList<>();
        datasetKeys.add("data-key1");
        datasetKeys.add("data-key2");
        List<String> datasetValues = new ArrayList<>();
        datasetValues.add("value1");
        datasetValues.add("value2");
        assertEquals(datasetKeys, dataset.keySet().stream().map(s -> s.toString()).toList());
        assertEquals(datasetValues, dataset.values().stream().toList());
    }

    /**
     * Test method for {@link Attributes#html()}
     */
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();

        StringBuilder expected = new StringBuilder();
        expected.append(" ");
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.html(expected, new Document.OutputSettings());
        assertEquals(" key1=\"value1\" key2=\"value2\" ", expected.toString());
    }

    /**
     * Test method for {@link Attributes#toString()}
     */
    @Test
    public void testToString() {
        Attributes attributes = new Attributes();

        StringBuilder expected = new StringBuilder();
        expected.append(" ");
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(" key1=\"value1\" key2=\"value2\" ", attributes.toString());
    }
}