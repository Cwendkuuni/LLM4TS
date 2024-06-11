package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSizeAndCapacity() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        assertEquals(3, attributes.size());

        attributes.addAll(new Attributes().put("key4", "value4").put("key5", "value5"));

        assertEquals(5, attributes.size());
    }

    @Test
    public void testGetAndPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.put("key1", "value1_updated");

        assertEquals("value1_updated", attributes.get("key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.putIgnoreCase("key1", "Value2");

        assertEquals("Value2", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        assertFalse(attributes.get("key1").isEmpty());
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");

        attributes.put(attribute);

        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes().put("key1", "value1").put("key2", "value2");

        attributes.remove("key1");

        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes().put("key1", "value1").put("Key2", "value2");

        attributes.removeIgnoreCase("key1");

        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes().put("key1", "value1").put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKey("key3"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes().put("key1", "value1").put("key2", "value2");

        List<Map.Entry<String, String>> attributesList = attributes.asList();

        for (Map.Entry<String, String> entry : attributesList) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes().put("data-key1", "value1").put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes().put("key1", "value1").put("key2", "value2");

        assertEquals("key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attributes2 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attributes3 = new Attributes().put("key1", "value1").put("key3", "value3");

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attributes2 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attributes3 = new Attributes().put("key1", "value1").put("key3", "value3");

        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }
}