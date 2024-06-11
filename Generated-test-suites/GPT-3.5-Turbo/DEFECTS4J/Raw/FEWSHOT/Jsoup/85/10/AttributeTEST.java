package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class AttributeTest {
    @Test
    public void testAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("newKey=\"newValue\"", attribute.html());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "differentValue");
        
        assertEquals(attribute1, attribute2);
        assertFalse(attribute1.equals(attribute3));
    }
}