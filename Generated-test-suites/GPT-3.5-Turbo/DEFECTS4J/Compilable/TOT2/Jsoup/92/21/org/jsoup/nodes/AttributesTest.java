package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {
    
    private Attributes attributes;
    
    @Before
    public void setup() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
    }
    
    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key3"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
    }
    
    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
    }
    
    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.hasKey("booleanKey"));
    }
    
    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY2");
        assertFalse(attributes.hasKey("key2"));
    }
    
    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("newKey", "newValue");
        attributes.addAll(newAttributes);
        assertTrue(attributes.hasKey("newKey"));
    }
    
    @Test
    public void testDataset() {
        attributes.put("data-key", "data-value");
        assertEquals("data-value", attributes.dataset().get("key"));
    }
    
    @Test
    public void testHtml() {
        String expectedHtml = " key1=\"value1\" key2=\"value2\"";
        assertEquals(expectedHtml, attributes.html());
    }
    
    @Test
    public void testEquals() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        newAttributes.put("key2", "value2");
        assertEquals(attributes, newAttributes);
    }
    
    @Test
    public void testHashCode() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        newAttributes.put("key2", "value2");
        assertEquals(attributes.hashCode(), newAttributes.hashCode());
    }
    
    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
    }
    
}