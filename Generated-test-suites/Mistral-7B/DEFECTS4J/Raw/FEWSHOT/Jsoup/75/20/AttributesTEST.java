package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attrs = new Attributes();
        assertEquals(0, attrs.size());

        attrs.put("key1", "value1");
        attrs.put("key2", "value2");
        attrs.put("key3", "value3");
        assertEquals(3, attrs.size());
    }

    @Test
    public void testPutGet() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        assertEquals("value1", attrs.get("key1"));
        attrs.put("key2", "value2");
        assertEquals("value2", attrs.get("key2"));
        assertEquals("", attrs.get("key3"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.putIgnoreCase("Key1", "Value1");
        assertEquals("Value1", attrs.get("Key1"));
        assertEquals("Value1", attrs.getIgnoreCase("Key1"));
        assertEquals("", attrs.get("Key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attrs = new Attributes();
        attrs.put("key1", false);
        assertFalse(attrs.get("key1").isEmpty());
        assertEquals(false, attrs.hasKey("key1"));
        assertTrue(attrs.hasKeyIgnoreCase("key1"));
        attrs.put("key1", true);
        assertEquals(false, attrs.get("key1").isEmpty());
        assertTrue(attrs.hasKey("key1"));
        assertFalse(attrs.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attrs = new Attributes();
        String key = "key1";
        attrs.put(new Attribute(key, "value1"));
        assertEquals("value1", attrs.get(key));

        attrs.put(new Attribute(key, "value2"));
        assertEquals("value2", attrs.get(key));
        assertEquals(1, attrs.size());
    }

    @Test
    public void testRemove() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");
        assertEquals(2, attrs.size());

        attrs.remove("key1");
        assertEquals(1, attrs.size());
        assertEquals("", attrs.get("key1"));

        attrs.remove("key2");
        assertEquals(0, attrs.size());
        assertEquals("", attrs.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.putIgnoreCase("key1", "Value1");
        attrs.putIgnoreCase("key2", "Value2");
        attrs.putIgnoreCase("key1", "Value3");

        attrs.removeIgnoreCase("key1");
        assertEquals(1, attrs.size());
        assertNull(attrs.get("key1"));

        attrs.removeIgnoreCase("Key1");
        assertEquals(1, attrs.size());
        assertNull(attrs.get("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        assertTrue(attrs.hasKey("key1"));
        assertTrue(attrs.hasKeyIgnoreCase("key1"));
        assertFalse(attrs.hasKey("key3"));
        assertFalse(attrs.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.putIgnoreCase("Key1", "Value1");
        attrs.putIgnoreCase("Key2", "Value2");

        assertTrue(attrs.hasKeyIgnoreCase("Key1"));
        assertTrue(attrs.hasKeyIgnoreCase("key1"));
        assertFalse(attrs.hasKeyIgnoreCase("Key3"));
        assertFalse(attrs.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testAddAll() {
        Attributes attrs1 = new Attributes();
        attrs1.put("key1", "value1");
        attrs1.put("key2", "value2");

        Attributes attrs2 = new Attributes();
        attrs2.put("key2", "value3");
        attrs2.put("key3", "value3");

        attrs1.addAll(attrs2);

        assertEquals(3, attrs1.size());
        assertEquals("value3", attrs1.get("key2"));
        assertEquals("value3", attrs1.get("key3"));
    }

    @Test
    public void testDataset() {
        Attributes attrs = new Attributes();
        attrs.put("data-key1", "value1");
        attrs.put("data-key2", "value2");

        Map<String, String> dataset = attrs.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        StringBuilder accum = new StringBuilder();
        try {
            attrs.html(accum, null);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        assertEquals(" key1=\"value1\" key2=\"value2\"", accum.toString());
    }
}