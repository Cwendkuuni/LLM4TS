package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    
    @Test
    public void testGetKey() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
    }
    
    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        
        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setKey("newKey");
        assertNotNull(attrWithParent.parent);
        assertEquals("newKey", attrWithParent.parent.keys[0]);
    }
    
    @Test
    public void testGetValue() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("value", attr.getValue());
    }
    
    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String oldValue = attr.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attr.getValue());
        
        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setValue("newValue");
        assertNotNull(attrWithParent.parent);
        assertEquals("newValue", attrWithParent.parent.vals[0]);
    }
    
    @Test
    public void testHtml() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.html());
    }
    
    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        Attribute attr3 = new Attribute("key", "differentValue");
        
        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
    }
    
    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        
        assertNotSame(attr, clonedAttr);
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }

}