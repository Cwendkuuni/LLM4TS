package org.junit.tests;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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
        assertEquals("Value", attributes.getIgnoreCase("key"));
        assertEquals(Attributes.EMPTY_STRING, attributes.getIgnoreCase("EmptyKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.get("key"));
        attributes.putIgnoreCase("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertNull(attributes.get("key"));
    }

    @Test
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertFalse(attributes.hasKey("key"));
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
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

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllNullAttributes() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        int counter = 0;
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            assertEquals(counter++, attribute.getKey().length());
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertTrue(list.get(0).getKey().equals("key1"));
        assertTrue(list.get(1).getKey().equals("key2"));
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
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" data-key1=\"value1\" data-key2=\"value2\"");
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(expectedHtml.toString(), attributes.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlSerializationException() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        throw new RuntimeException("IOException when calling html()"); // replace with actual IOException
    }
}