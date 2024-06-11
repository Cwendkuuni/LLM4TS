package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testInitialization() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertNull(attributes.get("testKey"));
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        assertEquals(1, attributes.size());
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("testKey", "testValue");
        assertEquals(1, attributes.size());
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", true);
        assertTrue(attributes.hasKey("testKey"));
        assertNull(attributes.get("testKey"));
    }

    @Test
    public void testPutMultiple() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        assertEquals(2, attributes.size());
        assertEquals("testValue1", attributes.get("testKey1"));
        assertEquals("testValue2", attributes.get("testKey2"));
    }

    @Test
    public void testPutOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue1");
        attributes.put("testKey", "testValue2");
        assertEquals(1, attributes.size());
        assertEquals("testValue2", attributes.get("testKey"));
    }

    @Test
    public void testPutIgnoreCaseOverwrite() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("testKey", "testValue1");
        attributes.putIgnoreCase("testKey", "testValue2");
        assertEquals(1, attributes.size());
        assertEquals("testValue2", attributes.get("testKey"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        assertEquals(1, attributes.size());
        attributes.remove("testKey");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("testKey"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("testKey", "testValue");
        assertEquals(1, attributes.size());
        attributes.removeIgnoreCase("testKey");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("testKey"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("testKey"));
        attributes.put("testKey", "testValue");
        assertTrue(attributes.hasKey("testKey"));
        assertFalse(attributes.hasKeyIgnoreCase("TestKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("testKey", "testValue");
        assertTrue(attributes.hasKeyIgnoreCase("testKey"));
        assertTrue(attributes.hasKeyIgnoreCase("TestKey"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");

        Attributes attributes2 = new Attributes();
        attributes2.put("testKey2", "testValue2");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals(2, attributes3.size());
        assertEquals("testValue1", attributes3.get("testKey1"));
        assertEquals("testValue2", attributes3.get("testKey2"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "testValue1");
        attributes.put("data-test2", "testValue2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("testValue1", dataset.get("data-test1"));
        assertEquals("testValue2", dataset.get("data-test2"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" testKey=\"testValue\"");

        String html = attributes.html();
        assertEquals(expectedHtml.toString(), html);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        String expectedToString = "Attributes{size=1, keys=[testKey], vals=[testValue]}";
        String toString = attributes.toString();
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        attributes2.put("testKey1", "testValue1");
        attributes3.put("testKey2", "testValue2");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals(attributes.get("testKey"), clonedAttributes.get("testKey"));
    }
}