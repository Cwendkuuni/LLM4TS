package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
  
    @Test
    public void testConstructor() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);
    }
    
    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String oldValue = attr.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attr.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.html());
    }
    
    @Test
    public void testFromString() {
        String encodedValue = "encodedValue";
        Attribute attr = Attribute.createFromEncoded("key", encodedValue);
        assertEquals("key", attr.getKey());
        assertEquals("encodedValue", attr.getValue());
        assertNull(attr.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-key"));
        assertFalse(Attribute.isDataAttribute("key"));
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", new Document.OutputSettings()));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("allowfullscreen"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }
    
    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        Attribute attr3 = new Attribute("key1", "value");
        
        assertEquals(attr1, attr2);
        assertNotEquals(attr1, attr3);
    }
    
    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        Attribute attr3 = new Attribute("key1", "value");
        
        assertEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clone = attr.clone();
        
        assertEquals(attr, clone);
        assertNotSame(attr, clone);
    }
}