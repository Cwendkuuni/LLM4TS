package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        String result = attributes.getIgnoreCase("key");
        assertEquals("value", result);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        boolean result = attributes.hasKeyIgnoreCase("key");
        assertTrue(result);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        assertEquals(2, result);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean result = attributes.hasKey("key");
        assertTrue(result);
        String value = attributes.get("key");
        assertTrue(value.equals("true"));

        attributes.put("key", false);
        result = attributes.hasKey("key");
        assertFalse(result);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        boolean result = attributes.hasKey("key1");
        assertFalse(result);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("key2", "value2");
        attributes.removeIgnoreCase("Key1");
        boolean result = attributes.hasKeyIgnoreCase("Key1");
        assertFalse(result);
    }

    @Test
    public void testAddAllMethod() {
        Attributes firstAttributes = new Attributes();
        firstAttributes.put("key1", "value1");
        firstAttributes.put("key2", "value2");

        Attributes secondAttributes = new Attributes();
        secondAttributes.put("key3", "value3");
        secondAttributes.put("key4", "value4");

        Attributes resultAttributes = new Attributes();
        resultAttributes.addAll(firstAttributes);
        resultAttributes.addAll(secondAttributes);

        Map<String, String> expectedMap = new LinkedHashMap<>();
        expectedMap.putAll(firstAttributes.asMap());
        expectedMap.putAll(secondAttributes.asMap());

        Map<String, String> resultMap = resultAttributes.asMap();
        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> resultMap = attributes.dataset();
        Map<String, String> expectedMap = new LinkedHashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");

        assertEquals(expectedMap, resultMap);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodThrowsException() {
        Attributes attributes = new Attributes();
        attributes.html(null, null);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        assertTrue(attributes1.equals(attributes2));

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        int result1 = attributes1.hashCode();
        int result2 = attributes2.hashCode();

        assertEquals(result1, result2);

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        int result3 = attributes1.hashCode();
        int result4 = attributes3.hashCode();

        assertNotEquals(result3, result4);
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();

        Map<String, String> originalMap = attributes.asMap();
        Map<String, String> clonedMap = clonedAttributes.asMap();

        assertEquals(originalMap, clonedMap);
    }

    @Test
    public void testGetNullAttributes() {
        Attributes attributes = new Attributes();

        String result = attributes.get(null);
        assertEquals("", result);

        String resultIgnoreCase = attributes.getIgnoreCase(null);
        assertEquals("", resultIgnoreCase);
    }

    @Test
    public void testPutNullKey() {
        Attributes attributes = new Attributes();

        try {
            attributes.put(null, "value");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();

        attributes.put("key", null);
        String result = attributes.get("key");
        assertEquals("", result);
    }
}