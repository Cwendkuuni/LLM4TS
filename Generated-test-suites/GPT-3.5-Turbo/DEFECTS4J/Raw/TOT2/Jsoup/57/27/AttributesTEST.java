package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    // Test get method
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    // Test getIgnoreCase method
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("kEy2"));
    }

    // Test put method
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
    }

    // Test remove method
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key4", "value4");
        attributes.remove("key4");
        assertFalse(attributes.hasKey("key4"));
    }

    // Test size method
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key5", "value5");
        assertEquals(1, attributes.size());
    }

    // Test addAll method
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key6", "value6");

        Attributes attributes2 = new Attributes();
        attributes2.put("key7", "value7");

        attributes1.addAll(attributes2);
        assertTrue(attributes1.hasKey("key7"));
    }

    // Test dataset method
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key8", "value8");
        assertEquals("value8", attributes.dataset().get("key8"));
    }

    // Add more test cases here for remaining methods...

}