package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();

        // Test setting and getting attributes
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key2"));
        assertEquals(2, attributes.size());

        // Test setting and getting case-insensitive attributes
        attributes.putIgnoreCase("key1", "value3");
        assertEquals("value3", attributes.getIgnoreCase("key1"));
        assertEquals("value3", attributes.get("key1"));

        // Test setting and getting boolean attributes
        attributes.put("boolKey", true);
        assertTrue(attributes.hasKey("boolKey"));
        assertFalse(attributes.get("boolKey").isEmpty());
        assertNull(attributes.get("boolKey").trim());
        assertEquals(1, attributes.size());

        // Test setting multiple attributes with same key
        attributes.put("key1", "value4");
        assertEquals("value4", attributes.get("key1"));

        // Test removing attributes
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
        assertEquals(0, attributes.size());

        // Test removing case-insensitive attributes
        attributes.putIgnoreCase("key1", "value5");
        assertEquals("value5", attributes.get("key1"));
        assertEquals("value5", attributes.getIgnoreCase("key1"));
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));

        // Test checking for existence of attributes
        attributes.put("key3", "value6");
        assertTrue(attributes.hasKey("key3"));
        assertTrue(attributes.hasKeyIgnoreCase("key3"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));

        // Test adding all attributes from another set
        Attributes attributes2 = new Attributes();
        attributes2.put("key4", "value7");
        attributes.addAll(attributes2);
        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("key4"));
        assertEquals("value7", attributes.get("key4"));

        // Test getting attributes as List
        List<Attribute> attributesList = attributes.asList();
        assertEquals(2, attributesList.size());
        assertEquals("key1", attributesList.get(0).getKey());
        assertEquals("value5", attributesList.get(0).getValue());
        assertEquals("key3", attributesList.get(1).getKey());
        assertEquals("value6", attributesList.get(1).getValue());

        // Test getting HTML representation of attributes
        assertEquals("<key1>value5</key1><key3>value6</key3>", attributes.html());

        // Test cloning attributes
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.toString(), clonedAttributes.toString());

        // Test normalizing attributes keys
        attributes.normalize();
        assertEquals(2, attributes.size());
        assertEquals("key1", attributes.get("key1"));
        assertEquals("key3", attributes.get("key3"));
    }
}