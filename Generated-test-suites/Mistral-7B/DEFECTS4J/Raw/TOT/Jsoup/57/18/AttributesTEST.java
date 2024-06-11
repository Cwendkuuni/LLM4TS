package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attribs = new Attributes();
        attribs.put("key1", "value1");
        assertEquals("value1", attribs.get("key1"));
        assertEquals("", attribs.get("key2"));

        // Test case insensitive key
        attribs.put("Key1", "Value1");
        assertEquals("Value1", attribs.getIgnoreCase("Key1"));
    }

    @Test
    public void testPutMethod() {
        Attributes attribs = new Attributes();
        attribs.put("key1", "value1");
        attribs.put("key1", "newValue1");
        assertEquals("newValue1", attribs.get("key1"));

        attribs.put("key2", "value2");
        Map<String, String> dataset = attribs.dataset();
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attribs = new Attributes();
        attribs.put("key1", false);
        assertTrue(attribs.hasKey("key1"));
        assertFalse(attribs.hasKey("key1"));
        attribs.put("key1", true);
        assertTrue(attribs.hasKey("key1"));
    }

    @Test
    public void testRemoveMethod() {
        Attributes attribs = new Attributes();
        attribs.put("key1", "value1");
        attribs.remove("key1");
        assertFalse(attribs.hasKey("key1"));

        attribs.put("key2", "value2");
        attribs.removeIgnoreCase("Key2");
        assertFalse(attribs.hasKey("key2"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attribs = new Attributes();
        assertEquals(0, attribs.size());

        attribs.put("key1", "value1");
        assertEquals(1, attribs.size());

        attribs.put("key2", "value2");
        assertEquals(2, attribs.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attribs1 = new Attributes();
        Attributes attribs2 = new Attributes();
        attribs2.put("key1", "value1");
        attribs2.put("key2", "value2");

        attribs1.addAll(attribs2);
        assertEquals(2, attribs1.size());
        assertTrue(attribs1.hasKey("key1"));
        assertTrue(attribs1.hasKey("key2"));
    }

    @Test
    public void testIterableMethod() {
        Attributes attribs = new Attributes();
        attribs.put("key1", "value1");
        attribs.put("key2", "value2");

        List<Attribute> attributesList = attribs.asList();
        assertEquals(2, attributesList.size());

        for (Attribute attribute : attributesList) {
            String key = attribute.getKey();
            String value = attribute.getValue();
            if (key.equals("key1")) {
                assertEquals("value1", value);
            } else if (key.equals("key2")) {
                assertEquals("value2", value);
            }
        }
    }

    @Test
    public void testDatasetMethod() {
        Attributes attribs = new Attributes();
        attribs.put("data-key1", "value1");
        attribs.put("data-key2", "value2");

        Map<String, String> dataset = attribs.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-key1"));
        assertTrue(dataset.containsKey("data-key2"));
    }

    @Test
    public void testHtmlMethod() {
        Attributes attribs = new Attributes();
        attribs.put("key1", "value1");
        attribs.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        String actualHtml = attribs.html();
        assertEquals(expectedHtml.toString(), actualHtml);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodException() {
        Attributes attribs = new Attributes();
        attribs.put("key1", "value1");
        attribs.put("key2", "value2");

        Document document = new Document();
        attribs.html(document.appendElement("html").appendElement("body"), null);
    }
}