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

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGetEmpty() {
        assertEquals("", attributes.get(""));
        assertEquals("", attributes.getIgnoreCase(""));
    }

    @Test
    public void testGetSet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testPutNull() {
        try {
            attributes.put(null, "value");
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            attributes.put("key", null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPutSameKey() {
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
        assertEquals("value2", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKey("other"));
        assertFalse(attributes.hasKeyIgnoreCase("other"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        attributes.remove("key");
        assertEquals("", attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        attributes.removeIgnoreCase("key");
        assertEquals("", attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes other = new Attributes();
        other.put("key3", "value3");

        attributes.addAll(other);
        assertEquals(3, attributes.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKey("key3"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        Iterator<Attribute> iterator = list.iterator();

        assertEquals("value1", iterator.next().getValue());
        assertEquals("value2", iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        Attribute attribute = list.get(0);

        assertEquals("value1", attribute.getValue());
        assertEquals("key1", attribute.getKey());

        assertEquals("value2", list.get(1).getValue());
        assertEquals("key2", list.get(1).getKey());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("key2"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() throws IOException {
        attributes.put("key", "value");

        StringBuilder output = new StringBuilder();
        attributes.html(output, null);

        assertEquals("<key>value</key>", output.toString());
    }

    @Test
    public void testEquals() {
        Attributes other = new Attributes();
        assertFalse(attributes.equals(null));
        assertFalse(attributes.equals(other));

        attributes.put("key", "value");
        other.put("key", "value");
        assertTrue(attributes.equals(other));
    }

    @Test
    public void testHashCode() {
        Attributes other = new Attributes();

        int hashCode1 = attributes.hashCode();
        attributes.put("key", "value");
        int hashCode2 = attributes.hashCode();

        assertEquals(hashCode1, attributes.hashCode());
        assertEquals(hashCode1, other.hashCode());
        other.put("key", "value");
        assertEquals(hashCode2, other.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes other = new Attributes();
        other.put("key", "value");

        Attributes cloned = attributes.clone();
        assertEquals("value", cloned.get("key"));

        // check that original and cloned have different hashcodes
        assertEquals(attributes.hashCode(), attributes.hashCode());
        assertNotSame(attributes.hashCode(), cloned.hashCode());
    }
}