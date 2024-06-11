package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AttributeTest {
    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        
        attribute = new Attribute("key", "value", null);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        
        attribute.parent = new Attributes();
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        
        attribute.parent.keys = new String[]{"key"};
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
        
        attribute.parent = new Attributes();
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        
        attribute.parent.vals = new String[]{"value"};
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
        
        attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
        
        attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.toString());
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
        Attribute attribute = new Attribute("data-key", "value");
        assertEquals(true, attribute.isDataAttribute());
        
        attribute = new Attribute("key", "value");
        assertEquals(false, attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "");
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        assertEquals(true, attribute.shouldCollapseAttribute(out));
        
        attribute = new Attribute("key", "value");
        assertEquals(false, attribute.shouldCollapseAttribute(out));
        
        out.syntax(Document.OutputSettings.Syntax.xml);
        assertEquals(false, attribute.shouldCollapseAttribute(out));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertEquals(true, attribute.isBooleanAttribute());
        
        attribute = new Attribute("key", "value");
        assertEquals(false, attribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(true, attribute1.equals(attribute2));
        
        attribute2 = new Attribute("newKey", "value");
        assertEquals(false, attribute1.equals(attribute2));
        
        attribute2 = new Attribute("key", "newValue");
        assertEquals(false, attribute1.equals(attribute2));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(true, attribute1.hashCode() == attribute2.hashCode());
        
        attribute2 = new Attribute("newKey", "value");
        assertEquals(false, attribute1.hashCode() == attribute2.hashCode());
        
        attribute2 = new Attribute("key", "newValue");
        assertEquals(false, attribute1.hashCode() == attribute2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertEquals(attribute.parent, clonedAttribute.parent);
    }
    
    @Test
    public void testIsBooleanAttributeWithKey() {
        String[] booleanAttributes = {
            "allowfullscreen", "async", "autofocus", "checked", "compact", "declare", "default", "defer", "disabled",
            "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple", "muted", "nohref", "noresize",
            "noshade", "novalidate", "nowrap", "open", "readonly", "required", "reversed", "seamless", "selected",
            "sortable", "truespeed", "typemustmatch"
        };
        
        Attribute attribute = new Attribute("key", "value");
        assertEquals(false, attribute.isBooleanAttribute());
        
        for (String booleanAttributeKey : booleanAttributes) {
            attribute = new Attribute(booleanAttributeKey, null);
            assertEquals(true, attribute.isBooleanAttribute());
        }
    }
}