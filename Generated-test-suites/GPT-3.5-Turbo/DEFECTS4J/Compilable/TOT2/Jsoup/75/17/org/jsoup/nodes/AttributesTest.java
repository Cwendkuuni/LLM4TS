package org.jsoup.nodes;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("nonExistingKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KeY2"));
        assertEquals("", attributes.getIgnoreCase("nonExistingKey"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", true);
        assertTrue(attributes.hasKeyIgnoreCase("Key4"));
        attributes.put("key4", false);
        assertFalse(attributes.hasKey("key4"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    // Add more test cases for other methods as needed

}