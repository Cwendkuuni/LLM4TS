package org.jsoup.nodes;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key3")); // Test for key which is not set
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("", attributes.getIgnoreCase("KEY3")); // Test for key which is not set
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        attributes.put("key2", false);

        assertEquals("", attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY2"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");

        attributes.put(attribute1);
        attributes.put(attribute2);

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        attributes.removeIgnoreCase("KEY2");
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY2"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");

        attributes.addAll(incoming);

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

}