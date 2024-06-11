package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key3"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("key3"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value3");
        assertEquals("value2", attributes.get("key1"));
        assertEquals("value3", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));

        attributes.putIgnoreCase("KEY3", "value3");
        assertEquals("value3", attributes.getIgnoreCase("key3"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertEquals("", attributes.get("key1"));
        assertTrue(attributes.hasKey("key1"));

        attributes.remove("key1");
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
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertEquals("value2", attributes.get("key2"));
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        attributes.removeIgnoreCase("key1");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKey("key3"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testHasKeyIgnoreCaseMultiple() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");
        attributes.put("KEY3", "value3");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY3"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
    }

    @Test
    public void testRemoveNonExistentKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();
        Iterator<Attribute> iterator = attributes.iterator();

        assertEquals(attributesList.get(0).getValue(), iterator.next().getValue());
        assertEquals(attributesList.get(1).getValue(), iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        String actualHtml = attributes.html();
        assertEquals(expectedHtml.toString(), actualHtml);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String expectedToString = "Attributes{size=2, keys=[key1, key2], vals=[value1, value2]}";
        String actualToString = attributes.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key1", "value1");
        attributes2.put("key3", "value3");
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");
        attributes3.put("key3", "value3");

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes3);
        assertNotEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key1", "value1");
        attributes2.put("key3", "value3");
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");
        attributes3.put("key3", "value3");

        int attributes1HashCode = attributes1.hashCode();
        int attributes2HashCode = attributes2.hashCode();
        int attributes3HashCode = attributes3.hashCode();

        assertEquals(attributes1HashCode, attributes1HashCode);
        assertEquals(attributes1HashCode, attributes3HashCode);
        assertNotEquals(attributes1HashCode, attributes2HashCode);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes, clonedAttributes);
    }
}