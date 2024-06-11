package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));

        attributes.put("key1", "newValue1");
        assertEquals("newValue1", attributes.get("key1"));

        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key2"));

        attributes.put("key1", null);
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.putIgnoreCase("key1", "newValue1");
        assertEquals("newValue1", attributes.get("key1"));

        attributes.putIgnoreCase("key2", "value2");
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        attributes.put("key1", true);
        assertFalse(attributes.get("key1").equals("true"));
        assertNull(attributes.get("key1"));

        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();

        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);

        assertEquals("value1", attributes.get("key1"));
        assertEquals(attribute, attributes.get(0));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));
        assertTrue(attributes.size() == 0);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("Key2"));

        attributes.removeIgnoreCase("Key2");
        assertFalse(attributes.hasKey("Key2"));
        assertTrue(attributes.size() == 0);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKey("key1"));
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        assertFalse(attributes.hasKeyIgnoreCase("KEY1"));
        attributes.putIgnoreCase("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKeyIgnoreCaseMultiple() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> list = new ArrayList<>();

        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            list.add(attribute);
        }

        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();

        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertEquals(" key1=\"value1\"", attributes.html());

        attributes.put("key2", "value2");
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());

        attributes.put("key3", null);
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());

        attributes.put("key4", "");
        assertEquals(" key1=\"value1\" key2=\"value2\" key4=\"\"", attributes.html());

        attributes.put("key5", "");
        assertEquals(" key1=\"value1\" key2=\"value2\" key5=\"\"", attributes.html());

        attributes.put("key6", false);
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());

        attributes.put("key7", true);
        assertEquals(" key1=\"value1\" key2=\"value2\" key7", attributes.html());
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");

        attributes.normalize();

        assertEquals("key1", attributes.get("key1").toLowerCase());
        assertEquals("key2", attributes.get("key2").toLowerCase());
    }
}