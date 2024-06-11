package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testSizeAndCapacity() {
        Attributes attributes = new Attributes();
        int initialCapacity = Attributes.InitialCapacity;
        attributes.checkCapacity(initialCapacity * 2);
        assertEquals(initialCapacity * 2, attributes.keys.length);
        assertEquals(initialCapacity * 2, attributes.vals.length);
    }

    @Test
    public void testGetSetAndRemove() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertNull(attributes.get("key2"));

        attributes.remove("key1");
        assertNull(attributes.get("key1"));

        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key2"));

        attributes.remove("key2");
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertNull(attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key2"));

        attributes.put(null, null);
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        attributes.put("key1", true);
        assertNull(attributes.get("key1"));
        assertTrue(attributes.hasKey("key1"));

        attributes.put("key1", false);
        assertNull(attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();

        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("key1");
        assertNull(attributes.get("key1"));
        attributes.put("Key1", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
        attributes.put("Key2", "value2");
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);

        assertEquals(3, attributes1.size());
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
        assertEquals("value3", attributes1.get("key3"));
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

        for (Attribute attribute : attributes) {
            assertTrue(expectedList.contains(attribute));
            expectedList.remove(attribute);
        }

        assertEquals(0, expectedList.size());
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

        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");

        Map<String, String> actualMap = attributes.dataset();

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml);

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expectedString = new StringBuilder();
        expectedString.append("key1=\"value1\" key2=\"value2\"");

        String actualString = attributes.toString();

        assertEquals(expectedString.toString(), actualString);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key1", "value1");
        attributes2.put("key3", "value3");

        Attributes attributes3 = new Attributes(attributes1);

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(""));
        assertFalse(attributes1.equals(null));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        int attributes1HashCode = attributes1.hashCode();
        int attributes2HashCode = attributes2.hashCode();

        assertEquals(attributes1HashCode, attributes1.hashCode());
        assertEquals(attributes2HashCode, attributes2.hashCode());
        assertNotEquals(attributes1HashCode, attributes2HashCode);

        Attributes attributes3 = new Attributes(attributes1);
        assertEquals(attributes1HashCode, attributes3.hashCode());
    }
}