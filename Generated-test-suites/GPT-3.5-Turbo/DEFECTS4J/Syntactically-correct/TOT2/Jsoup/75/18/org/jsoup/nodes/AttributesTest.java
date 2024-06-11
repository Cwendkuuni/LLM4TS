package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        attributes.put("newKey", "newValue");
        assertEquals("newValue", attributes.get("newKey"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("boolKey", true);
        assertTrue(attributes.hasKeyIgnoreCase("boolKey"));
        attributes.put("boolKey", false);
        assertFalse(attributes.hasKeyIgnoreCase("boolKey"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key3", "value3");
        attributes.put(attribute);
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY2");
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("invalidKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertFalse(attributes.hasKeyIgnoreCase("invalidKey"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        assertNotSame(attributes, clonedAttributes);
        assertEquals(attributes.size(), clonedAttributes.size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "dataValue");
        assertEquals("dataValue", attributes.get("data-key"));
    }
}