package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("id", "test");
        attributes.put("class", "test-class");
        attributes.put("data-test", "test-value");
        attributes.put("checked", true);
        attributes.put("disabled", false);
    }

    @Test
    public void testGetAttribute() {
        assertEquals("test", attributes.get("id"));
        assertEquals("test-class", attributes.get("class"));
        assertEquals("test-value", attributes.get("data-test"));
        assertEquals("", attributes.get("nonexistent"));
        assertEquals(null, attributes.get("checked"));
    }

    @Test
    public void testGetAttributeIgnoreCase() {
        assertEquals("test", attributes.getIgnoreCase("id"));
        assertEquals("test-class", attributes.getIgnoreCase("class"));
        assertEquals("test-value", attributes.getIgnoreCase("data-test"));
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
        assertEquals(null, attributes.getIgnoreCase("CHECKED"));
    }

    @Test
    public void testPutAttribute() {
        attributes.put("new-key", "new-value");
        assertEquals("new-value", attributes.get("new-key"));
        assertEquals(5, attributes.size());
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        attributes.put(null, "new-value");
    }

    @Test
    public void testPutDuplicateKey() {
        attributes.put("duplicate-key", "old-value");
        attributes.put("duplicate-key", "new-value");
        assertEquals("new-value", attributes.get("duplicate-key"));
        assertEquals(5, attributes.size());
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("boolean-key", false);
        attributes.put("boolean-key", true);
        assertFalse(attributes.getBoolean("boolean-key"));
        assertTrue(attributes.hasKey("boolean-key"));
    }

    @Test
    public void testPutNullBooleanAttribute() {
        attributes.put("boolean-key", null);
        assertFalse(attributes.getBoolean("boolean-key"));
        assertFalse(attributes.hasKey("boolean-key"));
    }

    @Test
    public void testPutAttributeWithSameKeyButDifferentCasing() {
        attributes.put("key-1", "value-1");
        attributes.put("Key-2", "value-2");
        assertEquals("value-1", attributes.get("key-1"));
        assertEquals("value-2", attributes.get("Key-2"));
        assertEquals(6, attributes.size());
    }

    @Test
    public void testRemoveAttribute() {
        attributes.put("key-to-remove", "value-to-remove");
        assertEquals("value-to-remove", attributes.get("key-to-remove"));
        assertTrue(attributes.hasKey("key-to-remove"));
        attributes.remove("key-to-remove");
        assertFalse(attributes.hasKey("key-to-remove"));
        assertNull(attributes.get("key-to-remove"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveInvalidIndex() {
        attributes.remove(100);
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("id"));
        assertTrue(attributes.hasKey("class"));
        assertTrue(attributes.hasKey("data-test"));
        assertTrue(attributes.hasKey("checked"));
        assertTrue(attributes.hasKey("disabled"));
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("id"));
        assertTrue(attributes.hasKeyIgnoreCase("class"));
        assertTrue(attributes.hasKeyIgnoreCase("data-test"));
        assertTrue(attributes.hasKeyIgnoreCase("checked"));
        assertTrue(attributes.hasKeyIgnoreCase("disabled"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize() {
        assertEquals(5, attributes.size());
    }

    @Test
    public void testIsEmpty() {
        Attributes emptyAttributes = new Attributes();
        assertEquals(0, emptyAttributes.size());
        assertTrue(emptyAttributes.isEmpty());
    }

    @Test
    public void testAddAll() {
        Attributes targetAttributes = new Attributes();
        Attributes sourceAttributes = new Attributes();
        sourceAttributes.put("id-1", "value-1");
        sourceAttributes.put("id-2", "value-2");

        targetAttributes.addAll(sourceAttributes);

        assertEquals(2, targetAttributes.size());
        assertEquals("value-1", targetAttributes.get("id-1"));
        assertEquals("value-2", targetAttributes.get("id-2"));
    }

    @Test
    public void testIterator() {
        List<Map.Entry<String, String>> expectedList = Arrays.asList(
                new AbstractMap.SimpleEntry<>("id", "test"),
                new AbstractMap.SimpleEntry<>("class", "test-class"),
                new AbstractMap.SimpleEntry<>("data-test", "test-value"),
                new AbstractMap.SimpleEntry<>("checked", "true"),
                new AbstractMap.SimpleEntry<>("disabled", "false")
        );

        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes.asList().iterator()) {
            actualList.add(entry);
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals("test-value", dataset.get("data-test"));
    }

    @Test
    public void testHtml() {
        StringBuilder expectedHtmlSB = new StringBuilder();
        expectedHtmlSB.append(" id=\"test\" class=\"test-class\" data-test=\"test-value\"");
        if (attributes.containsKey("checked")) {
            expectedHtmlSB.append(" checked");
        }

        StringBuilder actualHtmlSB = new StringBuilder();
        try {
            attributes.html(actualHtmlSB, new Document.OutputSettings());
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
        }

        assertEquals(expectedHtmlSB.toString(), actualHtmlSB.toString());
    }

    @Test
    public void testToString() {
        assertEquals("id=\"test\" class=\"test-class\" data-test=\"test-value\" checked checked:false disabled:false", attributes.toString());
    }
}