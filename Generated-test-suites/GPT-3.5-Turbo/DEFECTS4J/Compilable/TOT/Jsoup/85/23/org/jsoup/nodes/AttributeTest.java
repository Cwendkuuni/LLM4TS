package org.jsoup.nodes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
    
    private Attribute attribute;
    
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
        attribute.setValue("newValue");
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
    public void testClone() {
        Attribute clone = attribute.clone();
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("encodedKey", "encoded%20Value");
        assertEquals("encodedKey", encodedAttribute.getKey());
        assertEquals("encoded Value", encodedAttribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(equalAttribute));
        
        Attribute differentKeyAttribute = new Attribute("differentKey", "value");
        assertFalse(attribute.equals(differentKeyAttribute));
        
        Attribute differentValueAttribute = new Attribute("key", "differentValue");
        assertFalse(attribute.equals(differentValueAttribute));
    }
    
    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }
}