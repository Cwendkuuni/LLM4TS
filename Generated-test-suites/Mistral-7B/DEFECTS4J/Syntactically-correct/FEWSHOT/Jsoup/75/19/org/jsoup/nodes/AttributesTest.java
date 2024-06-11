package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertNull(attributes.get("key2"));

        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.putIgnoreCase("Key1", "value1");
        assertEquals("value1", attributes.get("Key1"));
        assertNull(attributes.get("key2"));

        attributes.putIgnoreCase("key2", "value2");
        assertEquals("value1", attributes.get("Key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutBooleanValue() {
        Attributes attributes = new Attributes();

        attributes.put("key", true);
        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));

        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
        assertNull(attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value, attributes);

        attributes.put(attribute);
        assertEquals(key, attributes.get(key));
        assertEquals(value, attributes.get(key));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertNull(attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.remove("key2");
        assertNull(attributes.get("key1"));
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("key2", "value2");

        attributes.removeIgnoreCase("key1");
        assertNull(attributes.get("Key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.removeIgnoreCase("KEY2");
        assertNull(attributes.get("Key1"));
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("key3"));

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));

        attributes.remove("Key1");
        assertFalse(attributes.hasKeyIgnoreCase("Key1"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> entries = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        );
        List<Map.Entry<String, String>> expectedEntries = Collections.unmodifiableList(entries);

        assertEquals(expectedEntries, attributes.asList());

        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
        List<Map.Entry<String, String>> actualEntries = new ArrayList<>();
        while (iterator.hasNext()) {
            actualEntries.add(iterator.next());
        }

        assertEquals(expectedEntries, actualEntries);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }
}