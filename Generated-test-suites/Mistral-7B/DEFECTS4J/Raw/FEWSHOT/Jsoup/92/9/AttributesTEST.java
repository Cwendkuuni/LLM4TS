package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key"));
        assertEquals("value2", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "Value");
        assertEquals(1, attributes.size());
        assertEquals("Value", attributes.getIgnoreCase("Key"));
        assertEquals("Value", attributes.getIgnoreCase("KEY"));
        assertEquals("Value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertEquals(1, attributes.size());
        assertFalse(attributes.get("key").isEmpty());
        assertNull(attributes.get("key"));
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttributeWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key"));
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        attributes.removeIgnoreCase("Key1");
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("Key1"));
        assertEquals("Value2", attributes.get("Key2"));
    }

    @Test
    public void testContainsAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testCopyConstructor() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("key1", "value1");
        originalAttributes.put("key2", "value2");

        Attributes attributes = new Attributes(originalAttributes);
        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testClear() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.clear();
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
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

        Iterator<Map.Entry<String, String>> iterator = attributes.asList().iterator();
        for (Map.Entry<String, String> entry : entries) {
            assertEquals(entry.getKey(), iterator.next().getKey());
            assertEquals(entry.getValue(), iterator.next().getValue());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutAttributeWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
        assertEquals("", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutAttributeWithNullKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.put(null, "value");
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPutAttributeWithNullValue() {
        Attributes attributes = new Attributes();
        try {
            attributes.put("key", null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("key1=\"value1\" key2=\"value2\"", attributes.toString());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("key1", "value1");
        originalAttributes.put("key2", "value2");

        Attributes attributes = (Attributes) originalAttributes.clone();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testContainsAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> entries = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        );

        assertTrue(attributes.containsAll(entries));
    }

    @Test
    public void testRemoveAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<String> keysToRemove = Arrays.asList("key1");

        attributes.removeAll(keysToRemove);
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRetainAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<String> keysToKeep = Arrays.asList("key2");

        attributes.retainAll(keysToKeep);
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));
    }
}