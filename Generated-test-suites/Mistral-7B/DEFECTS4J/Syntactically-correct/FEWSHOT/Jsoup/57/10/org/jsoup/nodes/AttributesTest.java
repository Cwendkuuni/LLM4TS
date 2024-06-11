package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attrs = new Attributes();
        attrs.put("key", "value");
        assertEquals("value", attrs.get("key"));
        assertEquals("", attrs.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("Key", "value");
        assertEquals("value", attrs.getIgnoreCase("key"));
        assertEquals("value", attrs.getIgnoreCase("Key"));
        assertEquals("", attrs.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attrs = new Attributes();
        attrs.put("key", "value1");
        attrs.put("key", "value2");
        assertEquals("value2", attrs.get("key"));

        Attributes otherAttrs = new Attributes();
        otherAttrs.put("key2", "value3");
        attrs.put(otherAttrs);
        assertTrue(attrs.size() == 2);
        assertEquals("value2", attrs.get("key"));
        assertEquals("value3", attrs.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attrs = new Attributes();
        attrs.put("key", true);
        assertTrue(attrs.get("key").equals(BooleanAttribute.TRUE));

        attrs.put("key", false);
        assertNull(attrs.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attrs = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attrs.put(attribute);
        assertEquals("value", attrs.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");
        attrs.remove("key1");
        assertNull(attrs.get("key1"));
        assertEquals("value2", attrs.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("Key2", "value2");
        attrs.removeIgnoreCase("key1");
        assertNull(attrs.get("key1"));
        assertEquals("value2", attrs.get("Key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");
        assertTrue(attrs.hasKey("key1"));
        assertTrue(attrs.hasKeyIgnoreCase("key1"));
        assertTrue(attrs.hasKey("key2"));
        assertTrue(attrs.hasKeyIgnoreCase("Key2"));
        assertFalse(attrs.hasKey("nonExistentKey"));
        assertFalse(attrs.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attrs = new Attributes();
        assertEquals(0, attrs.size());

        attrs.put("key1", "value1");
        attrs.put("key2", "value2");
        assertEquals(2, attrs.size());

        attrs.remove("key1");
        assertEquals(1, attrs.size());
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

        assertEquals("value1", attrs3.get("key1"));
        assertEquals("value2", attrs3.get("key2"));
        assertEquals(2, attrs3.size());
    }

    @Test
    public void testDataset() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("data-key2", "value2");

        Map<String, String> dataset = attrs.dataset();
        assertEquals("value2", dataset.get("key2"));
        assertEquals(1, dataset.size());

        attrs.remove("data-key2");
        assertEquals(0, dataset.size());
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("class", "classValue");

        StringBuilder expected = new StringBuilder();
        expected.append(" class=\"classValue\"");
        expected.append(" data-key1=\"value1\"");

        StringBuilder actual = new StringBuilder();
        attrs.html(actual, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("class", "classValue");

        StringBuilder expected = new StringBuilder();
        expected.append(" class=\"classValue\"");
        expected.append(" data-key1=\"value1\"");

        StringBuilder actual = new StringBuilder();
        actual.append(attrs.toString());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Attributes attrs1 = new Attributes();
        attrs1.put("key1", "value1");

        Attributes attrs2 = new Attributes();
        attrs2.put("key1", "value1");

        Attributes attrs3 = new Attributes();
        attrs3.put("key2", "value2");

        Attributes attrs4 = attrs1;

        assertTrue(attrs1.equals(attrs1));
        assertTrue(attrs1.equals(attrs2));
        assertFalse(attrs1.equals(attrs3));
        assertTrue(attrs1.equals(attrs4));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        Attributes cloned = attrs.clone();
        assertEquals("value1", cloned.get("key1"));
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneThrowsException() throws CloneNotSupportedException {
        Attributes attrs = new Attributes();
        attrs.clone();
    }
}