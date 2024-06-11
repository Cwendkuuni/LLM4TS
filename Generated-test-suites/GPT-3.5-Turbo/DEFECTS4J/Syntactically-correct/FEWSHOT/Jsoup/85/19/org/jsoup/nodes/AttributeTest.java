package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

public class AttributeTest {
    
    @Test
    public void testAttributeConstructor() {
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
        attribute.setKey("anotherKey");
        assertEquals("anotherKey", attribute.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        
        attribute.parent = new Attributes();
        attribute.setValue("anotherValue");
        assertEquals("anotherValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
        
        attribute.setValue("newVal");
        assertEquals("key=\"newVal\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
        
        attribute.setValue("newVal");
        assertEquals("key=\"newVal\"", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        assertEquals("key", attribute.getKey());
        assertEquals("\"value\"", attribute.getValue());
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
        Attribute attribute = new Attribute("key", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        
        attribute.setValue("key");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        
        attribute.setValue("");
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
        
        attribute = new Attribute("allowfullscreen", null);
        assertTrue(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
        
        Attribute attribute3 = new Attribute("key", "newValue");
        assertFalse(attribute1.equals(attribute3));
        
        Attribute attribute4 = new Attribute("newKey", "value");
        assertFalse(attribute1.equals(attribute4));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
        
        Attribute attribute3 = new Attribute("key", "newValue");
        assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
        
        Attribute attribute4 = new Attribute("newKey", "value");
        assertNotEquals(attribute1.hashCode(), attribute4.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        assertEquals(attribute1.getKey(), attribute2.getKey());
        assertEquals(attribute1.getValue(), attribute2.getValue());
        assertNull(attribute2.parent);
    }
}