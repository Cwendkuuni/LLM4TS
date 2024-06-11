package org.jsoup.nodes; // update the package name if necessary

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributesTest {
    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(Attributes.EmptyString, attributes.get("nonexistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("Key"));
        assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("nonexistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));

        attributes.put("newKey", "newValue");
        assertEquals("newValue", attributes.get("newKey"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value1");
        attributes.put("Key", "Value2");
        assertEquals("Value2", attributes.get("Key"));

        attributes.put("NewKey", "NewValue");
        assertEquals("NewValue", attributes.get("NewKey"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertEquals(Attributes.EmptyString, attributes.get("booleanKey"));

        attributes.remove("booleanKey");
        assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutWithDataPrefix() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey", "dataValue");
        assertEquals("dataValue", attributes.get("dataKey"));
        assertEquals("dataValue", attributes.getIgnoreCase("DataKey"));
        assertEquals("dataValue", attributes.dataset().get("dataKey"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonexistentKey"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistentKey"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
        attributes.put("key3", "value3");
        assertEquals(3, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("originalKey1", "originalValue1");
        originalAttributes.put("originalKey2", "originalValue2");

        Attributes newAttributes = new Attributes();
        newAttributes.put("newKey1", "newValue1");
        newAttributes.put("newKey2", "newValue2");

        Attributes mergedAttributes = new Attributes();
        mergedAttributes.addAll(originalAttributes);
        mergedAttributes.addAll(newAttributes);

        assertEquals(4, mergedAttributes.size());
        assertTrue(mergedAttributes.hasKey("originalKey1"));
        assertTrue(mergedAttributes.hasKey("originalKey2"));
        assertTrue(mergedAttributes.hasKey("newKey1"));
        assertTrue(mergedAttributes.hasKey("newKey2"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("dataKey1"));
        assertTrue(dataset.containsKey("dataKey2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder accumulator = new StringBuilder();
        try {
            attributes.html(accumulator, Document.OutputSettings.defaultOutputSettings());
            assertEquals("key1=\"value1\" key2=\"value2\"", accumulator.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("key1=\"value1\" key2=\"value2\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes identicalAttributes = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        identicalAttributes.put("key1", "value1");
        identicalAttributes.put("key2", "value2");
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        assertTrue(attributes1.equals(identicalAttributes));
        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testClone() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("originalKey1", "originalValue1");
        originalAttributes.put("originalKey2", "originalValue2");

        Attributes clonedAttributes = originalAttributes.clone();

        assertEquals(originalAttributes.size(), clonedAttributes.size());
        assertTrue(originalAttributes.hasKey("originalKey1"));
        assertTrue(originalAttributes.hasKey("originalKey2"));
        assertTrue(clonedAttributes.hasKey("originalKey1"));
        assertTrue(clonedAttributes.hasKey("originalKey2"));
        assertEquals(originalAttributes.get("originalKey1"), clonedAttributes.get("originalKey1"));
        assertEquals(originalAttributes.get("originalKey2"), clonedAttributes.get("originalKey2"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "Value1");
        attributes.put("KEY2", "Value2");

        attributes.normalize();

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    
}