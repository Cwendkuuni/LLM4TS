package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        assertEquals(3, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));

        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value3");
        assertEquals("value3", attributes.get("key2"));

        assertEquals(EmptyString, attributes.get("nonexistentkey"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.putIgnoreCase("Key1", "Value1");
        assertEquals("Value1", attributes.get("Key1"));

        attributes.putIgnoreCase("Key1", "Value2");
        assertEquals("Value2", attributes.get("Key1"));

        attributes.putIgnoreCase("KEY2", "Value3");
        assertEquals("Value3", attributes.get("KEY2"));

        assertEquals(EmptyString, attributes.get("nonexistentkey"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();

        attributes.put("booleanKey", true);
        assertTrue(attributes.get("booleanKey") == null);

        attributes.putIgnoreCase("BooleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
        assertFalse(attributes.hasKeyIgnoreCase("BooleanKey"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("Key1"));

        attributes.put("key2", "value2");
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKeyIgnoreCase("Key2"));

        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
        assertTrue(attributes.hasKeyIgnoreCase("Key3"));

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("Key1"));

        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("Key2"));

        attributes.remove("key3");
        assertFalse(attributes.hasKey("key3"));
        assertFalse(attributes.hasKeyIgnoreCase("Key3"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertTrue(attributes.size() > 0);

        attributes.remove("key1");
        assertEquals(EmptyString, attributes.get("key1"));
        assertEquals(0, attributes.size());

        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
        assertTrue(attributes.size() > 0);

        attributes.remove("key2");
        assertEquals(EmptyString, attributes.get("key2"));
        assertEquals(1, attributes.size());

        attributes.remove("key3");
        assertEquals(EmptyString, attributes.get("key3"));
        assertEquals(0, attributes.size());

        attributes.remove(null);
        assertEquals(EmptyString, attributes.get(null));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        Attributes attributes3 = new Attributes();
        attributes3.putAll(attributes1);
        attributes3.putAll(attributes2);

        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));
        assertEquals("value3", attributes3.get("key3"));
        assertEquals("value4", attributes3.get("key4"));
        assertEquals(4, attributes3.size());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        List<Attribute> attributesList = attributes.asList();

        for (Attribute attribute : attributesList) {
            System.out.println("Key: " + attribute.getKey() + ", Value: " + attribute.getValue());
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals("dataValue1", dataset.get("dataKey1"));
        assertEquals("dataValue2", dataset.get("dataKey2"));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        StringBuilder accum = new StringBuilder();
        attributes.html(accum, new Document.OutputSettings());

        assertEquals("<key1>value1</key1>", accum.toString());
    }
}