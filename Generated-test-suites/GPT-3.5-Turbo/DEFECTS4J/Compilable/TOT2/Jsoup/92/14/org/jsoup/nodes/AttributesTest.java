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
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.put("key1", "newValue1");
        assertEquals("newValue1", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        attributes.put("key1", false);
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key3", "value3");
        newAttributes.put("key4", "value4");

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.addAll(newAttributes);

        assertTrue(attributes.hasKey("key3"));
        assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testEquals() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        newAttributes.put("key2", "value2");

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.equals(newAttributes));
    }

    @Test
    public void testHashcode() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        newAttributes.put("key2", "value2");

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(attributes.hashCode(), newAttributes.hashCode());
    }
}