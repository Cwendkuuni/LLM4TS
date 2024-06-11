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

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("other"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("Key"));
        assertEquals("", attributes.getIgnoreCase("other"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals("value", attributes.get("key"));

        attributes.put("key", "newValue");

        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("other"));
        assertNull(attributes.get("other"));
        assertEquals("", attributes.get("other"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);

        assertEquals("value", attributes.get("key"));
        assertEquals(attribute, attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertFalse(attributes.hasKey("key3"));
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));

        attributes.remove("key1");

        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");

        assertFalse(attributes.hasKey("key3"));
        assertTrue(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKey("Key2"));

        attributes.removeIgnoreCase("Key1");

        assertFalse(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKey("Key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("other"));
        assertFalse(attributes.hasKey("otherIgnoreCase"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("Other"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);

        assertEquals(3, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(1, dataset.size());
        assertTrue(dataset.containsKey("data-key1"));
        assertEquals("value1", dataset.get("data-key1"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        StringBuilder expected = new StringBuilder();
        expected.append(" key=\"value\"");

        StringBuilder actual = new StringBuilder();
        attributes.html(actual, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes cloned = attributes.clone();

        assertEquals(attributes.size(), cloned.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(cloned.hasKey("key1"));
        assertEquals(attributes.get("key1"), cloned.get("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(cloned.hasKey("key2"));
        assertEquals(attributes.get("key2"), cloned.get("key2"));
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneException() {
        Attributes attributes = new Attributes();
        attributes.put("key", new Object());

        attributes.clone();

        fail("Should have thrown a CloneNotSupportedException");
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> list = new ArrayList<>(attributes.asList());

        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();

        assertEquals(list.get(0).getKey(), iterator.next().getKey());
        assertEquals(list.get(1).getKey(), iterator.next().getKey());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();

        assertEquals(2, list.size());
        assertEquals(attributes.get("key1"), list.get(0));
        assertEquals(attributes.get("key2"), list.get(1));
    }
}