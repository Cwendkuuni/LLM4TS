package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class AttributeTest {
    @Test
    public void testConstructor() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }
    
    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attr = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attr.html());
    }
    
    @Test
    public void testToString() {
        Attribute attr = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attr.toString());
    }
    
    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        assertTrue(attr1.equals(attr2));
    }
    
    @Test
    public void testHashCode() {
        Attribute attr = new Attribute("key", "value");
        assertEquals(attr.hashCode(), attr.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clone = attr.clone();
        assertEquals(attr.getKey(), clone.getKey());
        assertEquals(attr.getValue(), clone.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(Attribute.shouldCollapseAttribute("checked", "checked", out));
        assertFalse(Attribute.shouldCollapseAttribute("checked", "unchecked", out));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("value"));
    }
}