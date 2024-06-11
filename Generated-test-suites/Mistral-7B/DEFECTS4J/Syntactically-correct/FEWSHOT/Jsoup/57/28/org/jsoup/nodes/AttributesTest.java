package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.AbstractMap;
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
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "VALUE");
        assertEquals("VALUE", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testGetEmpty() {
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key", "VALUE");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKeyFalse() {
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBooleanTrue() {
        attributes.put("key", true);
        assertTrue(attributes.get("key").isBoolean());
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put("key", false);
        assertFalse(attributes.get("key").isBoolean());
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key").getValue());
    }

    @Test
    public void testPutNull() {
        attributes.put(null);
        assertNull(attributes.get(null));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key", "VALUE");
        attributes.removeIgnoreCase("key");
        assertEquals("", attributes.get("Key"));
    }

    @Test
    public void testRemoveIgnoreCaseDoesNotRemoveIfKeyDoesNotExist() {
        attributes.removeIgnoreCase("key");
        assertEquals("", attributes.get("Key"));
    }

    @Test
    public void testAddAll() {
        Attributes other = new Attributes();
        other.put("otherKey", "otherValue");

        attributes.addAll(other);
        assertEquals("otherValue", attributes.get("otherKey").getValue());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();
        assertEquals(2, clone.size());
        assertEquals("value1", clone.get("key1").getValue());
        assertEquals("value2", clone.get("key2").getValue());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
    }

    @Test
    public void testHtml() {
        attributes.put("key", "value");

        StringBuilder html = new StringBuilder();
        try {
            attributes.html(html, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(" key=\"value\"", html.toString());
    }

    @Test
    public void testEquals() {
        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");

        Attributes clone = new Attributes();
        clone.put("key1", "value1");
        clone.put("key2", "value2");

        Attributes otherDifferent = new Attributes();
        otherDifferent.put("key1", "value1");
        otherDifferent.put("key3", "value3");

        Attributes empty = new Attributes();

        assertEquals(attributes, attributes);
        assertEquals(attributes, clone);
        assertNotEquals(attributes, other);
        assertNotEquals(attributes, otherDifferent);
        assertNotEquals(attributes, empty);
    }

    @Test
    public void testHashCode() {
        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");

        Attributes clone = new Attributes();
        clone.put("key1", "value1");
        clone.put("key2", "value2");

        Attributes otherDifferent = new Attributes();
        otherDifferent.put("key1", "value1");
        otherDifferent.put("key3", "value3");

        Attributes empty = new Attributes();

        int hashCode = attributes.hashCode();
        assertEquals(hashCode, hashCode);
        assertEquals(hashCode, clone.hashCode());
        assertNotEquals(hashCode, other.hashCode());
        assertNotEquals(hashCode, otherDifferent.hashCode());
        assertNotEquals(hashCode, empty.hashCode());
    }
}