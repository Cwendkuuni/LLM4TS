package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Map;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3"); // duplicated key

        assertEquals(3, attributes.size());
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());

        attributes.put("key1", "value3");
        assertEquals("value3", attributes.get("key1"));

        attributes.put("key3", "value3");
        assertEquals(3, attributes.size());
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());

        attributes.putIgnoreCase("key1", "value3");
        assertEquals("value3", attributes.get("key1"));

        attributes.putIgnoreCase("key3", "value3");
        assertEquals(3, attributes.size());
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.get("key1").isEmpty());

        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.size() == 0);
    }

    @Test
    public void testPutIgnoreCaseBoolean() {
        Attributes attributes = new Attributes();

        attributes.putIgnoreCase("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.get("key1").isEmpty());

        attributes.putIgnoreCase("key1", false);
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.size() == 0);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("key3"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.remove("key2");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.removeIgnoreCase("key1");
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.removeIgnoreCase("key2");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);

        assertEquals(4, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
        assertTrue(attributes1.hasKey("key4"));

        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
        assertEquals("value3", attributes1.get("key3"));
        assertEquals("value4", attributes1.get("key4"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-key1"));
        assertTrue(dataset.containsKey("data-key2"));

        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test-class");
        attributes.put("style", "background-color: red; color: white;");

        assertEquals("<class='test-class' style='background-color: red; color: white;'>", attributes.html());
    }
}