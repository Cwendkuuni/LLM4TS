package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttributesTest {

    // Test for get() method

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String value1 = attributes.get("key1");
        String value2 = attributes.get("key2");

        assertEquals("value1", value1);
        assertEquals("value2", value2);

        String emptyValue = attributes.get("nonExistentKey");
        assertEquals("", emptyValue);
    }

    // Test for getIgnoreCase() method

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        String value1 = attributes.getIgnoreCase("key1");
        String value2 = attributes.getIgnoreCase("Key2");

        assertEquals("value1", value1);
        assertEquals("value2", value2);

        String emptyValue = attributes.getIgnoreCase("nonExistentKey");
        assertEquals("", emptyValue);
    }

    // Test for put() method

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "newValue1");

        String newValue1 = attributes.get("key1");
        assertEquals("newValue1", newValue1);

        String value2 = attributes.get("key2");
        assertEquals("value2", value2);

        // Test for put() with the same key but different value types
        attributes.put("key1", 1);
        attributes.put("key1", "newValue1");

        String newValue1String = attributes.get("key1");
        assertEquals("newValue1", newValue1String);

        Integer value1 = (Integer) attributes.get("key1");
        assertEquals(1, value1.intValue());
    }

    // Test for putIgnoreCase() method

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("Key1", "newValue1");

        String newValue1 = attributes.get("Key1");
        assertEquals("newValue1", newValue1);

        String value2 = attributes.get("key2");
        assertEquals("value2", value2);

        // Test for putIgnoreCase() with the same key but different value types
        attributes.put("Key1", 1);
        attributes.put("Key1", "newValue1");

        String newValue1String = attributes.get("Key1");
        assertEquals("newValue1", newValue1String);

        Integer value1 = (Integer) attributes.get("Key1");
        assertEquals(1, value1.intValue());
    }

    // Test for put() method with boolean value

    @Test
    public void testPutBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        attributes.put("key2", false);

        boolean value1 = attributes.get("key1", false);
        assertTrue(value1);

        boolean value2 = attributes.get("key2", false);
        assertFalse(value2);

        // Test for put() method with boolean value and same key
        attributes.put("key1", false);
        attributes.put("key1", true);

        boolean value1AfterChange = attributes.get("key1", false);
        assertTrue(value1AfterChange);
    }

    // Test for hasKey() method

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    // Test for hasKeyIgnoreCase() method

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    // Test for size() method

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int size = attributes.size();
        assertEquals(2, size);
    }

    // Test for addAll() method

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);

        int size = attributes1.size();
        assertEquals(4, size);

        String value1 = attributes1.get("key1");
        assertEquals("value1", value1);

        String value2 = attributes1.get("key2");
        assertEquals("value2", value2);

        String value3 = attributes1.get("key3");
        assertEquals("value3", value3);

        String value4 = attributes1.get("key4");
        assertEquals("value4", value4);
    }

    // Test for iterator() method

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedList = Collections.singletonList(new AbstractMap.SimpleEntry<>("key1", "value1"));
        List<Map.Entry<String, String>> actualList = new ArrayList<>();

        for (Map.Entry<String, String> entry : attributes.asList().iterator()) {
            actualList.add(entry);
        }

        assertEquals(expectedList, actualList);
    }

    // Test for asList() method

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedList = Arrays.asList(new AbstractMap.SimpleEntry<>("key1", "value1"), new AbstractMap.SimpleEntry<>("key2", "value2"));
        List<Map.Entry<String, String>> actualList = attributes.asList();

        assertEquals(expectedList, actualList);
    }

    // Test for dataset() method

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> actualMap = attributes.dataset();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");

        assertEquals(expectedMap, actualMap);
    }
}