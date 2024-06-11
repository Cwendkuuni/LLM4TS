package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
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
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHTML() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&lt;value&gt;");
        assertEquals("key", attribute.getKey());
        assertEquals("<value>", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
        
        attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        
        Attribute attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(out));
        
        attribute = new Attribute("key", "key");
        assertTrue(attribute.shouldCollapseAttribute(out));
        
        attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(out));
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
        assertTrue(attribute1.equals(attribute2));
        
        attribute1 = new Attribute("key", "value");
        attribute2 = new Attribute("key", "newValue");
        assertFalse(attribute1.equals(attribute2));
        
        attribute1 = new Attribute("key1", "value1");
        attribute2 = new Attribute("key2", "value2");
        assertFalse(attribute1.equals(attribute2));
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
        Attribute clone = attribute.clone();
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
        assertNull(clone.parent);
    }
}