package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        assertEquals(3, attributes.size());
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("nonexistentkey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value2");
        attributes.put("key2", "value3");

        assertEquals("value3", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY1", "value3");

        assertEquals("value3", attributes.getIgnoreCase("key1"));
        assertEquals("value3", attributes.getIgnoreCase("KEY1"));
        assertEquals("", attributes.getIgnoreCase("nonexistentkey"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        attributes.put("key1", false);

        assertFalse(attributes.get("key1").equals("true"));
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.get("key1").isEmpty());

        attributes.put("key2", false);
        attributes.put("key2", true);

        assertTrue(attributes.get("key2").isEmpty());
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key1", "value1"));
        attributes.put(new Attribute("key2", "value2"));

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        attributes.remove("key1");
        assertEquals("", attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY1", "value2");

        attributes.removeIgnoreCase("key1");
        assertEquals("", attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));

        attributes.removeIgnoreCase("KEY1");
        assertEquals("", attributes.get("KEY1"));
        assertFalse(attributes.hasKey("KEY1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKey("nonexistentkey"));
        assertFalse(attributes.hasKeyIgnoreCase("NONEEXISTENTKEY"));
    }

    @Test
    public void testRemoveByIndex() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        attributes.remove(1);

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value3", attributes.get("key3"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);

        assertEquals(4, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
        assertTrue(attributes1.hasKey("key4"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test", "test");
        attributes.put("data-test2", "test2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-test"));
        assertTrue(dataset.containsKey("data-test2"));
    }
}