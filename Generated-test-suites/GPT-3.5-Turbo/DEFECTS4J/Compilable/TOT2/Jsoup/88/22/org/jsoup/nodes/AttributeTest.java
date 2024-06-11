package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {
    
    private Attribute attribute;
    private Attributes parent;
    
    @Test
    public void testGetKey() {
        attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        attribute = new Attribute("key", "value");
        assertEquals("value", attribute.setValue("newValue"));
    }
    
    @Test
    public void testHtml() {
        attribute = new Attribute("key", "value");
        assertNotNull(attribute.html());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", newAttribute.getKey());
        assertEquals("value", newAttribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        attribute = new Attribute("key", "key");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testHashCode() {
        attribute = new Attribute("key", "value");
        assertNotNull(attribute.hashCode());
    }
    
    @Test
    public void testClone() {
        attribute = new Attribute("key", "value");
        assertTrue(attribute.clone() instanceof Attribute);
    }

}