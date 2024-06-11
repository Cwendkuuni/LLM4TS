package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {
    
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");
        
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }
    
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        
        assertEquals("value", attributes.get("key"));
    }
    
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }
    
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        
        attributes.remove("key");
        
        assertFalse(attributes.hasKey("key"));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        
        attributes.removeIgnoreCase("key");
        
        assertFalse(attributes.hasKey("key"));
    }
    
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("otherKey"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("otherKey"));
    }
    
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        
        attributes1.addAll(attributes2);
        
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
    }
    
    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        
        assertEquals(attributes.size(), count);
    }
    
    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals(attributes.size(), attributes.asList().size());
    }
    
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        
        assertEquals(attributes.size(), attributes.dataset().size());
        assertTrue(attributes.dataset().containsKey("key1"));
        assertTrue(attributes.dataset().containsKey("key2"));
        assertFalse(attributes.dataset().containsKey("data-key1"));
    }
    
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        String expected = " key1=\"value1\" key2=\"value2\"";
        
        assertEquals(expected, attributes.html());
    }
    
    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        
        assertEquals(attributes1, attributes2);
    }
    
    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }
    
    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        
        attributes.normalize();
        
        assertTrue(attributes.hasKey("key"));
    }
}
