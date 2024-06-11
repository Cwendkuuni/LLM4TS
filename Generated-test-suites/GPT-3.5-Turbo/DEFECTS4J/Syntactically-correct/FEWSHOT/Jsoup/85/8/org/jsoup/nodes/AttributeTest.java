package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 A single key + value attribute. (Only used for presentation.)
 */
public class AttributeTest {
    
    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testAttributeGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testAttributeSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testAttributeGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testAttributeSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testAttributeToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }
    
    @Test
    public void testAttributeClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "amp;");
        assertEquals("key", attribute.getKey());
        assertEquals("&", attribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", "true");
        assertTrue(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute1 = new Attribute("key", "");
        assertTrue(attribute1.shouldCollapseAttribute(outputSettings));
        
        Attribute attribute2 = new Attribute("key", "key");
        assertTrue(attribute2.shouldCollapseAttribute(outputSettings));
        
        Attribute attribute3 = new Attribute("key", "value");
        assertFalse(attribute3.shouldCollapseAttribute(outputSettings));
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(-791057759, attribute.hashCode());
    }
}