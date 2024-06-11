package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        attributes.addAll(otherAttributes);
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        attributes.put("key1", "value1");
        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int expectedHashCode = 31 * 2 + Arrays.hashCode(new String[] {"key1", "key2"}) + Arrays.hashCode(new String[] {"value1", "value2"});
        assertEquals(expectedHashCode, attributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        assertNotSame(attributes, clone);
        assertEquals(attributes.size(), clone.size());
        assertEquals(attributes.get("key1"), clone.get("key1"));
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}