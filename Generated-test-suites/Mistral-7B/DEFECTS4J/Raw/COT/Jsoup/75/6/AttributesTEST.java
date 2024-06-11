package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * JUnit test file for Attributes class.
 */
public class AttributesTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Attributes attributes = new Attributes();
        assertNull(attributes.get("test"));
        assertEquals(0, attributes.size());
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals("value", attributes.get("test"));
        assertEquals(EmptyString, attributes.get("unknown"));
    }

    /**
     * Test getIgnoreCase method.
     */
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        assertEquals("value", attributes.getIgnoreCase("test"));
        assertEquals(EmptyString, attributes.getIgnoreCase("Unknown"));
    }

    /**
     * Test put method.
     */
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");
        attributes.put("test", "value2");
        assertEquals("value2", attributes.get("test"));
    }

    /**
     * Test putIgnoreCase method.
     */
    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("TEST", "value1");
        attributes.putIgnoreCase("test", "value2");
        assertEquals("value2", attributes.get("test"));
    }

    /**
     * Test put method with boolean attribute.
     */
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        assertFalse(attributes.get("test").equals("test"));
        assertNull(attributes.get("test"));
        assertFalse(attributes.hasKey("test"));
        assertTrue(attributes.hasKeyIgnoreCase("test"));
    }

    /**
     * Test put method with null value.
     */
    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("test", null);
        assertEquals(EmptyString, attributes.get("test"));
        assertTrue(attributes.hasKey("test"));
        assertTrue(attributes.hasKeyIgnoreCase("test"));
    }

    /**
     * Test indexOfKey method.
     */
    @Test
    public void testIndexOfKey() {
        Attributes attributes = new Attributes();
        int index = attributes.indexOfKey("test");
        assertEquals(NotFound, index);

        attributes.put("test", "value");
        index = attributes.indexOfKey("test");
        assertEquals(0, index);
    }

    /**
     * Test indexOfKeyIgnoreCase method.
     */
    @Test
    public void testIndexOfKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        int index = attributes.indexOfKeyIgnoreCase("Test");
        assertEquals(NotFound, index);

        attributes.put("Test", "value");
        index = attributes.indexOfKeyIgnoreCase("test");
        assertEquals(0, index);
    }

    /**
     * Test hasKey method.
     */
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("test"));

        attributes.put("test", "value");
        assertTrue(attributes.hasKey("test"));

        attributes.remove("test");
        assertFalse(attributes.hasKey("test"));
    }

    /**
     * Test hasKeyIgnoreCase method.
     */
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("test"));

        attributes.put("Test", "value");
        assertTrue(attributes.hasKeyIgnoreCase("test"));

        attributes.remove("Test");
        assertFalse(attributes.hasKeyIgnoreCase("test"));
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        assertEquals(2, attributes.size());

        attributes.remove("test1");
        assertEquals(1, attributes.size());
    }

    /**
     * Test addAll method.
     */
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("test1", "value1");
        attributes2.put("test2", "value2");

        Attributes merged = new Attributes();
        merged.addAll(attributes1);
        merged.addAll(attributes2);

        assertTrue(merged.hasKey("test1"));
        assertTrue(merged.hasKey("test2"));
        assertEquals("value1", merged.get("test1"));
        assertEquals("value2", merged.get("test2"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        List<Map.Entry<String, String>> list = attributes.asList();
        Iterator<Map.Entry<String, String>> iterator = list.iterator();

        assertTrue(iterator.hasNext());
        Map.Entry<String, String> entry1 = iterator.next();
        assertEquals("test1", entry1.getKey());
        assertEquals("value1", entry1.getValue());

        assertTrue(iterator.hasNext());
        Map.Entry<String, String> entry2 = iterator.next();
        assertEquals("test2", entry2.getKey());
        assertEquals("value2", entry2.getValue());

        assertFalse(iterator.hasNext());
    }

    /**
     * Test asList method.
     */
    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        List<Attribute> list = attributes.asList();
        Attribute attribute1 = list.get(0);
        Attribute attribute2 = list.get(1);

        assertEquals("test1", attribute1.getKey());
        assertEquals("value1", attribute1.getValue());

        assertEquals("test2", attribute2.getKey());
        assertEquals("value2", attribute2.getValue());
    }

    /**
     * Test dataset method.
     */
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals("value1", dataset.get("data-test1"));
        assertEquals("value2", dataset.get("data-test2"));
    }

    /**
     * Test html method.
     */
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        StringBuilder html = new StringBuilder();
        attributes.html(html, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

        assertEquals("<html>", html.toString());

        attributes.put("test3", "value3");
        attributes.html(html, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

        String expected = "<html " +
                "test1=\"value1\"" +
                "test2=\"value2\"" +
                "test3=\"value3\"";
        assertEquals(expected, html.toString());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("test1", "value1");
        attributes2.put("test1", "value1");
        attributes3.put("test2", "value2");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(""));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("test1", "value1");
        attributes2.put("test1", "value1");
        attributes3.put("test2", "value2");

        int hashcode1 = attributes1.hashCode();
        int hashcode2 = attributes2.hashCode();
        int hashcode3 = attributes3.hashCode();

        assertEquals(hashcode1, attributes1.hashCode());
        assertEquals(hashcode1, attributes2.hashCode());
        assertNotEquals(hashcode1, hashcode3);
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        Attributes cloned = attributes.clone();

        assertTrue(attributes.equals(cloned));
        assertTrue(cloned.equals(cloned));
    }
}