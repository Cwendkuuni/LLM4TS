package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testAddAndGetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testAddAndGetBooleanAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.get("key1").isEmpty());
        assertEquals(null, attributes.get("key1"));
    }

    @Test
    public void testRemoveAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));

        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testRemoveBooleanAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testContainsAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllAttributes() {
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
    public void testCloneAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = attributes1.clone();

        assertEquals(attributes1.size(), attributes2.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes2.hasKey("key1"));
        assertTrue(attributes2.hasKey("key2"));
        assertEquals(attributes1.get("key1"), attributes2.get("key1"));
        assertEquals(attributes1.get("key2"), attributes2.get("key2"));
    }

    @Test
    public void testSerialization() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();
        Attributes attributes2 = new Attributes();
        attributes2.put(html);

        assertEquals(attributes.size(), attributes2.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes2.hasKey("key1"));
        assertTrue(attributes2.hasKey("key2"));
        assertEquals(attributes.get("key1"), attributes2.get("key1"));
        assertEquals(attributes.get("key2"), attributes2.get("key2"));
    }
}