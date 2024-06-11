package org.test; // replace with the actual package name

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    // Test for basic functionality of get() method
    @Test
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(Attributes.EmptyString, attributes.get("nonexistentKey"));
    }

    // Test for basic functionality of getIgnoreCase() method
    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("NONEEXISTENTKEY"));
    }

    // Test for basic functionality of put() method
    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.put("key1", "newValue1");
        assertEquals("newValue1", attributes.get("key1"));

        attributes.put("key2", null); // remove "key2" by setting its value to null
        assertNull(attributes.get("key2"));
    }

    // Test for basic functionality of putIgnoreCase() method
    @Test
    public void testPutIgnoreCase() {
        attributes.putIgnoreCase("KEY1", "value1");
        attributes.putIgnoreCase("KEY2", "value2");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));

        attributes.putIgnoreCase("KEY1", "newValue1");
        assertEquals("newValue1", attributes.getIgnoreCase("key1"));

        attributes.putIgnoreCase("KEY2", null); // remove "KEY2" by setting its value to null
        assertNull(attributes.getIgnoreCase("key2"));
    }

    // Test for basic functionality of put() and putIgnoreCase() methods with the same key
    @Test
    public void testPutAndPutIgnoreCaseWithSameKey() {
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        assertEquals("value2", attributes.get("key"));
        assertEquals("value2", attributes.getIgnoreCase("key"));
    }

    // Test for basic functionality of size() method
    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    // Test for basic functionality of addAll() method
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes mergedAttributes = new Attributes();
        mergedAttributes.addAll(attributes1);
        mergedAttributes.addAll(attributes2);

        assertEquals(2, mergedAttributes.size());
        assertEquals("value1", mergedAttributes.get("key1"));
        assertEquals("value2", mergedAttributes.get("key2"));
    }

    // Test for basic functionality of asList() method
    @Test
    public void testAsList() {
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

    // Test for basic functionality of dataset() method
    @Test
    public void testDataset() {
        attributes.put("key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals("value2", dataset.get("data-key2"));
    }

    // Test for basic functionality of html() method
    @Test
    public void testHtml() throws Exception {
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append("<html>");
        expectedHtml.append("<attribute key1=\"value1\" />");
        expectedHtml.append("</html>");

        attributes.put("key1", "value1");

        String actualHtml = attributes.html();
        assertEquals(expectedHtml.toString(), actualHtml);
    }

    // Test for edge cases and exception handling

    // Test for null key in put() and putIgnoreCase() methods
    @Test(expected = NullPointerException.class)
    public void testPutWithNullKey() {
        attributes.put(null, "value");
    }

    // Test for null value in put() method
    @Test
    public void testPutWithNullValue() {
        attributes.put("key", null);
        assertEquals(Attributes.EmptyString, attributes.get("key"));
    }

    // Test for empty string as key in put() method
    @Test
    public void testPutWithEmptyStringAsKey() {
        attributes.put("", "value");
        assertEquals(Attributes.EmptyString, attributes.get(""));
    }

    // Test for empty string as value in put() method
    @Test
    public void testPutWithEmptyStringAsValue() {
        attributes.put("key", Attributes.EmptyString);
        assertEquals(Attributes.EmptyString, attributes.get("key"));
    }

    // Test for null value in putIgnoreCase() method
    @Test
    public void testPutIgnoreCaseWithNullValue() {
        attributes.putIgnoreCase("key", null);
        assertNull(attributes.get("key"));
    }

    // Test for empty string as value in putIgnoreCase() method
    @Test
    public void testPutIgnoreCaseWithEmptyStringAsValue() {
        attributes.putIgnoreCase("key", Attributes.EmptyString);
        assertEquals(Attributes.EmptyString, attributes.get("key"));
    }

    // Test for negative index in remove() method
    @Test
    public void testRemoveWithNegativeIndex() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        try {
            attributes.remove(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    // Test for out-of-bound index in remove() method
    @Test
    public void testRemoveWithOutOfBoundIndex() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        try {
            attributes.remove(2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    // Test for removing the same key multiple times
    @Test
    public void testRemoveTheSameKeyMultipleTimes() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        attributes.put("key1", "value3");

        assertEquals("value3", attributes.get("key1"));

        attributes.remove("key1");
        assertNull(attributes.get("key1"));
    }

    // Test for removing the same key multiple times with ignoreCase option
    @Test
    public void testRemoveTheSameKeyMultipleTimesIgnoreCase() {
        attributes.put("KEY1", "value1");
        attributes.put("KEY1", "value2");
        attributes.put("KEY1", "value3");

        assertEquals("value3", attributes.getIgnoreCase("Key1"));

        attributes.removeIgnoreCase("Key1");
        assertNull(attributes.getIgnoreCase("Key1"));
    }

    // Test for hashCode() and equals() methods
    @Test
    public void testHashCodeAndEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value3");
        attributes3.put("key2", "value2");

        Attributes attributes4 = new Attributes();

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2); // should be equal since they have the same attributes
        assertNotEquals(attributes1, attributes3);
        assertNotEquals(attributes1, attributes4);

        List<Attribute> attributesList1 = attributes1.asList();
        List<Attribute> attributesList2 = attributes2.asList();

        assertEquals(attributes1, attributesList1);
        assertEquals(attributes1, attributesList2);
    }
}