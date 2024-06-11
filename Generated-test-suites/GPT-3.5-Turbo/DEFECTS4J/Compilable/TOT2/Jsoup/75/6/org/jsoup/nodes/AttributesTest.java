package org.jsoup.nodes;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
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
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", true);
        assertTrue(attributes.hasKeyIgnoreCase("key4"));
        assertFalse(attributes.hasKeyIgnoreCase("key4WithDifferentCase"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key5", "value5");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key5"));
        assertEquals("value5", attributes.get("key5"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    // Additional test cases for edge cases, exception handling scenarios can be added here

}