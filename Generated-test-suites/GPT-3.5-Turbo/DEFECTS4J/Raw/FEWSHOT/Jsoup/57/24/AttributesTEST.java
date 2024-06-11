package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        assertEquals("", attributes.get("key"));
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        attributes.put("Key", "Value");
        assertEquals("", attributes.get("Key"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("", attributes.getIgnoreCase("key"));
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
        attributes.put("key", "new value");
        assertEquals("new value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
        attribute.setValue("new value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        attributes.removeIgnoreCase("Key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
        attributes.remove("key1");
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
        assertFalse(attributes.iterator().hasNext());
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertNotNull(attributes.iterator().next());
    }

    @Test
    public void testAsList() {
        assertEquals(0, attributes.asList().size());
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        assertTrue(attributes.dataset().isEmpty());
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        assertEquals(2, attributes.dataset().size());
        assertEquals("value1", attributes.dataset().get("key1"));
        assertEquals("value2", attributes.dataset().get("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("class", "test");
        attributes.put("id", "testId");
        assertEquals(" class=\"test\" id=\"testId\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        assertTrue(attributes1.equals(attributes2));

        attributes1.put("key", "value");
        assertFalse(attributes1.equals(attributes2));

        attributes2.put("key", "value");
        assertTrue(attributes1.equals(attributes2));

        attributes1.put("Class", "test");
        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        assertEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes1.put("key", "value");
        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes2.put("key", "value");
        assertEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes1.put("Class", "test");
        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes clone = attributes.clone();
        assertTrue(attributes.equals(clone));
        assertNotSame(attributes, clone);
    }

}