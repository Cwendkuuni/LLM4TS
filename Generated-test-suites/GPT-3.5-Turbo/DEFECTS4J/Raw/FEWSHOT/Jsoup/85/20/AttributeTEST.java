package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
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
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
        assertNull(attribute.parent);
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String toString = attribute.toString();
        assertEquals("key=\"value\"", toString);
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute1 = new Attribute("data-key", "value");
        assertTrue(attribute1.isDataAttribute());
        Attribute attribute2 = new Attribute("key", "value");
        assertFalse(attribute2.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute1 = new Attribute("key", "");
        assertTrue(attribute1.shouldCollapseAttribute(outputSettings));
        Attribute attribute2 = new Attribute("key", "value");
        assertFalse(attribute2.shouldCollapseAttribute(outputSettings));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("allowfullscreen", "value");
        assertTrue(attribute1.isBooleanAttribute());
        Attribute attribute2 = new Attribute("key", "value");
        assertFalse(attribute2.isBooleanAttribute());
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
        int hashCode = attribute.hashCode();
        assertEquals(-305623912, hashCode);
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNull(clonedAttribute.parent);
    }
}