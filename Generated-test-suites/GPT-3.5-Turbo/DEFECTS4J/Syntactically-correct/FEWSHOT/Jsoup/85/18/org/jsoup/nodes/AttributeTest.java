package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

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
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
    }
    
    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assertNotNull(html);
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.toString();
        assertNotNull(html);
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertNotNull(attribute);
        assertEquals("unencodedKey", attribute.getKey());
        assertEquals("encodedValue", attribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        assertFalse(Attribute.isDataAttribute("key"));
        assertTrue(Attribute.isDataAttribute("data-key"));
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertFalse(Attribute.isBooleanAttribute("key"));
        assertTrue(Attribute.isBooleanAttribute("autofocus"));
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "newValue");
        
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
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
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}