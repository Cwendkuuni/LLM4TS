package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AttributeTest {
    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        
        Attributes parent = new Attributes();
        Attribute attributeWithParent = new Attribute("key", "value", parent);
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertEquals(parent, attributeWithParent.parent);
    }
    
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        
        Attributes parent = new Attributes();
        parent.put("key", "value");
        Attribute attributeWithParent = new Attribute("key", "value", parent);
        attributeWithParent.setKey("newKey");
        assertEquals("newKey", attributeWithParent.getKey());
        assertEquals("value", parent.get("newKey"));
    }
    
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        
        Attributes parent = new Attributes();
        Attribute attributeWithParent = new Attribute("key", "value", parent);
        attributeWithParent.setValue("newValue");
        assertEquals("newValue", attributeWithParent.getValue());
        assertEquals("newValue", parent.get("key"));
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
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertEquals("unencodedKey", attribute.getKey());
        assertEquals("encodedValue", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertEquals(true, attribute.isDataAttribute());
        
        Attribute attributeWithoutDataPrefix = new Attribute("key", "value");
        assertEquals(false, attributeWithoutDataPrefix.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "");
        assertEquals(true, attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        
        Attribute attributeWithValueSameAsKey = new Attribute("key", "key");
        assertEquals(true, attributeWithValueSameAsKey.shouldCollapseAttribute(new Document.OutputSettings()));
        
        Attribute attributeWithDifferentValueAndKey = new Attribute("key", "value");
        assertEquals(false, attributeWithDifferentValueAndKey.shouldCollapseAttribute(new Document.OutputSettings()));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(false, attribute.isBooleanAttribute());
        
        Attribute booleanAttribute = new Attribute("checked", null);
        assertEquals(true, booleanAttribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "otherValue");
        
        assertEquals(true, attribute1.equals(attribute2));
        assertEquals(false, attribute1.equals(attribute3));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.getKey().hashCode() * 31 + attribute.getValue().hashCode();
        assertEquals(hashCode, attribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
        assertEquals(attribute.parent, clone.parent);
    }
}