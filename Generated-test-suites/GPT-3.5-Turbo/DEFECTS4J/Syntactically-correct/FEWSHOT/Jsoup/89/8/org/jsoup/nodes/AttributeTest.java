package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

public class AttributeTest {
    
    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        
        Attribute nullValueAttribute = new Attribute("key", null);
        assertNotNull(nullValueAttribute);
        assertEquals("key", nullValueAttribute.getKey());
        assertNull(nullValueAttribute.getValue());
        assertNull(nullValueAttribute.parent);
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("oldKey", "value");
        
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        
        attribute.setKey(null);
        assertNotNull(attribute.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "oldValue");
        
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("oldValue", oldValue);
        
        attribute.setValue(null);
        assertNull(attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        
        String html = attribute.html();
        assertNotNull(html);
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testHtmlWithEscapedValue() {
        Attribute attribute = new Attribute("key", "value&amp;");
        
        String html = attribute.html();
        assertNotNull(html);
        assertEquals("key=\"value&amp;\"", html);
    }
    
    @Test(expected = SerializationException.class)
    public void testHtmlWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        Appendable appendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException();
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException();
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException();
            }
        };
        
        attribute.html(appendable, new Document.OutputSettings());
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        
        String toString = attribute.toString();
        assertNotNull(toString);
        assertEquals("key=\"value\"", toString);
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value&amp;");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value&", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        boolean isDataAttribute = Attribute.isDataAttribute("data-key");
        assertEquals(true, isDataAttribute);
        
        boolean isNotDataAttribute = Attribute.isDataAttribute("key");
        assertEquals(false, isNotDataAttribute);
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        boolean shouldCollapse = Attribute.shouldCollapseAttribute("key", "key", new Document.OutputSettings());
        assertEquals(true, shouldCollapse);
        
        shouldCollapse = Attribute.shouldCollapseAttribute("key", "", new Document.OutputSettings());
        assertEquals(true, shouldCollapse);
        
        shouldCollapse = Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings());
        assertEquals(false, shouldCollapse);
    }
    
    @Test
    public void testIsBooleanAttribute() {
        boolean isBooleanAttribute = Attribute.isBooleanAttribute("checked");
        assertEquals(true, isBooleanAttribute);
        
        boolean isNotBooleanAttribute = Attribute.isBooleanAttribute("key");
        assertEquals(false, isNotBooleanAttribute);
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        
        assertEquals(attribute1, attribute2);
        
        Attribute attribute3 = new Attribute("key", "value1");
        assertNotEquals(attribute1, attribute3);
        
        Attribute attribute4 = new Attribute("key", null);
        assertNotEquals(attribute1, attribute4);
        
        Attribute attribute5 = new Attribute(null, "value");
        assertNotEquals(attribute1, attribute5);
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
        
        assertNotNull(clone);
        assertEquals(attribute, clone);
        assertNotEquals(attribute, new Object());
    }
}