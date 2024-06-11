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
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
    }
    
    @Test
    public void testSetAttributeKey() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key1", "value1");
        String oldValue = attribute.setValue("newValue");
        
        assertEquals("value1", oldValue);
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("key1=\"value1\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("key1=\"value1\"", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key1", "value1");
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute1 = new Attribute("data-key", "value1");
        Attribute attribute2 = new Attribute("key", "value2");
        
        assertTrue(attribute1.isDataAttribute());
        assertFalse(attribute2.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        
        Attribute attribute1 = new Attribute("key1", "key1");
        Attribute attribute2 = new Attribute("key2", "");
        Attribute attribute3 = new Attribute("key3", null);
        Attribute attribute4 = new Attribute("key4", "value4");
        
        assertTrue(attribute1.shouldCollapseAttribute(out));
        assertTrue(attribute2.shouldCollapseAttribute(out));
        assertTrue(attribute3.shouldCollapseAttribute(out));
        assertFalse(attribute4.shouldCollapseAttribute(out));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("allowfullscreen", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        
        assertTrue(attribute1.isBooleanAttribute());
        assertFalse(attribute2.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key1", "value2");
        
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = attribute1.clone();
        
        assertNotSame(attribute1, attribute2);
        assertEquals(attribute1.getKey(), attribute2.getKey());
        assertEquals(attribute1.getValue(), attribute2.getValue());
    }
}