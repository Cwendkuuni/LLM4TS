package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testCapacity() {
        Attributes attributes = new Attributes();
        attributes.checkCapacity(10);
        String[] keys1 = attributes.keys;
        String[] vals1 = attributes.vals;
        assertEquals(10, keys1.length);
        assertEquals(10, vals1.length);

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.checkCapacity(5);
        String[] keys2 = attributes.keys;
        String[] vals2 = attributes.vals;
        assertEquals(5, keys2.length);
        assertEquals(5, vals2.length);
        assertEquals("key1", keys1[0]);
        assertEquals("value1", vals1[0]);
        assertEquals("key2", keys1[1]);
        assertEquals("value2", vals1[1]);
        assertNull(keys2[0]);
        assertNull(vals2[0]);
    }

    @Test
    public void testAdd() {
        Attributes attributes = new Attributes();
        attributes.add("key1", "value1");
        attributes.add("key2", "value2");
        assertEquals(2, attributes.size());
        assertEquals("key1", attributes.keys[0]);
        assertEquals("value1", attributes.vals[0]);
        assertEquals("key2", attributes.keys[1]);
        assertEquals("value2", attributes.vals[1]);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.putIgnoreCase("Key1", "value2");
        assertEquals(2, attributes.size());
        assertEquals("value2", attributes.get("key1"));
        assertEquals("value2", attributes.get("Key1"));
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.get("key").equals(""));
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key"));

        Attributes attributes2 = new Attributes();
        attributes2.put("key", false);
        assertTrue(attributes2.get("key").equals(""));
        assertFalse(attributes2.hasKey("key"));
        assertFalse(attributes2.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testPutDuplicateKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key1", "value2");
        assertEquals("value2", attributes.get("key1"));
        assertEquals("value2", attributes.get("Key1"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
        attributes.remove("key1");
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key1", "value2");
        assertEquals(2, attributes.size());
        attributes.removeIgnoreCase("key1");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key1"));
        assertEquals("value2", attributes.get("Key1"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveException() {
        Attributes attributes = new Attributes();
        attributes.remove(0);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY"));

        attributes.put("Key", "value");
        assertTrue(attributes.hasKey("Key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testHasKeyEmpty() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey(""));
        assertFalse(attributes.hasKeyIgnoreCase(""));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key", "value");
        assertEquals(1, attributes.size());

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
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);
        assertEquals(4, attributes1.size());
        assertEquals("value3", attributes1.get("key3"));
        assertEquals("value4", attributes1.get("key4"));
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));

        attributes2.remove("key3");
        attributes2.remove("key4");
        assertEquals(0, attributes2.size());
        attributes1.remove("key3");
        attributes1.remove("key4");
        assertEquals(2, attributes1.size());
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes cloned = attributes.clone();
        assertEquals(2, cloned.size());
        assertEquals("value1", cloned.get("key1"));
        assertEquals("value2", cloned.get("key2"));

        cloned.put("key3", "value3");
        assertEquals(3, cloned.size());
        assertEquals("value3", cloned.get("key3"));
        assertEquals(2, attributes.size());
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        assertEquals("", attributes.toString());

        attributes.put("key", "value");
        assertEquals("key=\"value\"", attributes.toString());

        attributes.put("key2", "value2");
        assertEquals("key=\"value\" key2=\"value2\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        Attributes attributes4 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key1", "value2");
        attributes4.put("key2", "value1");

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2);
        assertEquals(attributes2, attributes1);

        assertNotEquals(attributes1, attributes3);
        assertNotEquals(attributes1, attributes4);
        assertNotEquals(attributes3, attributes4);
        assertNotEquals(attributes4, attributes1);
    }

    @Test
    public void testEqualsEmpty() {
        Attributes attributes = new Attributes();
        Attributes emptyAttributes = new Attributes();

        assertEquals(attributes, emptyAttributes);
    }

    @Test
    public void testEqualsDifferentClass() {
        Attributes attributes = new Attributes();
        Object notAttributes = new Object();

        assertNotEquals(attributes, notAttributes);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key1", "value2");

        int hashCode1 = attributes1.hashCode();
        int hashCode2 = attributes2.hashCode();
        int hashCode3 = attributes3.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");
        attributes.put("KEY2", "value2");

        attributes.normalize();

        assertEquals("key1", attributes.keys[0]);
        assertEquals("value1", attributes.vals[0]);
        assertEquals("key2", attributes.keys[1]);
        assertEquals("value2", attributes.vals[1]);
    }

    @Test
    public void testDatasets() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testDatasetEmpty() {
        Attributes attributes = new Attributes();

        Map<String, String> dataset = attributes.dataset();

        assertEquals(0, dataset.size());
    }
}