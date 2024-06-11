package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttributesTest {

    @Test
    public void testSizeAndCapacity() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        assertEquals(3, attributes.size());

        attributes.addAll(new Attributes().put("key4", "value4").put("key5", "value5"));

        assertEquals(5, attributes.size());

        attributes.checkCapacity(10);

        assertEquals(5, attributes.keys.length);
        assertEquals(5, attributes.vals.length);
    }

    @Test
    public void testGetAndPut() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));

        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.put("key1", "value3");
        assertEquals("value3", attributes.get("key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.putIgnoreCase("Key1", "value1");
        assertEquals("value1", attributes.get("Key1"));
        assertEquals("", attributes.get("key1"));

        attributes.putIgnoreCase("Key1", "value2");
        assertEquals("value2", attributes.get("Key1"));
        assertEquals("value1", attributes.get("key1"));

        attributes.putIgnoreCase("Key1", null);
        assertEquals("", attributes.get("Key1"));
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        attributes.put("key1", true);
        assertTrue(attributes.getBoolean("key1"));
        assertEquals("", attributes.get("key1"));

        attributes.put("key1", false);
        assertFalse(attributes.getBoolean("key1"));
        assertEquals("", attributes.get("key1"));

        attributes.put("key2", false);
        attributes.put("key2", true);
        assertTrue(attributes.getBoolean("key2"));
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();

        attributes.put(new Attribute("key1", "value1"));
        assertEquals("value1", attributes.get("key1"));

        attributes.put(new Attribute("key2", null));
        assertNull(attributes.get("key2"));

        attributes.put(new Attribute("key1", null));
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes().put("key1", "value1").put("key2", "value2");

        attributes.remove("key1");
        assertEquals("value2", attributes.get("key2"));
        assertNull(attributes.get("key1"));

        attributes.remove("key2");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("key1"));
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes().put("key1", "value1").put("key2", "value2");

        attributes.removeIgnoreCase("key1");
        assertEquals("value2", attributes.get("key2"));
        assertNull(attributes.get("key1"));

        attributes.removeIgnoreCase("KEY1");
        assertEquals("value2", attributes.get("key2"));
        assertNull(attributes.get("key1"));
        assertNull(attributes.get("KEY1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        attributes.put("key2", "value2");
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKeyEmptyValue() {
        Attributes attributes = new Attributes().put("key1", "").put("key2", "");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));

        attributes.put("key1", null);
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKeyBooleanValue() {
        Attributes attributes = new Attributes();

        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));

        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attributes2 = new Attributes().put("key3", "value3").put("key4", "value4");

        Attributes merged = new Attributes();
        merged.addAll(attributes1);
        merged.addAll(attributes2);

        assertEquals(4, merged.size());
        assertEquals("value1", merged.get("key1"));
        assertEquals("value2", merged.get("key2"));
        assertEquals("value3", merged.get("key3"));
        assertEquals("value4", merged.get("key4"));

        List<String> keys = new ArrayList<>(Arrays.asList("key5", "key6"));
        Attributes empty = new Attributes();
        empty.addAll(keys.stream().map(k -> new Attributes().put(k, "")));
        merged.addAll(empty);

        assertEquals(6, merged.size());
        assertTrue(merged.hasKey("key5"));
        assertTrue(merged.hasKeyIgnoreCase("key5"));
        assertTrue(merged.hasKey("key6"));
        assertTrue(merged.hasKeyIgnoreCase("key6"));
        assertNull(merged.get("key5"));
        assertNull(merged.get("key6"));
    }

    @Test
    public void testIterable() {
        Attributes attributes = new Attributes().put("key1", "value1").put("key2", "value2");

        int count = 0;
        for (Attribute attribute : attributes) {
            assertEquals(count++ % 2, attribute.getValue().length());
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes().put("key1", "value1").put("key2", "value2");

        List<Attribute> list = attributes.asList();

        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes().put("data-key1", "value1").put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }
}