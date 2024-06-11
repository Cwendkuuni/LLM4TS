package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class AttributeTest {
    
    private Attribute attribute;
    
    @Before
    public void setup() {
        attribute = new Attribute("key", "value");
    }
    
    @Test
    public void testCreateAttribute() {
        assertNotNull(attribute);
    }
    
    @Test
    public void testGetKey() {
        String key = attribute.getKey();
        assertEquals("key", key);
    }
    
    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        String key = attribute.getKey();
        assertEquals("newKey", key);
    }
    
    @Test
    public void testGetValue() {
        String value = attribute.getValue();
        assertEquals("value", value);
    }
    
    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        String value = attribute.getValue();
        assertEquals("newValue", value);
    }
    
    @Test
    public void testHtml() {
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testToString() {
        String str = attribute.toString();
        assertEquals("key=\"value\"", str);
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        String key = encodedAttribute.getKey();
        String value = encodedAttribute.getValue();
        assertEquals("key", key);
        assertEquals("\"value\"", value);
    }
    
    @Test
    public void testIsDataAttribute() {
        boolean isDataAttr = attribute.isDataAttribute();
        assertFalse(isDataAttr);
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        boolean collapsible = attribute.shouldCollapseAttribute(new Document.OutputSettings());
        assertFalse(collapsible);
    }
    
    @Test
    public void testIsBooleanAttribute() {
        boolean isBooleanAttr = attribute.isBooleanAttribute();
        assertFalse(isBooleanAttr);
    }
    
    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        Attribute differentAttribute = new Attribute("newKey", "newValue");
        
        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentAttribute));
    }
    
    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute cloneAttribute = attribute.clone();
        assertEquals(attribute.getKey(), cloneAttribute.getKey());
        assertEquals(attribute.getValue(), cloneAttribute.getValue());
    }
}