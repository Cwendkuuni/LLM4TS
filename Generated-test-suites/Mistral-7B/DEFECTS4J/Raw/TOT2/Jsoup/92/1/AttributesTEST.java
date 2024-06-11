package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetEmptyStringMethod() {
        Attributes attributes = new Attributes();
        String actual = attributes.get("nonExistentKey");
        String expected = "";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "Value";
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutBooleanAttributeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        String actual = attributes.get("booleanKey");
        assertEquals("", actual);
        boolean expected = false;
        Assert.assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutBooleanAttributeMethodWithTrueValue() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        String actual = attributes.get("booleanKey");
        assertEquals("", actual);
        boolean expected = true;
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutAttributeMethodWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String actual = attributes.get("key");
        String expected = "value2";
        assertEquals(expected, actual);
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
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        String actual1 = attributes1.get("key1");
        String expected1 = "value1";
        assertEquals(expected1, actual1);
        String actual2 = attributes1.get("key2");
        String expected2 = "value2";
        assertEquals(expected2, actual2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllNullAttributesMethod() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethodWithNonExistentKey() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKeyIgnoreCase("nonExistentKey");
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        String expected = "";
        assertEquals(expected, actual);
        boolean hasKey = attributes.hasKey("key");
        assertFalse(hasKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testRemoveMethodWithNonExistentKey() {
        Attributes attributes = new Attributes();
        attributes.remove("nonExistentKey");
        boolean hasKey = attributes.hasKey("nonExistentKey");
        assertFalse(hasKey);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveMethodWithIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove(Integer.MAX_VALUE);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> actual = attributes.dataset();
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, actual);
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        StringBuilder actual = new StringBuilder();
        attributes.html(actual, new Document.OutputSettings());
        StringBuilder expected = new StringBuilder();
        expected.append(" key=\"value\"");
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToStringMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.toString();
        String expected = " key=\"value\"";
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        String actual = clonedAttributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }
}