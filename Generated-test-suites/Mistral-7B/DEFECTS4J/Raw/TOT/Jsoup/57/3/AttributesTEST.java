package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        assertEquals("testValue", attributes.get("testKey"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("TestKey", "TestValue");
        assertEquals("TestValue", attributes.getIgnoreCase("testkey"));
        assertEquals("", attributes.getIgnoreCase("NonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        attributes.put("testKey", "newTestValue");
        assertEquals("newTestValue", attributes.get("testKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "testValue");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", null);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", false);
        assertFalse(attributes.hasKey("testKey"));
        attributes.put("testKey", true);
        assertTrue(attributes.hasKey("testKey"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("testKey", "testValue");
        attributes.put(attribute);
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testPutAttributes() {
        Attributes baseAttributes = new Attributes();
        Attributes additionalAttributes = new Attributes();
        additionalAttributes.put("additionalKey", "additionalValue");

        baseAttributes.addAll(additionalAttributes);
        assertEquals("additionalValue", baseAttributes.get("additionalKey"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        attributes.remove("testKey");
        assertEquals("", attributes.get("testKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        assertTrue(attributes.hasKey("testKey"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("TestKey", "TestValue");
        assertTrue(attributes.hasKeyIgnoreCase("testkey"));
        assertFalse(attributes.hasKeyIgnoreCase("NonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testIterable() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        List<Attribute> attributesList = Arrays.asList(attributes);
        for (Attribute attribute : attributesList) {
            String key = attribute.getKey();
            String value = attribute.getValue();
            assertEquals(key, attributes.get(key));
            assertEquals(value, attribute.getValue());
        }
    }

    @Test
    public void testClone() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("originalKey", "originalValue");

        Attributes clonedAttributes = originalAttributes.clone();
        assertEquals("originalValue", originalAttributes.get("originalKey"));
        assertEquals("originalValue", clonedAttributes.get("originalKey"));

        originalAttributes.put("newKey", "newValue");
        assertNotSame(originalAttributes, clonedAttributes);
        assertEquals("originalValue", clonedAttributes.get("originalKey"));
        assertEquals("", clonedAttributes.get("newKey"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("dataValue1", dataset.get("dataKey1"));
        assertFalse(dataset.containsKey("nonExistentKey"));
    }
}