package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();

        // Test when attribute key is not set
        String result1 = attributes.get("key");
        assertEquals("", result1);

        // Test when attribute key is set
        attributes.put("key", "value");
        String result2 = attributes.get("key");
        assertEquals("value", result2);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();

        // Test when attribute key is not set
        String result1 = attributes.getIgnoreCase("Key");
        assertEquals("", result1);

        // Test when attribute key is set
        attributes.put("Key", "value");
        String result2 = attributes.getIgnoreCase("kEy");
        assertEquals("value", result2);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();

        // Test when attribute key is not set
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("key"));

        // Test when attribute key is already set
        attributes.put("key", "newValue");
        assertTrue(attributes.hasKey("key"));
        assertEquals(1, attributes.size());
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        // Test when value is true
        attributes.put("key", true);
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertEquals(1, attributes.size());
        assertEquals("", attributes.getIgnoreCase("key"));

        // Test when value is false
        attributes.put("key", false);
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");

        // Test when attribute key is not set
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key"));
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("key"));

        // Test when attribute key is already set
        attribute.setValue("newValue");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key"));
        assertEquals(1, attributes.size());
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        // Test when attribute key exists
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertEquals(0, attributes.size());

        // Test when attribute key does not exist
        attributes.remove("nonExistentKey");
        assertFalse(attributes.hasKey("nonExistentKey"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        // Test when attribute key exists
        attributes.removeIgnoreCase("Key");
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertEquals(0, attributes.size());

        // Test when attribute key does not exist
        attributes.removeIgnoreCase("NonExistentKey");
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        // Test when attribute key exists
        assertTrue(attributes.hasKey("key"));

        // Test when attribute key does not exist
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        // Test when attribute key exists
        assertTrue(attributes.hasKeyIgnoreCase("kEy"));

        // Test when attribute key does not exist
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }
    
    // Add more test cases for the remaining methods
    
}