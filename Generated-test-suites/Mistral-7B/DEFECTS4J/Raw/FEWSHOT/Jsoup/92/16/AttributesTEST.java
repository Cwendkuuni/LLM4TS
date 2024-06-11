package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
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
        assertEquals("", attributes.get("key2"));

        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));

        attributes.putIgnoreCase("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertFalse(attributes.get("key1").equals(""));
        assertEquals("", attributes.get("key2"));

        attributes.put("key1", false);
        assertEquals("", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key2"));

        attributes.remove("key2");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");
        assertEquals(2, attributes.size());

        attributes.removeIgnoreCase("key1");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("Key2"));

        attributes.removeIgnoreCase("KEY2");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> attributesList = new ArrayList<>();
        while (iterator.hasNext()) {
            attributesList.add(iterator.next());
        }

        assertEquals(2, attributesList.size());
        assertEquals("key1", attributesList.get(0).getKey());
        assertEquals("value1", attributesList.get(0).getValue());
        assertEquals("key2", attributesList.get(1).getKey());
        assertEquals("value2", attributesList.get(1).getValue());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();
        assertEquals(2, attributesList.size());
        assertEquals("key1", attributesList.get(0).getKey());
        assertEquals("value1", attributesList.get(0).getValue());
        assertEquals("key2", attributesList.get(1).getKey());
        assertEquals("value2", attributesList.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("key1", dataset.keySet().iterator().next());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("key2", dataset.keySet().iterator().next());
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, new Document.OutputSettings());

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }
}