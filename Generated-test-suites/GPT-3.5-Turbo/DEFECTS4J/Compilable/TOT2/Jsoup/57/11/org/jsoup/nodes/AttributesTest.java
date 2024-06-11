package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        attributes.put("newKey", "newValue");
        assertEquals("newValue", attributes.get("newKey"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("boolKey", true);
        assertEquals("boolKey", "true", attributes.get("boolKey"));
        attributes.put("boolKey", false);
        assertFalse(attributes.hasKey("boolKey"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY3");
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("newKey1", "newValue1");
        newAttributes.put("newKey2", "newValue2");
        attributes.addAll(newAttributes);
        assertEquals(5, attributes.size());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Attribute attr : attributes) {
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
        assertEquals(0, attributes.dataset().size());
    }

    @Test
    public void testHtml() {
        assertEquals("", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        newAttributes.put("key2", "value2");
        newAttributes.put("key3", "value3");
        assertTrue(attributes.equals(newAttributes));
    }

    @Test
    public void testHashCode() {
        assertEquals(attributes.hashCode(), attributes.clone().hashCode());
    }
}