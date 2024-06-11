package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributeTest {
    
    @Test
    public void testConstructor() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);
    }
    
    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertNull(attr.parent);
    }
    
    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String oldValue = attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertEquals("value", oldValue);
        assertNull(attr.parent);
    }
    
    @Test
    public void testHtml() {
        Attribute attr = new Attribute("key", "value");
        String html = attr.html();
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        String string = attr.toString();
        assertEquals("key=\"value\"", string);
    }
    
    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        Attribute attr3 = new Attribute("otherKey", "value");
        
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
    }
    
    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        
        assertEquals(attr1.hashCode(), attr2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.parent);
    }
}