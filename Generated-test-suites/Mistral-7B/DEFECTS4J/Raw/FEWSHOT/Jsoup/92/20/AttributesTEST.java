package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key", "value");
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String value = attributes.get("key");
        assertEquals("value", value);

        String ignoredCaseValue = attributes.getIgnoreCase("key");
        assertEquals("value", ignoredCaseValue);

        attributes.put("key2", "value2");
        value = attributes.get("key2");
        assertEquals("value2", value);

        ignoredCaseValue = attributes.getIgnoreCase("key2");
        assertEquals("value2", ignoredCaseValue);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "Value");

        String value = attributes.get("Key");
        assertEquals("Value", value);

        String ignoredCaseValue = attributes.getIgnoreCase("Key");
        assertEquals("Value", ignoredCaseValue);

        attributes.putIgnoreCase("KEY", "value2");
        value = attributes.get("KEY");
        assertEquals("value2", value);

        ignoredCaseValue = attributes.getIgnoreCase("key");
        assertEquals("value2", ignoredCaseValue);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        boolean value = attributes.getBoolean("key");
        assertTrue(value);

        attributes.put("key", false);
        value = attributes.getBoolean("key");
        assertFalse(value);

        attributes.put("key", true);
        value = attributes.getBoolean("Key");
        assertTrue(value);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        attributes.removeIgnoreCase("Key1");
        assertFalse(attributes.hasKey("key1"));

        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));

        attributes.removeIgnoreCase("Key2");
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));

        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKeyIgnoreCase("Key2"));

        assertFalse(attributes.hasKey("key3"));
        assertFalse(attributes.hasKeyIgnoreCase("Key3"));
    }

    @Test
    public void testIterable() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();

        assertEquals(2, attributesList.size());

        Attribute attribute1 = attributesList.get(0);
        assertEquals("key1", attribute1.getKey());
        assertEquals("value1", attribute1.getValue());

        Attribute attribute2 = attributesList.get(1);
        assertEquals("key2", attribute2.getKey());
        assertEquals("value2", attribute2.getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());

        String value1 = dataset.get("data-key1");
        assertEquals("value1", value1);

        String value2 = dataset.get("data-key2");
        assertEquals("value2", value2);

        assertFalse(dataset.containsKey("key1"));
        assertFalse(dataset.containsKey("key2"));
    }
}