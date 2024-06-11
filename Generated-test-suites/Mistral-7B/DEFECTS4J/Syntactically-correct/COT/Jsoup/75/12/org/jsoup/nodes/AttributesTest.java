package org.jsoup.nodes; // Import necessary packages

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get(null);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        String actual = attributes.getIgnoreCase("key");
        String expected = "VALUE";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseWithNullKey() {
        Attributes attributes = new Attributes();
        String actual = attributes.getIgnoreCase(null);
        assertEquals(Attributes.EmptyString, actual);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
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
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        attributes.putIgnoreCase("key", "newValue");
        String actual = attributes.getIgnoreCase("key");
        String expected = "newValue";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutWithExistingIgnoreCaseKey() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        attributes.putIgnoreCase("key", "newValue");
        String actual = attributes.getIgnoreCase("Key");
        String expected = "newValue";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.getBoolean("key");
        assertTrue(actual);
    }

    @Test
    public void testPutWithStringValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put(Attributes.EmptyString, "value");
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        assertEquals(Attributes.EmptyString, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        assertTrue(actual);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        assertTrue(actual);
    }

    @Test
    public void testHasKeyWithNullKey() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey(null);
        assertFalse(actual);
    }

    @Test
    public void testHasKeyWithEmptyKey() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey(Attributes.EmptyString);
        assertFalse(actual);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int size = attributes1.size();
        assertEquals(2, size);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Map.Entry<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new AbstractMap.SimpleEntry<>("key1","value1"));
        expectedList.add(new AbstractMap.SimpleEntry<>("key2","value2"));
        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            actualList.add(entry);
        }
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> expectedList = new ArrayList<>();
        expectedList.add(new Attribute("key1", "value1"));
        expectedList.add(new Attribute("key2", "value2"));
        List<Attribute> actualList = attributes.asList();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");
        Map<String, String> actualMap = attributes.dataset();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        String expectedString = "key1=\"value1\" key2=\"value2\"";
        String actualString = attributes.toString();
        assertEquals(expectedString, actualString);
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
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int expectedHashCode = 61 * (51 + Arrays.hashCode(new String[]{"key1", "key2"})) + Arrays.hashCode(new String[]{"value1", "value2"});
        int actualHashCode = attributes.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        String actualValue1 = attributes.get("key1");
        String expectedValue1 = clonedAttributes.get("key1");
        assertEquals(actualValue1, expectedValue1);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.normalize();
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    
}