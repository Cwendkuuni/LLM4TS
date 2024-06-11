package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertNull(attributes.get("foo"));
        assertFalse(attributes.hasKey("foo"));
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        assertEquals(1, attributes.size());
        assertEquals("bar", attributes.get("foo"));
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        attributes.put("foo", "baz");
        assertEquals(1, attributes.size());
        assertEquals("baz", attributes.get("foo")); // the last one added wins
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("FOO", "BAR");
        assertEquals("BAR", attributes.getIgnoreCase("foo"));
        assertTrue(attributes.hasKeyIgnoreCase("FOO"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("foo", "bar"));
        assertEquals(1, attributes.size());
        assertEquals("bar", attributes.get("foo"));
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase(new Attribute("FOO", "BAR"));
        assertEquals(1, attributes.size());
        assertEquals("BAR", attributes.get("foo")); // the key is normalized to lower case before comparison
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("foo", true);
        assertEquals(1, attributes.size());
        assertNull(attributes.get("foo")); // get returns an empty string for boolean attributes
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testPutBooleanAttributeAndRemove() {
        Attributes attributes = new Attributes();
        attributes.put("foo", true);
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("foo"));
        attributes.remove("foo");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("foo"));
    }

    @Test
    public void testPutEmptyAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "");
        assertEquals(1, attributes.size());
        assertEquals("", attributes.get("foo"));
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testPutNullAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("foo", null);
        assertEquals(1, attributes.size());
        assertEquals(null, attributes.get("foo"));
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testPutNullValueAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("foo", null);
        attributes.put("foo", "bar");
        assertEquals(1, attributes.size());
        assertEquals("bar", attributes.get("foo"));
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testPutSameAttributeTwice() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        attributes.put("foo", "baz");
        assertEquals(1, attributes.size());
        assertEquals("baz", attributes.get("foo"));
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testPutWithInitialCapacity() {
        Attributes attributes = new Attributes(5);
        attributes.put("foo", "bar");
        attributes.put("foo", "baz");
        assertEquals(2, attributes.size());
        assertEquals("baz", attributes.get("foo"));
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("foo"));
        attributes.remove("foo");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("foo"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("FOO", "BAR");
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKeyIgnoreCase("foo"));
        attributes.removeIgnoreCase("foo");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKeyIgnoreCase("foo"));
    }

    @Test
    public void testRemoveNonExistent() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.remove("foo"));
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("foo"));
    }

    @Test
    public void testRemoveSameKeyTwice() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        attributes.put("foo", "baz");
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("foo"));
        attributes.remove("foo");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("foo"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("foo", "bar");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        assertEquals("foo=\"bar\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("foo", "bar");
        attributes2.put("foo", "bar");
        attributes3.put("bar", "foo");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("foo", "bar");
        attributes2.put("foo", "bar");
        attributes3.put("bar", "foo");
        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        Attributes cloned = attributes.clone();
        assertEquals(attributes.size(), cloned.size());
        assertEquals(attributes.get("foo"), cloned.get("foo"));
        assertEquals(attributes, cloned);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        attributes.put("data-foo", "baz");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals("baz", dataset.get("data-foo"));
        assertTrue(dataset.containsKey("data-foo"));
        assertFalse(dataset.containsKey("foo"));
    }

    @Test
    public void testDatasetEmpty() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testDatasetWithDataPrefix() {
        Attributes attributes = new Attributes();
        attributes.put("data-foo", "bar");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals("bar", dataset.get("data-foo"));
        assertTrue(dataset.containsKey("data-foo"));
    }

    @Test
    public void testDatasetWithOutDataPrefix() {
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testDatasetWithDataPrefixAndOtherAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-foo", "bar");
        attributes.put("foo", "baz");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals("bar", dataset.get("data-foo"));
        assertTrue(dataset.containsKey("data-foo"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("FOO", "BAR");
        attributes.normalize();
        assertEquals("foo", attributes.get("foo"));
        assertTrue(attributes.hasKey("foo"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("foo", "bar");
        attributes2.put("baz", "qux");
        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        assertEquals("bar", attributes1.get("foo"));
        assertEquals("qux", attributes1.get("baz"));
        assertTrue(attributes1.hasKey("foo"));
        assertTrue(attributes1.hasKey("baz"));
    }

    @Test
    public void testAddAllEmpty() {
        Attributes attributes = new Attributes();
        Attributes emptyAttributes = new Attributes();
        attributes.addAll(emptyAttributes);
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAllSameAttributes() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("foo", "bar");
        attributes2.put("foo", "bar");
        attributes1.addAll(attributes2);
        assertEquals(1, attributes1.size());
        assertEquals("bar", attributes1.get("foo"));
        assertTrue(attributes1.hasKey("foo"));
    }
}