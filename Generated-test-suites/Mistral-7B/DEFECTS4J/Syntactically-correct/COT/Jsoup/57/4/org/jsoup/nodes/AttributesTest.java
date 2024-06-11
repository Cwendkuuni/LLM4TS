package org.jsoup.nodes; // Update the package name if necessary

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class for Attributes.
 */
public class AttributesTest {

    private Attributes attributes;
    private Attributes otherAttributes;

    /**
     * Initialize test objects.
     */
    @Before
    public void setup() {
        attributes = new Attributes();
        otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");
    }

    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        String value = attributes.get("key1");
        assertEquals("value1", value);
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        String value = attributes.getIgnoreCase("Key1");
        assertEquals("value1", value);
    }

    @Test
    public void testGetEmpty() {
        String value = attributes.get("nonExistentKey");
        assertEquals("", value);
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        String value = attributes.get("key1");
        assertEquals("value1", value);
    }

    @Test
    public void testPutBooleanTrue() {
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put("key1", true);
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        String value = attributes.get("key1");
        assertEquals("value1", value);
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key1", "value1");
        attributes.removeIgnoreCase("Key1");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("Key1"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        attributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");
        attributes.addAll(otherAttributes);
        assertEquals(3, attributes.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        Attribute attribute = list.get(0);
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, new Document.OutputSettings());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        String html = accum.toString();
        String expected = " key1=\"value1\"";
        assertEquals(expected, html);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(""));
        assertFalse(attributes1.equals(null));
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = attributes1.clone();
        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testEmptyDataset() {
        Map<String, String> dataset = new Attributes().dataset();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testPutNullKey() {
        try {
            attributes.put(null, "value");
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPutNullValue() {
        attributes.put("key", null);
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutEmptyMap() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes2.putAll(attributes1);
        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testPutDuplicateKey() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testRemoveKeyThatDoesntExist() {
        attributes.remove("nonExistentKey");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKeyNull() {
        assertFalse(attributes.hasKey(null));
    }
}