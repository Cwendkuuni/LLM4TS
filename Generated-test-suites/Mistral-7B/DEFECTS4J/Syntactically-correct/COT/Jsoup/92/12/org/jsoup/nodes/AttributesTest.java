package org.jsoup.nodes; // Update the package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertEquals(Attributes.EMPTY_STRING, attributes.get("emptyKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        assertEquals("Value", attributes.getIgnoreCase("Key"));
        assertEquals(Attributes.EMPTY_STRING, attributes.getIgnoreCase("EmptyKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        assertEquals("value2", attributes.get("key"));
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "value1");
        attributes.putIgnoreCase("key", "value2");

        assertEquals("value2", attributes.get("key"));
        assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        assertTrue(attributes.hasKey("key"));
        assertNull(attributes.get("key"));
        attributes.remove("key");
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);

        assertEquals("value", attributes.get("key"));
        assertFalse(attributes.hasKey("Key"));
        attributes.remove("key");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(null);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "value1");
        attributes.putIgnoreCase("key", "value2");

        attributes.remove("Key");
        assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("EmptyKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "Value");

        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        assertFalse(attributes.hasKeyIgnoreCase("emptyKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");

        attributes1.addAll(attributes2);

        assertEquals(2, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("dataValue1", dataset.get("dataKey1"));
        assertEquals("dataValue2", dataset.get("dataKey2"));
    }

    @Test
    public void testtoString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals("key=\"value\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals(attributes.get("key"), clonedAttributes.get("key"));
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneException() {
        Attributes attributes = new Attributes();
        throw new CloneNotSupportedException();
    }
}