package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testInitialState() {
        assertTrue(attributes.size() == 0);
    }

    @Test
    public void testGetPut() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.putIgnoreCase("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertNull(attributes.get("Key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertNull(attributes.get("booleanKey"));
    }

    @Test
    public void testPutMultiple() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testGetMultiple() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertNull(attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.remove("key1");
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.removeIgnoreCase("key1");
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes.addAll(attributes2);
        assertEquals(4, attributes.size());
        assertEquals("value3", attributes.get("key3"));
        assertEquals("value4", attributes.get("key4"));
    }

    @Test
    public void testPutSameAttribute() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        Attributes cloned = attributes.clone();
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", cloned.get("key1"));
        attributes.remove("key1");
        assertNull(attributes.get("key1"));
        assertTrue(cloned.hasKey("key1"));
        assertEquals("value1", cloned.get("key1"));
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = (Attributes) attributes1.clone();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key1", "value1");
        assertTrue(attributes1.equals(attributes2));
        assertTrue(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes));
    }

    @Test
    public void testNormalize() {
        Attributes attributes1 = new Attributes();
        attributes1.put("Key1", "Value1");
        attributes1.normalize();
        assertEquals("value1", attributes1.get("key1"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("dataValue1", dataset.get("dataKey1"));
        assertNull(dataset.get("key1"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(" key1=\"value1\"", accum.toString());
    }
}