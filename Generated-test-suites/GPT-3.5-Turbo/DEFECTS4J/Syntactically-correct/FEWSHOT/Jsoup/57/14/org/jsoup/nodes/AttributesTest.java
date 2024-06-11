package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value3", attributes.get("key3"));
        assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("value3", attributes.getIgnoreCase("key3"));
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("value3", attributes.getIgnoreCase("KEY3"));
    }

    @Test
    public void testPut() {
        attributes.put("key4", "value4");
        assertEquals("value4", attributes.get("key4"));

        attributes.put("key4", "updatedValue4");
        assertEquals("updatedValue4", attributes.get("key4"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key5", true);
        assertEquals("true", attributes.get("key5"));

        attributes.put("key5", false);
        assertEquals("", attributes.get("key5"));

        attributes.put("key6", false);
        assertFalse(attributes.hasKey("key6"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key4", "value4");
        attributes.put(attribute);
        assertEquals("value4", attributes.get("key4"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));

        attributes.remove("nonexistent");
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));

        attributes.removeIgnoreCase("nonexistent");
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKey("key3"));
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY3"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());

        attributes.put("key4", "value4");
        assertEquals(4, attributes.size());

        attributes.remove("key2");
        assertEquals(3, attributes.size());

        attributes.remove("nonexistent");
        assertEquals(3, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key4", "value4");
        incoming.put("key5", "value5");

        attributes.addAll(incoming);
        assertTrue(attributes.hasKey("key4"));
        assertTrue(attributes.hasKey("key5"));
        assertEquals("value4", attributes.get("key4"));
        assertEquals("value5", attributes.get("key5"));

        attributes.addAll(new Attributes());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testAsList() {
        assertEquals(3, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertTrue(dataset.isEmpty());

        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        String html = attributes.html();
        assertTrue(html.startsWith(" key1=\"value1\" key2=\"value2\" key3=\"value3\""));
    }

    @Test
    public void testEquals() {
        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");
        other.put("key3", "value3");

        assertTrue(attributes.equals(other));

        other.remove("key1");
        assertFalse(attributes.equals(other));

        assertFalse(attributes.equals(null));
        assertFalse(attributes.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");
        other.put("key3", "value3");

        assertEquals(attributes.hashCode(), other.hashCode());

        other.remove("key1");
        assertNotEquals(attributes.hashCode(), other.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        assertTrue(attributes.equals(clone));
    }

    @Test
    public void testDataKey() {
        assertEquals("data-key", attributes.dataKey("key"));
    }
}