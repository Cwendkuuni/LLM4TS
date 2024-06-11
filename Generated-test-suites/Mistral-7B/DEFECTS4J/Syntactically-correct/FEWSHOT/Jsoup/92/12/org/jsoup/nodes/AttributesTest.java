package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import java.util.Arrays;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testInitialize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
        assertFalse(attributes.hasKey("key1")); // size has been updated
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertNull(attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        assertFalse(attributes.get("key").equals(""));
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutBooleanRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutBooleanReplace() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        attributes.put("key", true);
        assertTrue(attributes.get("key").equals(""));
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutAndRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutAndRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("Key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("other"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
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
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key3", "value3");
        attributes1.addAll(attributes2);
        assertEquals(3, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        Attribute attribute = iterator.next();
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
        attribute = iterator.next();
        assertEquals("key2", attribute.getKey());
        assertEquals("value2", attribute.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        Attribute attribute = list.get(0);
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
        attribute = list.get(1);
        assertEquals("key2", attribute.getKey());
        assertEquals("value2", attribute.getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> data = attributes.dataset();
        assertEquals("value1", data.get("data-key1"));
        assertEquals("value2", data.get("data-key2"));
    }

    @Test
    public void testDataKey() {
        String dataKey = Attributes.dataKey("key");
        assertEquals("data-key", dataKey);
    }
}