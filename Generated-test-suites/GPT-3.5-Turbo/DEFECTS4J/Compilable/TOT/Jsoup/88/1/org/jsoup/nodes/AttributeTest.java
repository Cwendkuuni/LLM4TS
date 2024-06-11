package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class AttributeTest {
    
    Attribute attribute;
    
    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }
    
    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("encodedKey", "encoded%20Value");
        assertEquals("encodedKey", encodedAttribute.getKey());
        assertEquals("encoded Value", encodedAttribute.getValue());
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("someAttribute"));
    }
    
    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        Attribute differentAttribute = new Attribute("key", "differentValue");
        
        assertTrue(attribute.equals(equalAttribute));
        assertFalse(attribute.equals(differentAttribute));
    }
    
    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        Attribute differentAttribute = new Attribute("key", "differentValue");
        
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
        assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}