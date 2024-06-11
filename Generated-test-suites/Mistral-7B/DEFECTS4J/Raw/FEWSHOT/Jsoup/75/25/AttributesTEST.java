package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3"); // overwrite existing key
        attributes.put("key3", "value3");

        assertEquals(3, attributes.size());
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3"); // overwrite existing key

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key3")); // non-existent key
        assertEquals("", attributes.get("key4")); // non-existent key, case insensitive

        // test getIgnoreCase
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("key3"));
        assertEquals("", attributes.getIgnoreCase("key4"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3"); // overwrite existing key
        attributes.put("key3", "value3");

        // test put
        attributes.put("key4", "value4");
        assertEquals(4, attributes.size());

        // test putIgnoreCase
        attributes.putIgnoreCase("Key4", "value5");
        assertEquals(5, attributes.size());

        // test put with boolean value
        attributes.put("key6", true);
        assertEquals(5, attributes.size());
        assertTrue(attributes.hasKey("key6"));
        assertEquals("", attributes.get("key6"));

        // test put with existing boolean value
        attributes.put("key6", false);
        assertEquals(5, attributes.size());
        assertFalse(attributes.hasKey("key6"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3"); // overwrite existing key
        attributes.put("key3", "value3");

        // test remove by index
        attributes.remove(1);
        assertEquals(3, attributes.size());

        // test remove by key
        attributes.remove("key1");
        assertEquals(2, attributes.size());
        assertFalse(attributes.hasKey("key1"));

        // test removeIgnoreCase by key
        attributes.putIgnoreCase("key1", "value1");
        attributes.removeIgnoreCase("Key1");
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("Key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3"); // overwrite existing key
        attributes.put("key3", "value3");

        // test hasKey
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("key4"));
        assertFalse(attributes.hasKey("key5"));

        // test hasKeyIgnoreCase
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key4"));
        assertFalse(attributes.hasKeyIgnoreCase("key5"));
    }

    @Test
    public void testPutAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        Attributes attributes3 = new Attributes();
        attributes3.putAll(attributes1);
        attributes3.putAll(attributes2);

        assertEquals(4, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
        assertTrue(attributes3.hasKey("key3"));
        assertTrue(attributes3.hasKey("key4"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3"); // overwrite existing key
        attributes.put("key3", "value3");

        List<Map.Entry<String, String>> list = new ArrayList<>(attributes.asList());

        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
        for (Map.Entry<String, String> entry : list) {
            assertEquals(entry.getKey(), iterator.next().getKey());
            assertEquals(entry.getValue(), iterator.next().getValue());
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("data-key2", "value2");
        attributes.put("data-key3", "value3");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(3, dataset.size());
        assertTrue(dataset.containsKey("data-key2"));
        assertTrue(dataset.containsKey("data-key3"));
        assertFalse(dataset.containsKey("key1"));
    }
}