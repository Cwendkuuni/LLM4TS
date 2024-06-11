package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        // Test getting an existing attribute
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));

        // Test getting a non-existing attribute
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Test getting an existing attribute
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));

        // Test getting a non-existing attribute
        assertEquals("", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        // Test putting a new attribute
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        // Test replacing an existing attribute
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testRemove() {
        // Test removing an existing attribute
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        // Test removing a non-existing attribute
        attributes.remove("key2");
    }

    @Test
    public void testHasKey() {
        // Test checking for an existing attribute
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        // Test checking for a non-existing attribute
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testSize() {
        // Test getting the size of attributes
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        // Test adding attributes and getting the updated size
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testDataset() {
        // Test retrieving custom data attributes
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.dataset().get("key1"));
        assertEquals("value1", attributes.dataset().get("data-key1"));
        assertNull(attributes.dataset().get("key2"));
    }

    // Add more test cases for the remaining methods...

}