package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertEquals("", attributes.get("key4"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("value3", attributes.getIgnoreCase("key3"));
        assertEquals("", attributes.getIgnoreCase("key4"));
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "newvalue1");
        assertEquals("newvalue1", attributes.get("key1"));
        attributes.put("key4", "value4");
        assertEquals("value4", attributes.get("key4"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertEquals("", attributes.get("key1"));
        attributes.put("key2", false);
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        attributes.removeIgnoreCase("KEY2");
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key4"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertFalse(attributes.hasKeyIgnoreCase("key4"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY5"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
        attributes.remove("key1");
        assertEquals(2, attributes.size());
    }
}