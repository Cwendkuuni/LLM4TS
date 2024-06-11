package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get("key");

        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.getIgnoreCase("KEY");

        assertEquals("value", result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get("key");

        assertEquals("value", result);
    }

    @Test
    public void testPutBooleanTrue() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);

        boolean result = attributes.hasKeyIgnoreCase("checked");

        assertTrue(result);
    }

    @Test
    public void testPutBooleanFalse() {
        Attributes attributes = new Attributes();
        attributes.put("checked", false);

        boolean result = attributes.hasKeyIgnoreCase("checked");

        assertFalse(result);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");

        boolean result = attributes.hasKey("key");

        assertFalse(result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.removeIgnoreCase("KEY");

        boolean result = attributes.hasKey("key");

        assertFalse(result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean result = attributes.hasKey("key");

        assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean result = attributes.hasKeyIgnoreCase("KEY");

        assertTrue(result);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int result = attributes.size();

        assertEquals(2, result);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        attributes1.addAll(attributes2);

        assertTrue(attributes1.hasKey("key2"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");

        String result = attributes.dataset().get("key");

        assertEquals("value", result);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.html();

        assertEquals(" key=\"value\"", result);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key", "value");
        attributes1.put("key2", "value2");
        Attributes attributes2 = new Attributes();
        attributes2.put("key", "value");
        attributes2.put("key2", "value2");

        boolean result = attributes1.equals(attributes2);

        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        int result = attributes.hashCode();

        assertEquals(34, result);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Attributes clone = attributes.clone();

        assertTrue(clone.hasKey("key"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        attributes.normalize();

        String result = attributes.get("key");

        assertEquals("value", result);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int result = attributes.asList().size();

        assertEquals(2, result);
    }
}