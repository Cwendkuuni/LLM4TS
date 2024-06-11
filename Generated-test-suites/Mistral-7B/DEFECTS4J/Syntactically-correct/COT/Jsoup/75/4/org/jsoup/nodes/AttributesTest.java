package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;

import java.util.Arrays;

/**
 * Test class for Attributes.java
 */
public class AttributesTest {

    @org.junit.Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(EmptyString, attributes.get("emptyKey"));
    }

    @org.junit.Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("Key"));
        assertEquals(EmptyString, attributes.getIgnoreCase("EmptyKey"));
    }

    @org.junit.Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "newValue1");
        assertEquals("newValue1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @org.junit.Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");
        attributes.put("key1", "newValue1");
        assertEquals("newValue1", attributes.get("key1"));
        assertEquals("value2", attributes.get("Key2"));
    }

    @org.junit.Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.get("booleanKey").equals(EmptyString));
        assertFalse(attributes.hasKey("emptyBooleanKey"));
    }

    @org.junit.Test
    public void testPutBooleanRemove() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
    }

    @org.junit.Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @org.junit.Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals(4, attributes3.size());
        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));
        assertEquals("value3", attributes3.get("key3"));
        assertEquals("value4", attributes3.get("key4"));
    }

    @org.junit.Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @org.junit.Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @org.junit.Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        attributes.remove("key1");
        assertEquals(2, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKey("key3"));
    }

    @org.junit.Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");
        attributes.put("Key3", "value3");

        attributes.removeIgnoreCase("key1");
        assertEquals(2, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKey("Key2"));
        assertTrue(attributes.hasKey("Key3"));
    }

    @org.junit.Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    @org.junit.Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();
        String expectedHtml = " key1=\"value1\" key2=\"value2\"";
        assertEquals(expectedHtml, html);
    }

    @org.junit.Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String toString = attributes.toString();
        String expectedToString = attributes.html();
        assertEquals(expectedToString, toString);
    }

    @org.junit.Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(2, clonedAttributes.size());
        assertEquals("value1", clonedAttributes.get("key1"));
        assertEquals("value2", clonedAttributes.get("key2"));
    }

    @org.junit.Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        Attributes attributes4 = new Attributes();
        attributes4.put("key2", "value2");
        attributes4.put("key3", "value3");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes4));
    }

    @org.junit.Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        Attributes attributes4 = new Attributes();
        attributes4.put("key2", "value2");
        attributes4.put("key3", "value3");

        int hashCode1 = attributes1.hashCode();
        int hashCode2 = attributes2.hashCode();
        int hashCode3 = attributes3.hashCode();
        int hashCode4 = attributes4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }
}