package org.jsoup.nodes;

import static org.junit.Assert.*;
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
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("Key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key3", "value3");
        newAttributes.put("key4", "value4");

        attributes.addAll(newAttributes);
        assertEquals(2, attributes.size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        assertFalse(attributes.dataset().isEmpty());
        assertEquals(2, attributes.dataset().size());
        assertTrue(attributes.dataset().containsKey("key1"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        assertEquals(" key1=\"value1\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");

        attributes.put("key1", "value1");
        assertTrue(attributes.equals(newAttributes));
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        assertEquals(attributes.hashCode(), attributes.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();
        assertTrue(attributes.equals(clonedAttributes));
    }

    @Test
    public void testEdgeCases() {
        // Add edge case test scenarios here
    }

    @Test
    public void testExceptions() {
        // Add exception handling test scenarios here
    }

}