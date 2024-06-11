
package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {
    
    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testFromString() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("key", "value");
        boolean shouldCollapse1 = attribute1.shouldCollapseAttribute(new Document.OutputSettings());
        assertFalse(shouldCollapse1);
        
        Attribute attribute2 = new Attribute("key", "key");
        boolean shouldCollapse2 = attribute2.shouldCollapseAttribute(new Document.OutputSettings());
        assertTrue(shouldCollapse2);
        
        Attribute attribute3 = new Attribute("key", "");
        boolean shouldCollapse3 = attribute3.shouldCollapseAttribute(new Document.OutputSettings());
        assertTrue(shouldCollapse3);
    }
    
    @Test
    public void testEqualsAndHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("differentKey", "value");
        Attribute attribute4 = new Attribute("key", "differentValue");
        
        assertTrue(attribute1.equals(attribute2));
        assertTrue(attribute2.equals(attribute1));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));
        assertFalse(attribute2.equals(attribute3));
        assertFalse(attribute2.equals(attribute4));
        
        assertTrue(attribute1.hashCode() == attribute2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        
        assertEquals(attribute1.getKey(), attribute2.getKey());
        assertEquals(attribute1.getValue(), attribute2.getValue());
    }
}
