package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        
        attribute = new Attribute("key", "value", new Attributes());
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNotNull(attribute.parent);
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        
        attribute.parent = new Attributes();
        attribute.setKey("newKey2");
        assertEquals("newKey2", attribute.getKey());
        assertEquals(0, attribute.parent.indexOfKey("newKey"));
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
        
        attribute.parent = new Attributes();
        oldValue = attribute.setValue("newValue2");
        assertEquals("newValue", oldValue);
        assertEquals("newValue2", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
        
        attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.html());
        
        attribute = new Attribute("data-id", "123");
        assertEquals("data-id=\"123\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
        
        attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.toString());
        
        attribute = new Attribute("data-id", "123");
        assertEquals("data-id=\"123\"", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-id", "123");
        assertTrue(attribute.isDataAttribute());
        
        attribute = new Attribute("id", "123");
        assertFalse(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "", new Document.OutputSettings());
        assertTrue(attribute.shouldCollapseAttribute());
        
        attribute = new Attribute("key", "value", new Document.OutputSettings());
        assertFalse(attribute.shouldCollapseAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1, attribute2);
        
        attribute1.parent = new Attributes();
        attribute2.parent = new Attributes();
        attribute1.parent.put("key", "value");
        attribute2.parent.put("key", "value");
        assertEquals(attribute1, attribute2);
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}