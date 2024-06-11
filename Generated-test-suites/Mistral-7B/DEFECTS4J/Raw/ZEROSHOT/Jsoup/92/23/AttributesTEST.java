package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class AttributesTest {

    @Test
    public void testAttributesInitialization() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testAddIgnoredCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        assertEquals(1, attributes.size());
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetEmptyAttribute() {
        Attributes attributes = new Attributes();
        assertEquals("", attributes.get("key1"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testPutAttributeWithSameName() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoredCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.remove("key1");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasIgnoredCaseKey() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
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

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        assertEquals(2, result.size());
        assertTrue(result.hasKey("key1"));
        assertTrue(result.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedList = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        );

        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            actualList.add(entry);
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testList() {
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
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

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
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals(attributes.toString(), clonedAttributes.toString());
    }
}