package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSizeAndCapacity() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
        attributes.put("key4", "value4");
        attributes.put("key5", "value5");
        attributes.put("key6", "value6");

        assertEquals(6, attributes.size());
        attributes.addAll(new Attributes());
        attributes.put("key7", "value7");
        assertEquals(7, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.put("key1", "value1_updated");
        assertEquals("value1_updated", attributes.get("key1"));
    }

    @Test
    public void testPutGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        assertEquals("Value1", attributes.getIgnoreCase("Key1"));
        attributes.put("Key1", "Value1_updated");
        assertEquals("Value1_updated", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPutPutSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value1_updated");
        assertEquals("value1_updated", attributes.get("key1"));
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", null);
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testPutEmptyStringValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "");
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testRemoveKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCaseKey() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("Key1"));
        assertEquals("Value2", attributes.get("Key2"));
    }

    @Test
    public void testRemoveKeyNotPresent() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testHasKeyEmpty() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey(""));
        assertFalse(attributes.hasKeyIgnoreCase(""));
    }

    @Test
    public void testPutBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutAttributeSameKey() {
        Attributes attributes = new Attributes();
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value2");
        attributes.put(attribute1);
        attributes.put(attribute2);
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutAttributeNullValue() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", null);
        attributes.put(attribute);
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testPutAttributeEmptyStringValue() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "");
        attributes.put(attribute);
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        attributes.remove(attribute.getKey());
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveAttributeNull() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.remove(null) != false);
    }

    @Test
    public void testAddAllEmpty() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.addAll(attributes2);
        assertEquals(0, attributes1.size());
    }

    @Test
    public void testAddAllNonEmpty() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
        List<Map.Entry<String, String>> entries = new ArrayList<>();
        while (iterator.hasNext()) {
            entries.add(iterator.next());
        }
        assertEquals(2, entries.size());
        assertEquals("key1", entries.get(0).getKey());
        assertEquals("value1", entries.get(0).getValue());
        assertEquals("key2", entries.get(1).getKey());
        assertEquals("value2", entries.get(1).getValue());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("key1", list.get(0).getKey());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("key2", list.get(1).getKey());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("key1", dataset.keySet().iterator().next());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("key2", dataset.keySet().iterator().next());
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");
        assertEquals(expectedHtml.toString(), attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key1", "value2");
        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key1", "value2");
        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
    }
}