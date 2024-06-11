
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("nonexistent"));
        assertEquals("", attributes.get(""));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("kEy"));
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
        assertEquals("", attributes.getIgnoreCase(""));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertEquals("key", attributes.get("key"));

        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attr = new Attribute("key", "value");
        attributes.put(attr);
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key", "value");
        attributes.removeIgnoreCase("kEy");
        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("kEy"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");
        attributes.addAll(incoming);
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int count = 0;
        for (Attribute attr : attributes) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertTrue(dataset.containsKey("key1"));
        assertTrue(dataset.containsKey("key2"));
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
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
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertTrue(attributes.equals(clone));
        assertFalse(attributes == clone);
        assertFalse(attributes.asList() == clone.asList());
    }
}
