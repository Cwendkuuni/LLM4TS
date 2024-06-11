package org.jsoup.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class for Attributes.
 */
public class AttributesTest {

    private Attributes attributes;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    /**
     * Tear down test environment.
     */
    @After
    public void tearDown() {
        attributes = null;
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));
    }

    /**
     * Test getIgnoreCase method.
     */
    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("key2"));
    }

    /**
     * Test put method.
     */
    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));

        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key2"));
    }

    /**
     * Test put with boolean value.
     */
    @Test
    public void testPutBoolean() {
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));

        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    /**
     * Test remove method.
     */
    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertEquals("", attributes.get("key1"));

        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key2"));
    }

    /**
     * Test removeIgnoreCase method.
     */
    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key1", "Value1");
        attributes.removeIgnoreCase("Key1");
        assertEquals("", attributes.getIgnoreCase("Key1"));

        attributes.put("Key2", "Value2");
        assertEquals("Value2", attributes.getIgnoreCase("Key2"));
    }

    /**
     * Test hasKey method.
     */
    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    /**
     * Test hasKeyIgnoreCase method.
     */
    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key1", "Value1");
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("Key2"));
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    /**
     * Test addAll method.
     */
    @Test
    public void testAddAll() {
        Attributes anotherAttributes = new Attributes();
        anotherAttributes.put("anotherKey1", "anotherValue1");
        anotherAttributes.put("anotherKey2", "anotherValue2");

        attributes.addAll(anotherAttributes);
        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("anotherKey1"));
        assertTrue(attributes.hasKey("anotherKey2"));
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(clonedAttributes.hasKey("key1"));
        assertTrue(clonedAttributes.hasKey("key2"));

        Attribute attribute1 = attributes.get("key1");
        Attribute attribute2 = clonedAttributes.get("key1");
        assertNotSame(attribute1, attribute2);
        assertEquals(attribute1.getKey(), attribute2.getKey());
        assertEquals(attribute1.getValue(), attribute2.getValue());
    }
}