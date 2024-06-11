package org.jsoup.nodes; // Import the class under test

import static org.junit.Assert.*; // Import assertion methods from JUnit
import static org.hamcrest.CoreMatchers.*; // Import Hamcrest matchers
import java.util.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

public class AttributesTest {

    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String value = attributes.get("key");
        assertEquals("value", value);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String value = attributes.getIgnoreCase("Key");
        assertEquals("value", value);
    }

    @Test
    public void testGet_WhenKeyDoesntExist_ReturnsEmptyString() {
        Attributes attributes = new Attributes();
        String value = attributes.get("nonExistentKey");
        assertEquals(Attributes.EmptyString, value);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String value = attributes.get("key");
        assertEquals("value", value);
    }

    @Test
    public void testPut_WhenKeyAlreadyExists_UpdatesValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String value = attributes.get("key");
        assertEquals("value2", value);
    }

    @Test
    public void testPut_WhenKeyIsCaseInsensitive_UpdatesValue() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value1");
        attributes.put("key", "value2");
        String value = attributes.get("Key");
        assertEquals("value2", value);
    }

    @Test
    public void testPut_WhenKeyIsBooleanAttribute_SetsValueToTrue() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        boolean value = attributes.getBoolean("key");
        assertTrue(value);
    }

    @Test
    public void testPut_WhenKeyIsBooleanAttribute_RemovesAttributeIfValueIsFalse() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        boolean value = attributes.getBoolean("key");
        assertFalse(value);
        assertEquals(0, attributes.size());
    }

    @Test
    public void testPut_WhenPuttingAnAttributeObject() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        String value = attributes.get("key");
        assertEquals("value", value);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        assertEquals(0, attributes.size());
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
    public void testHasKey_WhenKeyDoesntExist_ReturnsFalse() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        assertEquals(3, result.size());
        assertTrue(result.hasKey("key1"));
        assertTrue(result.hasKey("key2"));
        assertTrue(result.hasKey("key3"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );

        List<Attribute> actualList = new ArrayList<>();
        for (Attribute attribute : attributes) {
            actualList.add(attribute);
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );

        List<Attribute> actualList = attributes.asList();

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("dataKey1", "dataValue1");
        expectedMap.put("dataKey2", "dataValue2");

        Map<String, String> actualMap = attributes.dataset();

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key=\"value\"");

        StringBuilder actualHtml = new StringBuilder();
        try {
            attributes.html(actualHtml, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    
}