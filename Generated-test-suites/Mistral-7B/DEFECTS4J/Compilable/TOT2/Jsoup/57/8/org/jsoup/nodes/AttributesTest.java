package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testPutAndRemoveBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        assertEquals(2, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllNull() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }
}