package org.test; // replace this with the correct test package name

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AttributesTest {

    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test(expected = AssertionError.class)
    public void testGetNegativeIndex() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(EmptyString, attributes.get(-1));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testGetIgnoreCaseNotFound() {
        Attributes attributes = new Attributes();
        assertEquals(EmptyString, attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertEquals(EmptyString, attributes.get("key"));
    }

    @Test
    public void testPutEmptyStringValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        assertEquals(EmptyString, attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertFalse(attributes.get("key").equals(EmptyString));
        assertNull(attributes.get("key")); // Boolean attributes are represented as keys
    }

    @Test
    public void testPutDuplicate() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutIgnoreCaseDuplicate() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals(EmptyString, attributes.get("key"));
    }

    @Test(expected = AssertionError.class)
    public void testRemoveNegativeIndex() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove(-1);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        assertEquals(EmptyString, attributes.get("Key"));
    }

    @Test(expected = AssertionError.class)
    public void testRemoveIgnoreCaseNotFound() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase("Key");
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKeyIgnoreCaseNotFound() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
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

        Dataset dataset = attributes.dataset();

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

        StringBuilder expected = StringUtil.borrowBuilder();
        attributes.html(expected, new Document.OutputSettings());
        StringBuilder actual = new StringBuilder();
        attributes.html(actual, new Document.OutputSettings());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(attributes.toString(), attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");

        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testEqualsDifferentSize() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testEqualsDifferentValues() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value2");

        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");

        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testHashCodeDifferentSize() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testHashCodeDifferentValues() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value2");

        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes cloned = attributes.clone();

        attributes.put("key2", "value2");
        assertNotEquals(attributes, cloned);
        assertEquals(attributes.size(), cloned.size());
        assertEquals(attributes.get("key1"), cloned.get("key1"));
        assertNull(cloned.get("key2"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value");
        attributes.normalize();
        assertEquals("key1", attributes.get("KEY1"));
    }

    @Test
    public void testRemoveDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Dataset dataset = attributes.dataset();
        assertEquals(2, dataset.size());

        attributes.remove("data-key1");
        dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertNull(attributes.get("data-key1"));
        assertEquals("value2", attributes.get("data-key2"));
    }

    @Test
    public void testPutIgnoreCaseDuplicateDataAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");

        AssertionError exception = assertThrows(IllegalArgumentException.class, () -> {
            attributes.putIgnoreCase("Data-key1", "value2");
        });
        assertEquals("Duplicate attribute key 'data-key1'", exception.getMessage());
    }

    @Test
    public void testPutDuplicateDataAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");

        AssertionError exception = assertThrows(IllegalArgumentException.class, () -> {
            attributes.put("data-key1", "value2");
        });
        assertEquals("Duplicate attribute key 'data-key1'", exception.getMessage());
    }

    @Test
    public void testPutDataAttributeWithEmptyStringValue() {
        Attributes attributes = new Attributes();

        attributes.put("data-key", "");
        assertEquals("", attributes.get("data-key"));
    }

    @Test
    public void testPutDataAttributeWithNullValue() {
        Attributes attributes = new Attributes();

        attributes.put("data-key", null);
        assertNull(attributes.get("data-key"));
    }

    @Test
    public void testPutDataAttributeWithEmptyStringKey() {
        Attributes attributes = new Attributes();

        attributes.put("", "value");
        assertEquals(EmptyString, attributes.get(""));
    }

    @Test
    public void testPutDataAttributeWithNullKey() {
        Attributes attributes = new Attributes();

        attributes.put(null, "value");
        assertNull(attributes.get(null));
    }

    
}