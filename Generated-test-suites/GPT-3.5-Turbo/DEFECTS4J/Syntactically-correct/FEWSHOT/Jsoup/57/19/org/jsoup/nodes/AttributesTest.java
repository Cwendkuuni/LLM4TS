package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    
    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        assertEquals("", attributes.get("key"));

        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        attributes.put("key", "new value");
        assertEquals("new value", attributes.get("key"));

        attributes.put("key", "");
        assertEquals("", attributes.get("key"));

        assertEquals("", attributes.get("nonexistent key"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("", attributes.getIgnoreCase("key"));

        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));

        attributes.put("Key", "new value");
        assertEquals("new value", attributes.getIgnoreCase("key"));

        attributes.put("KEY", "");
        assertEquals("", attributes.getIgnoreCase("key"));

        assertEquals("", attributes.getIgnoreCase("nonexistent key"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        attributes.put("key", "new value");
        assertEquals("new value", attributes.get("key"));

        attributes.put("key", "");
        assertEquals("", attributes.get("key"));

        attributes.put("nullKey", null);
        assertNull(attributes.get("nullKey"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        assertEquals("true", attributes.get("key"));

        attributes.put("key", false);
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals(attribute.getValue(), attributes.get(attribute.getKey()));
    }

    @Test
    public void testRemove() {
        attributes.remove("key");
        assertEquals(0, attributes.size());

        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("key");
        assertEquals(0, attributes.size());

        attributes.put("key", "value");
        attributes.removeIgnoreCase("Key");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        assertFalse(attributes.hasKey("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("Key"));

        attributes.put("Key", "new value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKey("Key"));

        attributes.put("KEY", "");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKey("Key"));

        assertFalse(attributes.hasKey("nonexistent key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));

        attributes.put("Key", "new value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));

        attributes.put("KEY", "");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));

        assertFalse(attributes.hasKeyIgnoreCase("nonexistent key"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        attributes.addAll(incoming);
        assertEquals(0, attributes.size());

        incoming.put("key1", "value1");
        attributes.addAll(incoming);
        assertEquals(incoming.size(), attributes.size());

        incoming.put("key2", "value2");
        attributes.addAll(incoming);
        assertEquals(incoming.size(), attributes.size());
    }

    @Test
    public void testIterator() {
        assertFalse(attributes.iterator().hasNext());

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
        assertEquals(0, attributes.asList().size());

        attributes.put("key1", "value1");
        assertEquals(1, attributes.asList().size());

        attributes.put("key2", "value2");
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));

        attributes.removeIgnoreCase("data-key2");
        assertNull(dataset.get("key2"));

        dataset.put("key3", "value3");
        assertTrue(attributes.hasKey("data-key3"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        assertTrue(attributes1.equals(attributes2));

        attributes1.put("key1", "value1");
        assertFalse(attributes1.equals(attributes2));

        attributes2.put("key1", "value1");
        assertTrue(attributes1.equals(attributes2));

        attributes2.put("key1", "new value");
        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        assertEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes1.put("key1", "value1");
        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes2.put("key1", "value1");
        assertEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes2.put("key1", "new value");
        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();
        assertNotEquals(attributes, clone);
        assertEquals(attributes.get("key1"), clone.get("key1"));
        assertEquals(attributes.get("key2"), clone.get("key2"));
    }

}