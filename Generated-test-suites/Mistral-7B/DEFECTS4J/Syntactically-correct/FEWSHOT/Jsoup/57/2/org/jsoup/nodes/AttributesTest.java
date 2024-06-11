package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        assertEquals("value1", attrs.get("key1"));
        assertEquals("value2", attrs.get("key2"));
        assertEquals("", attrs.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("Key1", "Value1");
        attrs.put("Key2", "Value2");

        assertEquals("Value1", attrs.getIgnoreCase("key1"));
        assertEquals("Value2", attrs.getIgnoreCase("key2"));
        assertEquals("", attrs.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key1", "value2");

        assertEquals("value2", attrs.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attrs = new Attributes();
        attrs.put("key1", true);
        attrs.put("key1", false);
        assertTrue(attrs.hasKey("key1"));
        assertFalse(attrs.get("key1").getValue().equals("true"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attrs = new Attributes();
        Attribute attr = new Attribute("key1", "value1");
        attrs.put(attr);

        assertEquals("value1", attrs.get("key1").getValue());
    }

    @Test
    public void testRemove() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.remove("key1");
        assertNull(attrs.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("Key1", "Value1");
        attrs.removeIgnoreCase("key1");
        assertNull(attrs.get("Key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        assertTrue(attrs.hasKey("key1"));
        assertFalse(attrs.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("Key1", "Value1");

        assertTrue(attrs.hasKeyIgnoreCase("key1"));
        assertFalse(attrs.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        assertEquals(2, attrs.size());
    }

    @Test
    public void testAddAll() {
        Attributes attrs1 = new Attributes();
        attrs1.put("key1", "value1");

        Attributes attrs2 = new Attributes();
        attrs2.put("key2", "value2");
        attrs2.put("key3", "value3");

        attrs1.addAll(attrs2);

        assertEquals(3, attrs1.size());
        assertTrue(attrs1.hasKey("key1"));
        assertTrue(attrs1.hasKey("key2"));
        assertTrue(attrs1.hasKey("key3"));
    }

    @Test
    public void testDataset() {
        Attributes attrs = new Attributes();
        attrs.put("dataKey1", "dataValue1");
        attrs.put("dataKey2", "dataValue2");

        Map<String, String> dataset = attrs.dataset();

        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("dataKey1"));
        assertTrue(dataset.containsKey("dataKey2"));
    }

    @Test
    public void testHtml() throws IOException {
        Document doc = new Document();
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        String html = attrs.html();

        String expectedHtml = " key1=\"value1\"";
        assertEquals(expectedHtml, html);
    }

    @Test
    public void testEquals() {
        Attributes attrs1 = new Attributes();
        attrs1.put("key1", "value1");

        Attributes attrs2 = new Attributes();
        attrs2.put("key1", "value1");

        Attributes attrs3 = new Attributes();
        attrs3.put("key2", "value2");

        assertEquals(attrs1, attrs1);
        assertEquals(attrs1, attrs2); // equals for same contents
        assertNotEquals(attrs1, attrs3); // not equals for different contents
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        Attributes clonedAttributes = attrs.clone();

        assertEquals(attrs, clonedAttributes);
    }
}