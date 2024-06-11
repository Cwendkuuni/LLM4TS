package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

public class AttributeTest {
    
    private Attribute attribute;
    
    @Before
    public void setup() {
        attribute = new Attribute("key", "value");
    }
    
    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }
    
    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}
