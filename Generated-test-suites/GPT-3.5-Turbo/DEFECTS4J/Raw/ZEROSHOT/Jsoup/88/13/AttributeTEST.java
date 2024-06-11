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
    }

    @Test
    public void testSetterMethods() {
        Attribute attribute = new Attribute("key", "value");
        
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.toString());
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "key");
        assertFalse(attribute.shouldCollapseAttribute(out));
        
        attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(out));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("allowfullscreen", "true");
        assertTrue(attribute.isBooleanAttribute());
        
        attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
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
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        assertEquals(attribute1, attribute2);
    }
}