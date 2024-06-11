package org.jsoup.nodes;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AttributeTest {
    
    private Attribute attribute;
    
    @Test
    public void testConstructor() {
        attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        
        attribute = new Attribute("", "");
        assertEquals("", attribute.getKey());
        assertEquals("", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testSetKey() {
        attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        
        attribute.setKey("");
        assertEquals("", attribute.getKey());
    }
    
    @Test
    public void testSetValue() {
        attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
        
        oldValue = attribute.setValue("");
        assertEquals("", attribute.getValue());
        assertEquals("newValue", oldValue);
    }
    
    @Test
    public void testHtml() throws IOException {
        attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
        
        attribute = new Attribute("key", "");
        assertEquals("key=\"\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
        
        attribute = new Attribute("key", "");
        assertEquals("key=\"\"", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
        
        attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
        
        attribute = new Attribute("key", "key");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        
        attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        
        attribute = new Attribute("key", null);
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
        
        attribute = new Attribute("key", "key");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
        
        attribute = new Attribute("key", "");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
        
        attribute = new Attribute("key", null);
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
        
        attribute = new Attribute("key", null);
        assertTrue(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        attribute = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute.equals(attribute2));
        
        attribute2 = new Attribute("key", "value2");
        assertFalse(attribute.equals(attribute2));
        
        attribute = new Attribute("", "");
        attribute2 = new Attribute("", "");
        assertTrue(attribute.equals(attribute2));
    }
    
    @Test
    public void testHashCode() {
        attribute = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), attribute2.hashCode());
        
        attribute2 = new Attribute("key", "value2");
        assertNotEquals(attribute.hashCode(), attribute2.hashCode());
        
        attribute = new Attribute("", "");
        attribute2 = new Attribute("", "");
        assertEquals(attribute.hashCode(), attribute2.hashCode());
    }
    
    @Test
    public void testClone() {
        attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNull(clonedAttribute.parent);
    }
}