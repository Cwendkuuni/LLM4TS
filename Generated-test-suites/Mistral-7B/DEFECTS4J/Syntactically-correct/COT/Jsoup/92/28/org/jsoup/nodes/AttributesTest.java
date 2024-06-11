package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);

        result = attributes.get("nonExistentKey");
        assertNull(result);

        attributes.put("key", null);
        result = attributes.get("key");
        assertNull(result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        String result = attributes.getIgnoreCase("key");
        assertEquals("value", result);

        result = attributes.getIgnoreCase("nonExistentKey");
        assertNull(result);
    }

    @Test
    public void testAdd() {
        Attributes attributes = new Attributes();
        attributes.add("key1", "value1");
        attributes.add("key2", "value2");
        assertEquals(2, attributes.size());

        String key1 = attributes.get("key1");
        assertEquals("value1", key1);

        String key2 = attributes.get("key2");
        assertEquals("value2", key2);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("key1", "Value2");
        assertEquals("Value2", attributes.get("Key1"));
        assertEquals("Value2", attributes.get("key1"));

        attributes.put("Key2", "Value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.get("key").equals("true"));
        assertNull(attributes.get("key")).isInstanceOf(Boolean.class);

        attributes.put("key", false);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key"));
        assertNull(attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);

        assertEquals("value", attributes.get("key"));
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("key1"));

        assertNotNull(attributes.get("key2"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.removeIgnoreCase("key1");
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("Key1"));

        assertNotNull(attributes.get("key2"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testIsEmpty() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.isEmpty());

        attributes.clear();

        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
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
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            list.add(entry);
        }

        assertEquals(2, list.size());
        assertEquals("key1", list.get(0).getKey());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("key2", list.get(1).getKey());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> result = attributes.dataset();
        assertEquals(2, result.size());

        assertEquals("value1", result.get("data-key1"));
        assertEquals("value2", result.get("data-key2"));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String result = attributes.toString();
        String expected = "Attributes{size=2, keys=[key1, key2], vals=[value1, value2]}";
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        Attributes attributes4 = new Attributes();
        attributes4.put("key2", "value2");
        attributes4.put("key3", "value3");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes4));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        Attributes attributes4 = new Attributes();
        attributes4.put("key2", "value2");
        attributes4.put("key3", "value3");

        int hashCode1 = attributes1.hashCode();
        int hashCode2 = attributes2.hashCode();
        int hashCode3 = attributes3.hashCode();
        int hashCode4 = attributes4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
        assertEquals(attributes.get("key2"), clonedAttributes.get("key2"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");
        attributes.put("key2", "value2");

        attributes.normalize();

        String key1 = attributes.get("key1");
        assertEquals("key1", key1);
        String key2 = attributes.get("key2");
        assertEquals("key2", key2);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlThrowsSerializationException() {
        Attributes attributes = new Attributes();
        StringBuilder sb = StringUtil.borrowBuilder();
        attributes.html(sb, null);
    }
}