package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        Assert.assertArrayEquals(Attributes.Empty, attributes.keys);
        Assert.assertArrayEquals(Attributes.Empty, attributes.vals);
    }

    @Test
    public void testBasicAttributeOperations() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.get("otherKey"));
        assertEquals("", attributes.getIgnoreCase("otherKey"));

        attributes.put("otherKey", "otherValue");
        assertEquals(2, attributes.size());
        assertEquals("value", attributes.get("key"));
        assertEquals("otherValue", attributes.get("otherKey"));

        attributes.putIgnoreCase("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
        assertEquals("newValue", attributes.getIgnoreCase("key"));

        attributes.put("key", null);
        assertEquals(2, attributes.size());
        assertEquals(null, attributes.get("key"));
        assertEquals("otherValue", attributes.get("otherKey"));

        attributes.remove("key");
        assertEquals(1, attributes.size());
        assertEquals("otherValue", attributes.get("otherKey"));

        attributes.removeIgnoreCase("Key");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testBooleanAttributeOperations() {
        Attributes attributes = new Attributes();
        attributes.put("boolKey", true);
        assertEquals(1, attributes.size());
        assertTrue(attributes.get("boolKey") == null);
        assertTrue(attributes.hasKey("boolKey"));

        attributes.put("boolKey", false);
        assertEquals(1, attributes.size());
        assertTrue(attributes.get("boolKey") == null);
        assertFalse(attributes.hasKey("boolKey"));

        attributes.put("boolKey", true);
        assertEquals(1, attributes.size());
        assertTrue(attributes.get("boolKey") == null);
        assertTrue(attributes.hasKey("boolKey"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes result = new Attributes();
        result.putAll(attributes1);
        result.putAll(attributes2);

        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));

        List<Attribute> list = result.asList();
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testPutIgnoreCaseAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("KEY2", "value2");

        Attributes result = new Attributes();
        result.putAll(attributes1);
        result.putAll(attributes2);

        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("KEY2"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(1, attributes.size());

        attributes.remove("key");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(1, attributes.size());

        attributes.removeIgnoreCase("key");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(1, attributes.size());

        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKey("otherKey"));
        assertFalse(attributes.hasKeyIgnoreCase("OtherKey"));
    }

    @Test
    public void testHasKeyWithBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("boolKey", true);
        assertEquals(1, attributes.size());

        assertTrue(attributes.hasKey("boolKey"));
        assertTrue(attributes.hasKeyIgnoreCase("boolKey"));
        assertFalse(attributes.hasKey("otherKey"));
        assertFalse(attributes.hasKeyIgnoreCase("OtherKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(2, clonedAttributes.size());
        assertEquals("value1", clonedAttributes.get("key1"));
        assertEquals("value2", clonedAttributes.get("key2"));

        attributes.put("key3", "value3");
        clonedAttributes.put("key3", "value4");

        assertNotEquals(attributes, clonedAttributes);
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value4", clonedAttributes.get("key3"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");

        attributes.normalize();

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }
}