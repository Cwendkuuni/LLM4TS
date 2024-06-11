package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGetEmpty() {
        Attributes attrs = new Attributes();
        assertEquals("", attrs.get("key"));
        assertFalse(attrs.hasKey("key"));
    }

    @Test
    public void testPutGet() {
        Attributes attrs = new Attributes();
        attrs.put("key", "value");
        assertEquals("value", attrs.get("key"));
        assertTrue(attrs.hasKey("key"));
    }

    @Test
    public void testPutOverwrite() {
        Attributes attrs = new Attributes();
        attrs.put("key", "value1");
        attrs.put("key", "value2");
        assertEquals("value2", attrs.get("key"));
    }

    @Test
    public void testPutNull() {
        Attributes attrs = new Attributes();
        attrs.put(null);
        assertNull(attrs.get(null));
        assertFalse(attrs.hasKey(null));
    }

    @Test
    public void testPutBooleanTrue() {
        Attributes attrs = new Attributes();
        attrs.put("key", true);
        assertTrue(attrs.get("key").isBoolean());
        assertTrue(attrs.hasKey("key"));
    }

    @Test
    public void testPutBooleanFalse() {
        Attributes attrs = new Attributes();
        attrs.put("key", false);
        assertFalse(attrs.get("key").isBoolean());
        assertFalse(attrs.hasKey("key"));
    }

    @Test
    public void testRemove() {
        Attributes attrs = new Attributes();
        attrs.put("key", "value");
        attrs.remove("key");
        assertNull(attrs.get("key"));
        assertFalse(attrs.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("key", "value");
        attrs.removeIgnoreCase("Key");
        assertNull(attrs.get("key"));
        assertFalse(attrs.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");
        assertTrue(attrs.hasKey("key1"));
        assertTrue(attrs.hasKey("key2"));
        assertFalse(attrs.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("KEY2", "value2");
        assertTrue(attrs.hasKeyIgnoreCase("key1"));
        assertTrue(attrs.hasKeyIgnoreCase("KEY2"));
        assertFalse(attrs.hasKeyIgnoreCase("key3"));
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

        Attributes attrs3 = new Attributes();
        attrs3.addAll(attrs1);
        attrs3.addAll(attrs2);

        assertEquals(2, attrs3.size());
        assertEquals("value1", attrs3.get("key1"));
        assertEquals("value2", attrs3.get("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        List<Attribute> attributes = new ArrayList<>(attrs);
        List<Attribute> list = attrs.asList();

        assertEquals(2, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(attributes.get(i), list.get(i));
        }
    }

    @Test
    public void testDataset() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("data-key2", "value2");

        Map<String, String> dataset = attrs.dataset();
        assertEquals(1, dataset.size());
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        StringBuilder html = new StringBuilder();
        attrs.html(html, new Document("").outputSettings());

        assertEquals("<key1>value1</key1>", html.toString());
    }

    @Test
    public void testEquals() {
        Attributes attrs1 = new Attributes();
        attrs1.put("key1", "value1");

        Attributes attrs2 = new Attributes();
        attrs2.put("key1", "value1");

        Attributes attrs3 = new Attributes();
        attrs3.put("key2", "value2");

        assertTrue(attrs1.equals(attrs1));
        assertTrue(attrs1.equals(attrs2));
        assertFalse(attrs1.equals(attrs3));
    }

    @Test
    public void testClone() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        Attributes cloned = attrs.clone();

        assertEquals(attrs.size(), cloned.size());
        assertEquals(attrs.get("key1"), cloned.get("key1"));
    }

    @Test
    public void testDatasetIterator() {
        Attributes attrs = new Attributes();
        attrs.put("data-key1", "value1");
        attrs.put("data-key2", "value2");
        attrs.put("key3", "value3");

        Map<String, String> dataset = attrs.dataset();

        Iterator<Map.Entry<String, String>> iterator = dataset.entrySet().iterator();
        assertEquals("value1", iterator.next().getValue());
        assertEquals("value2", iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        assertEquals("", attrs.getIgnoreCase("key1"));
        assertEquals("", attrs.getIgnoreCase("KEY1"));
        assertEquals("value1", attrs.getIgnoreCase("key1"));
        assertEquals("value1", attrs.getIgnoreCase("Key1"));
        assertEquals("", attrs.getIgnoreCase("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attrs = new Attributes();

        attrs.putIgnoreCase("key1", "value1");
        attrs.putIgnoreCase("KEY1", "value2");

        assertEquals("value1", attrs.get("key1"));
        assertEquals("value2", attrs.get("KEY1"));
        assertEquals(2, attrs.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("KEY1", "value2");

        attrs.removeIgnoreCase("key1");

        assertEquals("value2", attrs.get("KEY1"));
        assertFalse(attrs.hasKey("key1"));
    }
}