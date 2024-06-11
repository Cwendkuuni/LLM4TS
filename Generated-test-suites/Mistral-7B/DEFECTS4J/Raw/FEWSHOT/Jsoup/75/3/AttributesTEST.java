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
        assertEquals(2, attributes.size());
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", null);
        attributes.put("key2", "value3");
        assertEquals("value3", attributes.get("key2"));

        attributes.put("key3", "value4");
        assertEquals("value4", attributes.get("key3"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key2"));

        attributes.put("key3", null);
        attributes.put("KEY3", "value4");
        assertEquals("value4", attributes.getIgnoreCase("key3"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertNull(attributes.get("key"));

        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));

        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.removeIgnoreCase("key2");
        assertFalse(attributes.hasKey("key2"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("nonExistentKey"));

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKeyIgnoCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));

        attributes.remove("key1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> list = attributes.asList();
        Iterator<Map.Entry<String, String>> iterator = list.iterator();

        assertEquals("key1", iterator.next().getKey());
        assertEquals("value1", iterator.next().getValue());
        assertEquals("key2", iterator.next().getKey());
        assertEquals("value2", iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
        assertFalse(dataset.containsKey("key1"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes expected = new Attributes();
        expected.put("key1", "value1");
        expected.put("key2", "value2");

        assertEquals(expected.html(), attributes.html());
    }
}