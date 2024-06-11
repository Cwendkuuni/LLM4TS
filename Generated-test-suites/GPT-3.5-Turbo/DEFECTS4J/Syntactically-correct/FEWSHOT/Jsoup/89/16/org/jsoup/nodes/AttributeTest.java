package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeConstructor() {
        String key = "class";
        String value = "example";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertSame(parent, attribute.parent);
    }
    
    @Test
    public void testAttributeSetKey() {
        String key = "class";
        String newKey = "id";
        String value = "example";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        attribute.setKey(newKey);
        
        assertEquals(newKey, attribute.getKey());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAttributeSetKeyNull() {
        String key = "class";
        String value = "example";
        Attribute attribute = new Attribute(key, value);
        attribute.setKey(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAttributeSetKeyEmpty() {
        String key = "class";
        String value = "example";
        Attribute attribute = new Attribute(key, value);
        attribute.setKey("");
    }
    
    @Test
    public void testAttributeSetValue() {
        String key = "class";
        String value = "example";
        String newValue = "new example";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        String oldVal = attribute.setValue(newValue);
        
        assertEquals(newValue, attribute.getValue());
        assertEquals(value, oldVal);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAttributeSetValueNull() {
        String key = "class";
        String value = "example";
        Attribute attribute = new Attribute(key, value);
        attribute.setValue(null);
    }
    
    @Test
    public void testAttributeHtml() {
        String key = "href";
        String value = "index.html";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        String html = attribute.html();
        assertEquals(key + "=\"" + value + "\"", html);
    }
    
    @Test
    public void testAttributeToString() {
        String key = "href";
        String value = "index.html";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        String toString = attribute.toString();
        assertEquals(key + "=\"" + value + "\"", toString);
    }
    
    @Test
    public void testAttributeCreateFromEncoded() {
        String unencodedKey = "href";
        String encodedValue = "index.html";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        
        assertEquals(unencodedKey, attribute.getKey());
        assertEquals(encodedValue, attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testAttributeIsDataAttribute() {
        String key = "data-name";
        String value = "example";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        assertTrue(attribute.isDataAttribute());
    }
    
    @Test
    public void testAttributeIsDataAttributeFalse() {
        String key = "name";
        String value = "example";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        assertFalse(attribute.isDataAttribute());
    }
    
    @Test
    public void testAttributeShouldCollapseAttribute() {
        String key = "class";
        String value = "";
        Document.OutputSettings out = new Document.OutputSettings();
        boolean shouldCollapse = Attribute.shouldCollapseAttribute(key, value, out);
        
        assertTrue(shouldCollapse);
    }
    
    @Test
    public void testAttributeShouldCollapseAttributeFalse() {
        String key = "class";
        String value = "example";
        Document.OutputSettings out = new Document.OutputSettings();
        boolean shouldCollapse = Attribute.shouldCollapseAttribute(key, value, out);
        
        assertFalse(shouldCollapse);
    }
    
    @Test
    public void testAttributeShouldCollapseAttributeSyntaxXhtml() {
        String key = "class";
        String value = "";
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xhtml);
        boolean shouldCollapse = Attribute.shouldCollapseAttribute(key, value, out);
        
        assertFalse(shouldCollapse);
    }
    
    @Test
    public void testAttributeIsBooleanAttribute() {
        String key = "checked";
        String value = "";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        assertTrue(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testAttributeIsBooleanAttributeFalse() {
        String key = "class";
        String value = "example";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        
        assertFalse(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testAttributeEquals() {
        String key = "class";
        String value = "example";
        Attributes parent = new Attributes();
        Attribute attribute1 = new Attribute(key, value, parent);
        Attribute attribute2 = new Attribute(key, value, parent);
        
        assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testAttributeEqualsFalse() {
        String key = "class";
        String value = "example";
        String newValue = "new example";
        Attributes parent = new Attributes();
        Attribute attribute1 = new Attribute(key, value, parent);
        Attribute attribute2 = new Attribute(key, newValue, parent);
        
        assertFalse(attribute1.equals(attribute2));
    }
    
    @Test
    public void testAttributeHashCode() {
        String key = "class";
        String value = "example";
        Attributes parent = new Attributes();
        Attribute attribute1 = new Attribute(key, value, parent);
        Attribute attribute2 = new Attribute(key, value, parent);
        
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }
    
    @Test
    public void testAttributeClone() {
        String key = "class";
        String value = "example";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);
        Attribute clone = attribute.clone();
        
        assertNotSame(attribute, clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
        assertSame(attribute.parent, clone.parent);
    }
}