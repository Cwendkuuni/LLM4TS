package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Attributes.Attribute;
import org.jsoup.nodes.Attributes.BooleanAttribute;
import org.jsoup.nodes.Attributes.Dataset;

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
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("Key"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.get("key"));

        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));

        attributes.put("key2", "value2");
        assertEquals("newValue", attributes.get("key"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        assertTrue(attributes.get("key").isBoolean());
        assertFalse(attributes.get("key").getValue().isEmpty());

        attributes.put("key", false);
        assertFalse(attributes.get("key").isBoolean());
        assertNull(attributes.get("key").getValue());

        attributes.put("key", true);
        assertTrue(attributes.get("key").isBoolean());
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));

        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.get("key"));

        attributes.put(new Attribute("key", "newValue"));
        assertEquals("newValue", attributes.get("key"));
        assertEquals("newValue", attributes.get("key"));

        attributes.put(new Attribute("key2", "value2"));
        assertEquals("newValue", attributes.get("key"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.get("key"));

        attributes.remove("key");
        assertNull(attributes.get("key"));
        assertNull(attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.get("key"));

        attributes.removeIgnoreCase("Key");
        assertNull(attributes.get("key"));
        assertNull(attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonExistentKey"));

        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));

        attributes.remove("Key");
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String html = attributes.html();
        assertEquals("<key>value</key>", html);
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

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes4));
    }

    @Test
    public void testHashCode() {
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

        int hashCode1 = attributes1.hashCode();
        int hashCode2 = attributes2.hashCode();
        int hashCode3 = attributes3.hashCode();
        int hashCode4 = attributes4.hashCode();

        assertEquals(hashCode1, attributes1.hashCode());
        assertEquals(hashCode1, attributes2.hashCode());
        assertNotEquals(hashCode1, attributes3.hashCode());
        assertNotEquals(hashCode1, attributes4.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Attributes clonedAttributes = attributes.clone();

        assertEquals("value", clonedAttributes.get("key"));
        assertEquals("value", clonedAttributes.get("key"));

        attributes.put("newKey", "newValue");

        assertNotEquals("newValue", clonedAttributes.get("newKey"));
        assertNull(clonedAttributes.get("newKey"));
    }

    @Test
    public void testCloneWithDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("data-key1", "value1"));
        attributes.put(new Attribute("data-key2", "value2"));

        Attributes clonedAttributes = attributes.clone();

        Map<String, String> clonedDataset = clonedAttributes.dataset();
        Map<String, String> dataset = attributes.dataset();

        assertEquals(dataset, clonedDataset);
    }

    @Test
    public void testCloneWithBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        Attributes clonedAttributes = attributes.clone();

        assertTrue(clonedAttributes.get("key").isBoolean());
        assertFalse(clonedAttributes.get("key").getValue().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }

    @Test(expected = IllegalStateException.class)
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "newValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateIgnoreCaseKey() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.put("key", "newValue");
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            list.add(entry);
        }

        assertEquals(2, list.size());
        assertEquals("key1", list.get(0).getKey());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("key2", list.get(1).getKey());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> asList = attributes.asList();

        assertEquals(2, asList.size());
        assertEquals("key1", asList.get(0).getKey());
        assertEquals("value1", asList.get(0).getValue());
        assertEquals("key2", asList.get(1).getKey());
        assertEquals("value2", asList.get(1).getValue());

        for (Attribute attribute : asList) {
            assertEquals(attribute.getClass(), Attribute.class);
        }
    }

    @Test
    public void testDatasetIterator() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("data-key1", "value1"));
        attributes.put(new Attribute("data-key2", "value2"));

        Iterator<Map.Entry<String, String>> datasetIterator = attributes.dataset().entrySet().iterator();

        assertEquals("data-key1", datasetIterator.next().getKey());
        assertEquals("value1", datasetIterator.next().getValue());
        assertFalse(datasetIterator.hasNext());
    }
}