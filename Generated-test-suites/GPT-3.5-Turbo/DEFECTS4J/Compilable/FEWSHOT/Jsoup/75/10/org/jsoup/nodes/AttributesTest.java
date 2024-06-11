package org.jsoup.nodes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributesTest {
    
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key3"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("KEY2", "value2");

        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("key3"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        assertEquals("value1", attributes.get("key1"));
        
        attributes.put("key1", "value2");
        
        assertEquals("value2", attributes.get("key1"));
    }
    
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        
        assertTrue(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
        
        attributes.put("key2", false);
        
        assertEquals("", attributes.get("key2"));
        assertEquals(1, attributes.size());
    }
    
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        assertTrue(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertTrue(!attributes.hasKey("key1"));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        attributes.removeIgnoreCase("key1");
        assertTrue(!attributes.hasKeyIgnoreCase("key1"));
    }
    
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        assertTrue(attributes.hasKey("key1"));
        assertTrue(!attributes.hasKey("key2"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(!attributes.hasKeyIgnoreCase("key2"));
    }
    
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
        
        assertEquals(3, attributes.size());
        
        attributes.remove("key2");
        
        assertEquals(2, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key2", "value2");
        otherAttributes.put("key3", "value3");
        
        attributes.addAll(otherAttributes);
        
        assertEquals(3, attributes.size());
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value3", attributes.get("key3"));
    }
}