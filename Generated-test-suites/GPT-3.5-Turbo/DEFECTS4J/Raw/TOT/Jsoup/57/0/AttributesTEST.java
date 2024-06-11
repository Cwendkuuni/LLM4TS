package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");

        assertEquals("John", attributes.get("name"));
        assertEquals("25", attributes.get("age"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");

        assertEquals("John", attributes.getIgnoreCase("NAME"));
        assertEquals("25", attributes.getIgnoreCase("aGe"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");

        assertEquals("John", attributes.get("name"));
        assertEquals("25", attributes.get("age"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        attributes.put("disabled", false);

        assertEquals("true", attributes.get("checked"));
        assertFalse(attributes.hasKey("disabled"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        attributes.remove("name");

        assertFalse(attributes.hasKey("name"));
        assertEquals("25", attributes.get("age"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        attributes.removeIgnoreCase("Name");

        assertFalse(attributes.hasKey("name"));
        assertEquals("25", attributes.get("age"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");

        assertTrue(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("age"));
        assertFalse(attributes.hasKey("gender"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");

        assertTrue(attributes.hasKeyIgnoreCase("NAME"));
        assertTrue(attributes.hasKeyIgnoreCase("aGe"));
        assertFalse(attributes.hasKeyIgnoreCase("gender"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        attributes.remove("name");

        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");

        Attributes attributes2 = new Attributes();
        attributes2.put("gender", "male");
        attributes2.put("city", "New York");

        attributes1.addAll(attributes2);

        assertEquals(4, attributes1.size());
        assertEquals("John", attributes1.get("name"));
        assertEquals("25", attributes1.get("age"));
        assertEquals("male", attributes1.get("gender"));
        assertEquals("New York", attributes1.get("city"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");

        Iterator<Attribute> iterator = attributes.iterator();
        assertTrue(iterator.hasNext());
        Attribute attribute = iterator.next();
        assertEquals("name", attribute.getKey());
        assertEquals("John", attribute.getValue());
        assertTrue(iterator.hasNext());
        attribute = iterator.next();
        assertEquals("age", attribute.getKey());
        assertEquals("25", attribute.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");

        List<Attribute> attributeList = attributes.asList();
        assertEquals(2, attributeList.size());
        assertEquals("name", attributeList.get(0).getKey());
        assertEquals("John", attributeList.get(0).getValue());
        assertEquals("age", attributeList.get(1).getKey());
        assertEquals("25", attributeList.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");

        String html = attributes.html();

        assertTrue(html.contains("name=\"John\""));
        assertTrue(html.contains("age=\"25\""));
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");

        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        attributes2.put("age", "25");

        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");

        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        attributes2.put("age", "25");

        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");

        Attributes attributes2 = attributes1.clone();

        assertEquals(attributes1, attributes2);
    }
}