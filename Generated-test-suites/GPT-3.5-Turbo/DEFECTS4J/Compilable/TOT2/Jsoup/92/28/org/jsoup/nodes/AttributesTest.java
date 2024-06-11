package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        // Test case for getting an existing attribute by key
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        
        // Test case for getting a non-existent attribute
        assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Test case for getting an attribute by case-insensitive key
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        
        // Test case for getting a non-existent attribute by case-insensitive key
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
    }

    // More test cases for other public methods can be added here

}