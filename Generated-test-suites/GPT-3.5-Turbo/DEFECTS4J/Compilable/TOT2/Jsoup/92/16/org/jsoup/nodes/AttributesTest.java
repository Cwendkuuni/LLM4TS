package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKeyIgnoreCase("booleanKey"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key4", "value4");
        attributes.remove("key4");
        assertFalse(attributes.hasKey("key4"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key5", "value5");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key6", "value6");
        Attributes attributes2 = new Attributes();
        attributes2.addAll(attributes1);
        assertTrue(attributes2.hasKey("key6"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");
        assertEquals("data-value", attributes.dataset().get("key"));
    }

}