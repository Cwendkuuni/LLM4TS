package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AttributeTest {
    @Test
    public void testAttribute() {
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
       attribute.setValue("newValue");
       assertEquals("newValue", attribute.getValue());
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
    public void testClone() {
       Attribute attribute = new Attribute("key", "value");
       Attribute clonedAttribute = attribute.clone();
       assertEquals(attribute.getKey(), clonedAttribute.getKey());
       assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
    
    @Test
    public void testCreateFromEncoded() {
       Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
       assertEquals("key", attribute.getKey());
       assertEquals("\"value\"", attribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
       Attribute attribute1 = new Attribute("data-key", "value");
       Attribute attribute2 = new Attribute("key", "value");
       assertEquals(true, attribute1.isDataAttribute());
       assertEquals(false, attribute2.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
       Attribute attribute1 = new Attribute("key", "key");
       Attribute attribute2 = new Attribute("key", "");
       Attribute attribute3 = new Attribute("key", null);
       Document.OutputSettings out = new Document.OutputSettings();
       assertEquals(true, attribute1.shouldCollapseAttribute(out));
       assertEquals(true, attribute2.shouldCollapseAttribute(out));
       assertEquals(true, attribute3.shouldCollapseAttribute(out));
    }
    
    @Test
    public void testIsBooleanAttribute() {
       Attribute attribute1 = new Attribute("key", "value");
       Attribute attribute2 = new Attribute("allowfullscreen", null);
       assertEquals(false, attribute1.isBooleanAttribute());
       assertEquals(true, attribute2.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
       Attribute attribute1 = new Attribute("key", "value");
       Attribute attribute2 = new Attribute("key", "value");
       Attribute attribute3 = new Attribute("anotherKey", "value");
       assertEquals(true, attribute1.equals(attribute2));
       assertEquals(false, attribute1.equals(attribute3));
    }
    
    @Test
    public void testHashCode() {
       Attribute attribute1 = new Attribute("key", "value");
       Attribute attribute2 = new Attribute("key", "value");
       assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }
}