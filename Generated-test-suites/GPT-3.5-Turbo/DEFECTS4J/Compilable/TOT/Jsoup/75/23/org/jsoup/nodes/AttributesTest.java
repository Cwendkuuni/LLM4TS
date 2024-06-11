package org.jsoup.nodes;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testPutAndGet() {
        // Test for adding and retrieving value by key
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));

        // Test for replacing an existing value by key
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        // Test for adding a new attribute
        attributes.put("key2", "value3");
        assertTrue(attributes.hasKey("key2"));
        assertEquals("value3", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        // Test for adding a boolean attribute
        attributes.put("checked", true);
        assertTrue(attributes.hasKeyIgnoreCase("checked"));
        assertEquals("", attributes.getIgnoreCase("checked"));

        // Test for removing a boolean attribute
        attributes.put("checked", false);
        assertFalse(attributes.hasKeyIgnoreCase("checked"));
        assertEquals("", attributes.getIgnoreCase("checked"));
    }

    @Test
    public void testRemove() {
        // Test for removing an attribute by key
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));

        // Test for removing an attribute by case-insensitive key
        attributes.put("KEY3", "value3");
        attributes.put("KEY4", "value4");

        attributes.removeIgnoreCase("key3");
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
        assertNull(attributes.getIgnoreCase("key3"));
    }

    @Test
    public void testHasKey() {
        // Test for checking if attribute exists by key
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        // Test for checking if attribute exists by case-insensitive key
        attributes.put("KEY1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        // Test for getting the size of attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        assertEquals(3, attributes.size());

        attributes.remove("key2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutAttribute() {
        // Test for putting an Attribute object
        Attribute attr = new Attribute("key1", "value1");

        attributes.put(attr);

        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));
        assertEquals(attributes, attr.parent); // Check if parent reference is set correctly

        // Test for replacing an existing attribute with an Attribute object
        attr = new Attribute("key1", "value2");

        attributes.put(attr);

        assertTrue(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testNormalize() {
        // Test for normalizing attribute keys
        attributes.put("Key1", "value1");
        attributes.put("KEY2", "value2");

        attributes.normalize();

        assertFalse(attributes.hasKey("Key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));

        assertFalse(attributes.hasKey("KEY2"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testEquals() {
        // Test for checking equality of attributes
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertEquals(attributes1, attributes2);

        attributes2.put("key3", "value3");

        assertNotEquals(attributes1, attributes2);
    }

    @Test
    public void testClone() {
        // Test for cloning an Attributes object
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);

        attributes.put("key3", "value3");

        assertNotEquals(attributes, clone);
    }
}

