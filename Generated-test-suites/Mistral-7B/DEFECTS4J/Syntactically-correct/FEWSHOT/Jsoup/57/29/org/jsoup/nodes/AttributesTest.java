package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

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
        assertEquals("TestValue", attributes.getIgnoreCase("testKey"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", true);
        assertTrue(attributes.hasKey("testKey"));
        assertFalse(attributes.hasKey("testKey"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("testKey", "testValue");
        attributes.put(attribute);
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        attributes.remove("testKey");
        assertFalse(attributes.hasKey("testKey"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("TestKey", "TestValue");
        attributes.removeIgnoreCase("testKey");
        assertFalse(attributes.hasKey("testKey"));
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
        assertTrue(attributes.hasKeyIgnoreCase("testKey"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        Attributes attributes2 = new Attributes();
        attributes2.put("testKey2", "testValue2");
        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        assertTrue(attributes1.hasKey("testKey1"));
        assertTrue(attributes1.hasKey("testKey2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        List<Attribute> attributesList = attributes.asList();
        for (Attribute attribute : attributesList) {
            assertEquals(attribute.getKey(), "testKey1"
                    || "testKey2");
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        List<Attribute> attributesList = attributes.asList();
        assertEquals(2, attributesList.size());
        assertEquals(attributesList.get(0).getKey(), "testKey1");
        assertEquals(attributesList.get(1).getKey(), "testKey2");
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-testKey1", "data-testValue1");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals(dataset.get("data-testKey1"), "data-testValue1");
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        StringBuilder accum = new StringBuilder();
        attributes.html(accum, null);
        assertEquals("<testKey>testValue</testKey>", accum.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        Attributes attributes2 = new Attributes();
        attributes2.put("testKey1", "testValue1");
        Attributes attributes3 = new Attributes();
        attributes3.put("testKey2", "testValue2");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        Attributes attributes2 = new Attributes();
        attributes2.put("testKey1", "testValue1");
        Attributes attributes3 = new Attributes();
        attributes3.put("testKey2", "testValue2");
        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
    }
}