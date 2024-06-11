package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        assertEquals("", attributes.get("nonExistingKey"));
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("", attributes.getIgnoreCase("nonExistingKey"));
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testRemove() {
        attributes.put("keyToRemove", "value");
        assertTrue(attributes.hasKey("keyToRemove"));
        attributes.remove("keyToRemove");
        assertFalse(attributes.hasKey("keyToRemove"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("key1", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        assertEquals(attributes.size(), clone.size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "data-value");
        assertEquals("data-value", attributes.dataset().get("key"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        String expectedHtml = " key1=\"value1\"";
        assertEquals(expectedHtml, attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes newAttributes = new Attributes();
        assertEquals(attributes, newAttributes);
        attributes.put("key1", "value1");
        assertNotEquals(attributes, newAttributes);
    }

    @Test
    public void testHashCode() {
        Attributes newAttributes = new Attributes();
        assertEquals(attributes.hashCode(), newAttributes.hashCode());
        attributes.put("key1", "value1");
        assertNotEquals(attributes.hashCode(), newAttributes.hashCode());
    }

    @Test
    public void testNormalize() {
        attributes.put("Key", "value");
        attributes.normalize();
        assertFalse(attributes.hasKey("Key"));
        assertTrue(attributes.hasKey("key"));
    }

}