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
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testHtml() {
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
        Attribute attribute = Attribute.createFromEncoded("key", "&#34;value&#34;");
        assertEquals("key", attribute.getKey());
        assertEquals("\"value\"", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertEquals(true, attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertEquals(false, attribute.shouldCollapseAttribute(out));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertEquals(true, Attribute.isBooleanAttribute("checked"));
        assertEquals(false, Attribute.isBooleanAttribute("key"));
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "differentValue");
        Attribute attribute4 = new Attribute("differentKey", "value");
        
        assertEquals(true, attribute1.equals(attribute2));
        assertEquals(false, attribute1.equals(attribute3));
        assertEquals(false, attribute1.equals(attribute4));
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertEquals(attribute.parent, clonedAttribute.parent);
    }
}