package org.jsoup.nodes; // Update the package name if required

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class AttributesTest {
    

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullKey() {
        Attributes attributes = new Attributes();
        String actual = attributes.get(null);
        fail("Expected NullPointerException but got: " + actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        String actual = attributes.getIgnoreCase("key");
        String expected = "VALUE";
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testGetIgnoreCaseWithNullKey() {
        Attributes attributes = new Attributes();
        String actual = attributes.getIgnoreCase(null);
        fail("Expected NullPointerException but got: " + actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testPutWithNullKey() {
        Attributes attributes = new Attributes();
        String value = "value";
        attributes.put(null, value);
        fail("Expected NullPointerException but got: " + attributes.size());
    }

    @Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String actual = attributes.get("key");
        String expected = "value2";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.get("key").equals(EmptyString);
        assertTrue(actual);
    }

    @Test
    public void testPutIgnoreCaseBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", true);
        boolean actual = attributes.get("key").equals(EmptyString);
        assertTrue(actual);
    }

    @Test
    public void testPutIgnoreCaseExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value1");
        attributes.putIgnoreCase("key", "value2");
        String actual = attributes.get("Key");
        String expected = "value2";
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        assertEquals(EmptyString, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
        fail("Expected NullPointerException but got: " + attributes.size());
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        String actual = attributes.get("Key");
        assertEquals(EmptyString, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveIgnoreCaseWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase(null);
        fail("Expected NullPointerException but got: " + attributes.size());
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        assertTrue(actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        assertTrue(actual);
    }

    @Test
    public void testHasKeyIgnoreCaseCaseSensitiveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        boolean actual = attributes.hasKey("Key");
        assertFalse(actual);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int actualSize1 = attributes1.size();
        int actualSize2 = attributes2.size();
        int expectedSize = actualSize1 + actualSize2;
        assertEquals(expectedSize, actualSize1);

        for (Map.Entry<String, String> entry : attributes2.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String actualValue = attributes1.get(key);
            assertEquals(value, actualValue);
        }
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedList = Arrays.asList(
                new AbstractMap.SimpleImmutableEntry<>("key1", "value1"),
                new AbstractMap.SimpleImmutableEntry<>("key2", "value2")
        );

        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            actualList.add(new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), entry.getValue()));
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedList = Arrays.asList(
                new AbstractMap.SimpleImmutableEntry<>("key1", "value1"),
                new AbstractMap.SimpleImmutableEntry<>("key2", "value2")
        );

        List<Map.Entry<String, String>> actualList = attributes.asList();

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");

        Map<String, String> actualMap = attributes.dataset();

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key=\"value\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, null);

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodWithException() {
        Attributes attributes = new Attributes();

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, null); // This should not throw an exception, but we are testing for it

        attributes.put("Exception", "value");
        attributes.html(actualHtml, null); // This should throw an exception

        fail("Expected SerializationException but got: " + actualHtml.toString());
    }

    
}