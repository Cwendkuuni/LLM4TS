package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertNull(attributes.get("test"));
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");
        assertEquals(1, attributes.size());
        assertEquals("value1", attributes.get("test"));
        assertTrue(attributes.hasKey("test"));
        assertNull(attributes.get("nonexistent"));
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");
        attributes.put("test", "value2");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("test"));
        assertTrue(attributes.hasKey("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("test", null);
    }

    @Test
    public void testAddBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", false);
        assertEquals(1, attributes.size());
        assertFalse(attributes.get("test").isEmpty());
        assertFalse(attributes.get("test"));
        assertTrue(attributes.hasKey("test"));

        attributes.put("test", true);
        assertEquals(1, attributes.size());
        assertFalse(attributes.get("test").isEmpty());
        assertTrue(attributes.get("test"));
    }

    @Test
    public void testAddAttributeWithSpecialCharacter() {
        Attributes attributes = new Attributes();
        attributes.put("test-data", "value");
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("test-data"));
        assertTrue(attributes.hasKey("test-data"));
    }

    @Test
    public void testAddMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        attributes.put("test3", "value3");

        assertEquals(3, attributes.size());
        assertEquals("value1", attributes.get("test1"));
        assertEquals("value2", attributes.get("test2"));
        assertEquals("value3", attributes.get("test3"));
        assertTrue(attributes.hasKey("test1"));
        assertTrue(attributes.hasKey("test2"));
        assertTrue(attributes.hasKey("test3"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals("value", attributes.getIgnoreCase("test"));
        assertEquals("value", attributes.getIgnoreCase("Test"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals(1, attributes.size());

        attributes.remove("test");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("test"));
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testRemoveNonExistentAttribute() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.remove("test"));
    }

    @Test
    public void testRemoveDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");
        attributes.put("test", "value2");
        assertEquals(1, attributes.size());

        attributes.remove("test");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("test"));
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testRemoveBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", false);
        assertEquals(1, attributes.size());

        attributes.remove("test");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("test"));
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testRemoveAllAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        attributes.put("test3", "value3");

        assertEquals(3, attributes.size());

        attributes.clear();
        assertEquals(0, attributes.size());
        assertNull(attributes.get("test1"));
        assertNull(attributes.get("test2"));
        assertNull(attributes.get("test3"));
        assertFalse(attributes.hasKey("test1"));
        assertFalse(attributes.hasKey("test2"));
        assertFalse(attributes.hasKey("test3"));
    }

    @Test
    public void testPutAll() {
        Attributes target = new Attributes();
        Attributes source = new Attributes();
        source.put("test1", "value1");
        source.put("test2", "value2");
        source.put("test3", "value3");

        target.putAll(source);

        assertEquals(3, target.size());
        assertEquals("value1", target.get("test1"));
        assertEquals("value2", target.get("test2"));
        assertEquals("value3", target.get("test3"));
        assertTrue(target.hasKey("test1"));
        assertTrue(target.hasKey("test2"));
        assertTrue(target.hasKey("test3"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutAllNull() {
        Attributes target = new Attributes();
        Attributes source = null;
        target.putAll(source);
    }

    @Test
    public void testAddAll() {
        Attributes target = new Attributes();
        Attributes source1 = new Attributes();
        Attributes source2 = new Attributes();

        source1.put("test1", "value1");
        source2.put("test2", "value2");
        source2.put("test3", "value3");

        target.addAll(source1);
        target.addAll(source2);

        assertEquals(3, target.size());
        assertEquals("value1", target.get("test1"));
        assertEquals("value2", target.get("test2"));
        assertEquals("value3", target.get("test3"));
        assertTrue(target.hasKey("test1"));
        assertTrue(target.hasKey("test2"));
        assertTrue(target.hasKey("test3"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllNull() {
        Attributes target = new Attributes();
        Attributes source = null;
        target.addAll(source);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertTrue(attributes.hasKeyIgnoreCase("test"));
        assertTrue(attributes.hasKeyIgnoreCase("TEST"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        attributes.put("test3", "value3");

        assertEquals(3, attributes.size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");
        attributes.put("data-test3", "value3");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(3, dataset.size());
        assertEquals("value1", dataset.get("data-test1"));
        assertEquals("value2", dataset.get("data-test2"));
        assertEquals("value3", dataset.get("data-test3"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" test=\"value\"");

        StringBuilder actualHtml = new StringBuilder();
        try {
            attributes.html(actualHtml, new Document.OutputSettings());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }
}