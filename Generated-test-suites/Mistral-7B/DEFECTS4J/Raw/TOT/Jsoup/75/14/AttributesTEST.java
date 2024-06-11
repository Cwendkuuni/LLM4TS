package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(emptyString, attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("key"));
        assertEquals("Value", attributes.getIgnoreCase("Key"));
        assertEquals(emptyString, attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.put("key1", "newValue1");
        assertEquals("newValue1", attributes.get("key1"));
    }

    @Test
    public void testPutIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "VALUE1");
        attributes.put("KEY2", "VALUE2");

        assertEquals("VALUE1", attributes.getIgnoreCase("key1"));
        assertEquals("VALUE1", attributes.getIgnoreCase("KEY1"));
        assertEquals("VALUE2", attributes.getIgnoreCase("key2"));
        assertEquals("VALUE2", attributes.getIgnoreCase("KEY2"));

        attributes.putIgnoreCase("KEY1", "newValue1");
        assertEquals("newValue1", attributes.getIgnoreCase("key1"));
        assertEquals("newValue1", attributes.getIgnoreCase("KEY1"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertFalse(attributes.get("key").equals("key"));
        assertNull(attributes.get("key"));

        attributes.put("key", false);
        assertFalse(attributes.get("key").equals("key"));
        assertNull(attributes.get("key"));

        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.get("key") == null);
    }

    @Test
    public void testPutAttributeWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        assertEquals("value2", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "VALUE1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "VALUE");
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
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
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        assertEquals(2, result.size());
        assertTrue(result.hasKey("key1"));
        assertTrue(result.hasKey("key2"));
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        Attribute attribute1 = list.get(0);
        Attribute attribute2 = list.get(1);
        assertEquals("key1", attribute1.getKey());
        assertEquals("value1", attribute1.getValue());
        assertEquals("key2", attribute2.getKey());
        assertEquals("value2", attribute2.getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");

        Map<String, String> result = attributes.dataset();
        assertEquals(2, result.size());
        assertTrue(result.containsKey("data-key1"));
        assertTrue(result.containsKey("data-key2"));
        assertEquals("data-value1", result.get("data-key1"));
        assertEquals("data-value2", result.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, null);

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        Attributes attributes4 = new Attributes();
        attributes4.put("key2", "value2");
        attributes4.put("key1", "value1");

        Attributes attributes5 = new Attributes();

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
        assertNotEquals(attributes1, attributes4);
        assertNotEquals(attributes1, attributes5);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
        assertEquals(attributes.get("key2"), clonedAttributes.get("key2"));
    }
}