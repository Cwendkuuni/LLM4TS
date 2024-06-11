package org.jsoup.nodes; // adjust the package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    // Add instance variable for the Attributes object under test
    private Attributes attributes;

    @Before
    public void setUp() {
        // Initialize the Attributes object under test
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
    }

    @Test
    public void testGet() {
        // Basic test case for get() method
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertNull(attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Basic test case for getIgnoreCase() method
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertNull(attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        // Basic test case for put() method
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
        assertSame(attributes, attributes.put("key1", "updatedValue1")); // chaining test
    }

    @Test
    public void testPutIgnoreCase() {
        // Basic test case for putIgnoreCase() method
        attributes.putIgnoreCase("Key3", "value3");
        assertEquals("value3", attributes.getIgnoreCase("Key3"));
        assertSame(attributes, attributes.putIgnoreCase("key1", "updatedValue1")); // chaining test
    }

    @Test
    public void testPutBoolean() {
        // Basic test case for put() method with boolean value
        attributes.put("booleanKey", true);
        assertFalse(attributes.get("booleanKey").equals("true")); // boolean values are stored as keys
        assertSame(attributes, attributes.put("booleanKey", false)); // changing a boolean value to false
        assertNull(attributes.get("booleanKey")); // removing a boolean value by setting it to null
    }

    @Test
    public void testRemove() {
        // Basic test case for remove() method
        attributes.put("key4", "value4");
        assertEquals("value4", attributes.get("key4"));
        attributes.remove("key4");
        assertNull(attributes.get("key4"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        // Basic test case for removeIgnoreCase() method
        attributes.putIgnoreCase("Key4", "value4");
        assertEquals("value4", attributes.getIgnoreCase("Key4"));
        attributes.removeIgnoreCase("key4");
        assertNull(attributes.getIgnoreCase("key4"));
    }

    @Test
    public void testHasKey() {
        // Basic test case for hasKey() method
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("nonExistentKey"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        // Basic test case for size() method
        assertEquals(2, attributes.size());
        attributes.put("key3", "value3");
        assertEquals(3, attributes.size());
        attributes.remove("key1");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        // Test case for addAll() method
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("otherKey1", "otherValue1");
        otherAttributes.put("otherKey2", "otherValue2");

        attributes.addAll(otherAttributes);
        assertEquals(5, attributes.size());
        assertTrue(attributes.hasKey("otherKey1"));
        assertTrue(attributes.hasKey("otherKey2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAttributesToAdd() {
        // Test case for IllegalArgumentException when adding null Attributes object
        attributes.addAll(null);
    }

    @Test
    public void testToString() {
        // Test case for toString() method
        assertEquals("Attributes{size=3}", attributes.toString());
    }

    @Test
    public void testEquals() {
        // Test case for equals() method
        Attributes equalAttributes = new Attributes();
        equalAttributes.put("key1", "value1");
        equalAttributes.put("key2", "value2");
        assertTrue(attributes.equals(equalAttributes));

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("otherKey1", "otherValue1");
        assertFalse(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        // Test case for hashCode() method
        Attributes equalAttributes = new Attributes();
        equalAttributes.put("key1", "value1");
        equalAttributes.put("key2", "value2");
        assertEquals(attributes.hashCode(), equalAttributes.hashCode());

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("otherKey1", "otherValue1");
        assertNotEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        // Test case for clone() method
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
        assertNotSame(attributes, clonedAttributes);
    }

    @Test
    public void testNormalize() {
        // Test case for normalize() method
        Attributes attributesWithMixedKeys = new Attributes();
        attributesWithMixedKeys.put("key1", "value1");
        attributesWithMixedKeys.put("Key2", "value2");
        attributesWithMixedKeys.normalize();
        assertEquals("key1", attributesWithMixedKeys.keys[0]);
        assertEquals("key2", attributesWithMixedKeys.keys[1]);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithSerializationException() {
        // Test case for SerializationException when generating HTML representation
        Attributes attributes = new Attributes();
        try {
            attributes.html();
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }
}