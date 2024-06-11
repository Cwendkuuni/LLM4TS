package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        
        // Test case for an existing attribute
        attributes.put("key1", "value1");
        assertEquals(attributes.get("key1"), "value1");

        // Test case for a non-existing attribute
        assertEquals(attributes.get("key2"), "");
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        
        // Test case for an existing attribute with different cases
        attributes.put("Key1", "value1");
        assertEquals(attributes.getIgnoreCase("key1"), "value1");

        // Test case for a non-existing attribute
        assertEquals(attributes.getIgnoreCase("key2"), "");
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        
        // Test case for a new attribute
        attributes.put("key1", "value1");
        assertEquals(attributes.get("key1"), "value1");

        // Test case for an existing attribute
        attributes.put("key1", "value2");
        assertEquals(attributes.get("key1"), "value2");
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        
        // Test case for a true boolean attribute
        attributes.put("key1", true);
        assertEquals(attributes.get("key1"), "");

        // Test case for a false boolean attribute (should be removed)
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        
        // Test case for putting a new attribute
        attributes.put(attribute);
        assertEquals(attributes.get("key1"), "value1");
        
        // Test case for replacing an existing attribute
        attribute.setValue("value2");
        attributes.put(attribute);
        assertEquals(attributes.get("key1"), "value2");
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        
        // Test case for removing an existing attribute
        attributes.put("key1", "value1");
        assertEquals(attributes.get("key1"), "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        // Test case for removing a non-existing attribute
        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        
        // Test case for removing an existing attribute with different cases
        attributes.put("Key1", "value1");
        assertEquals(attributes.get("Key1"), "value1");
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));

        // Test case for removing a non-existing attribute
        attributes.removeIgnoreCase("key2");
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        // Test case for an attribute that exists
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        // Test case for an attribute that doesn't exist
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();

        // Test case for an attribute with different cases that exists
        attributes.put("Key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        // Test case for an attribute that doesn't exist
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }
}