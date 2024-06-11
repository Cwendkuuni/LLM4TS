package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributeTest {
    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testAttributeSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newkey");
        
        assertEquals("newkey", attribute.getKey());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testAttributeValue() {
        Attribute attribute = new Attribute("key", "value");
        
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testAttributeSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newvalue");
        
        assertEquals("newvalue", attribute.getValue());
        assertEquals("value", oldValue);
    }
    
    @Test
    public void testAttributeHtml() {
        Attribute attribute = new Attribute("key", "value");
        
        assertEquals("key=\"value\"", attribute.html());
    }
    
    @Test
    public void testAttributeFromString() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-key"));
        assertFalse(Attribute.isDataAttribute("key"));
    }
    
    @Test
    public void testShouldCollapseAttributeEmpty() {
        Document.OutputSettings out = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html);
        
        assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
    }
    
    @Test
    public void testShouldCollapseAttributeSameAsKey() {
        Document.OutputSettings out = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html);
        
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
    }
    
    @Test
    public void testShouldCollapseAttributeBoolean() {
        Document.OutputSettings out = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html);
        
        assertTrue(Attribute.shouldCollapseAttribute("readonly", null, out));
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "different");
        
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        
        assertTrue(attribute1.hashCode() == attribute2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        
        assertTrue(attribute1.equals(attribute2));
    }
}