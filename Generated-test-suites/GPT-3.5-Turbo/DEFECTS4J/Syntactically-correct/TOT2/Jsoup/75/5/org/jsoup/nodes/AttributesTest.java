package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

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
        assertEquals("value2", attributes.getIgnoreCase("kEy2"));
    }
    
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
    }
    
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key4", true);
        assertTrue(attributes.hasKeyIgnoreCase("kEy4"));
    }
    
    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key5", "value5");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key5"));
    }
    
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key6", "value6");
        attributes.remove("key6");
        assertFalse(attributes.hasKey("key6"));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key7", "value7");
        attributes.removeIgnoreCase("kEy7");
        assertFalse(attributes.hasKey("Key7"));
    }
    
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key8", "value8");
        assertEquals(1, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes attributes = new Attributes();
        Attributes incoming = new Attributes();
        incoming.put("key9", "value9");
        incoming.put("key10", "value10");
        
        attributes.addAll(incoming);
        
        assertTrue(attributes.hasKey("key9"));
        assertTrue(attributes.hasKey("key10"));
    }

}