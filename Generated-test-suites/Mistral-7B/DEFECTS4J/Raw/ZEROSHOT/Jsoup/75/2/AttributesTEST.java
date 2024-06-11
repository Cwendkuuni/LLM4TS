package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testInitialState() {
        assertTrue(attributes.size() == 0);
    }

    @Test
    public void testGetEmpty() {
        assertEquals(Attributes.EMPTY_STRING, attributes.get(""));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        attributes.put("key", null);
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.put("key", "value");
        attributes.putIgnoreCase("Key", "value2");
        assertEquals("value", attributes.get("key"));
        assertEquals("value2", attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.get("booleanKey").isEmpty());
        assertFalse(attributes.hasKey("BooleanKey"));
        assertNull(attributes.get("BooleanKey"));
        assertFalse(attributes.hasKeyIgnoreCase("BooleanKey"));
        assertTrue(attributes.getIgnoreCase("booleanKey").isEmpty());
    }

    @Test
    public void testPutReplace() {
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutMultiple() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals(2, attributes.size());
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertEquals("value2", attributes.get("key2"));
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");
        attributes.removeIgnoreCase("Key2");
        assertEquals("value1", attributes.get("key1"));
        assertFalse(attributes.hasKey("Key2"));
        assertNull(attributes.get("Key2"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("Key2"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");
        assertTrue(attributes.hasKeyIgnoreCase("Key2"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY2"));
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
        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");
        attributes.addAll(attributes2);
        assertEquals(4, attributes.size());
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value3", attributes.get("key3"));
        assertEquals("value4", attributes.get("key4"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Map.Entry<String, String>> entryList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes.asList().iterator()) {
            entryList.add(entry);
        }
        assertEquals(2, entryList.size());
        assertEquals("key1", entryList.get(0).getKey());
        assertEquals("value1", entryList.get(0).getValue());
        assertEquals("key2", entryList.get(1).getKey());
        assertEquals("value2", entryList.get(1).getValue());
    }

    @Test
    public void testDataset() {
        attributes.put("key1", "value1");
        attributes.put("data:key2", "value2");
        Map<String, String> dataSet = attributes.dataset();
        assertEquals(1, dataSet.size());
        assertEquals("value2", dataSet.get("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        StringBuilder sb = new StringBuilder();
        try {
            attributes.html(sb, Document.OutputSettings.standardOutputSettings());
        } catch (IOException e) {
            fail("Unexpected exception when generating HTML: " + e.getMessage());
        }
        assertEquals("key1=\"value1\"", sb.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        assertTrue(attributes1.equals(attributes2));
        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }
}