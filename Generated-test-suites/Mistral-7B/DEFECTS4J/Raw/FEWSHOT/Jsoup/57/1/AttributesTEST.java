package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attrs = new Attributes();
        assertEquals(0, attrs.size());

        attrs.put("test", "value");
        assertEquals(1, attrs.size());

        attrs.put("test2", "value2");
        assertEquals(2, attrs.size());
    }

    @Test
    public void testGet() {
        Attributes attrs = new Attributes();
        attrs.put("test", "value");

        assertEquals("value", attrs.get("test"));
        assertEquals("", attrs.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("Test", "value");

        assertEquals("value", attrs.getIgnoreCase("test"));
        assertEquals("", attrs.getIgnoreCase("NONEExistent"));
    }

    @Test
    public void testPut() {
        Attributes attrs = new Attributes();
        attrs.put("test", "value");

        assertEquals("value", attrs.get("test"));

        attrs.put("test", "newvalue");
        assertEquals("newvalue", attrs.get("test"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attrs = new Attributes();
        attrs.put("test", true);
        assertTrue(attrs.hasKey("test"));
        assertFalse(attrs.hasKey("test2"));
        attrs.put("test", false);
        assertFalse(attrs.hasKey("test"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attrs = new Attributes();
        attrs.put(new Attribute("test", "value"));

        assertEquals("value", attrs.get("test"));

        attrs.put(new Attribute("test", "newvalue"));
        assertEquals("newvalue", attrs.get("test"));
    }

    @Test
    public void testRemove() {
        Attributes attrs = new Attributes();
        attrs.put("test", "value");
        attrs.remove("test");
        assertFalse(attrs.hasKey("test"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("Test", "value");
        attrs.removeIgnoreCase("test");
        assertFalse(attrs.hasKey("Test"));
    }

    @Test
    public void testHasKey() {
        Attributes attrs = new Attributes();
        attrs.put("test", "value");
        assertTrue(attrs.hasKey("test"));
        assertFalse(attrs.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("Test", "value");
        assertTrue(attrs.hasKeyIgnoreCase("test"));
        assertFalse(attrs.hasKeyIgnoreCase("NONEExistent"));
    }

    @Test
    public void testAddAll() {
        Attributes attrs1 = new Attributes();
        attrs1.put("test1", "value1");
        Attributes attrs2 = new Attributes();
        attrs2.put("test2", "value2");

        attrs1.addAll(attrs2);
        assertEquals(2, attrs1.size());
        assertTrue(attrs1.hasKey("test1"));
        assertTrue(attrs1.hasKey("test2"));
        assertEquals("value1", attrs1.get("test1"));
        assertEquals("value2", attrs1.get("test2"));
    }

    @Test
    public void testDataset() {
        Attributes attrs = new Attributes();
        attrs.put("data-test", "value");
        Map<String, String> dataset = attrs.dataset();
        assertEquals(1, dataset.size());
        assertEquals("value", dataset.get("test"));
    }

    @Test
    public void testHtml() {
        Attributes attrs = new Attributes();
        attrs.put("test", "value");

        StringBuilder sb = new StringBuilder();
        try {
            attrs.html(sb, new Document.OutputSettings());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        assertEquals(" test=\"value\"", sb.toString());
    }

    @Test
    public void testClone() {
        Attributes attrs = new Attributes();
        attrs.put("test", "value");

        Attributes cloned = attrs.clone();
        assertEquals(1, cloned.size());
        assertEquals("value", cloned.get("test"));
    }
}