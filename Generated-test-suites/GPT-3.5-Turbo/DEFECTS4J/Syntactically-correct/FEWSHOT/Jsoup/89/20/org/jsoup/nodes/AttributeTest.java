package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class AttributeTest {
    
    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        
        attribute = new Attribute("key", "value", new Attributes());
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNotNull(attribute.parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeConstructorNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeConstructorEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeConstructorNullParent() {
        new Attribute("key", "value", null);
    }
    
    @Test
    public void testAttributeGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testAttributeSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAttributeSetKeyNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeSetKeyEmptyKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test
    public void testAttributeSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAttributeSetValueNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }
    
    @Test
    public void testAttributeHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }
    
    @Test
    public void testAttributeToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }
    
    @Test
    public void testAttributeCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testAttributeIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-key"));
    }
    
    @Test
    public void testAttributeShouldCollapseAttribute() {
        assertTrue(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings()));
    }
    
    @Test
    public void testAttributeIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }
    
    @Test
    public void testAttributeEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "newValue");
        Attribute attribute4 = new Attribute("key", null);
        
        assertTrue(attribute1.equals(attribute2));
        assertTrue(attribute2.equals(attribute1));
        assertTrue(attribute1.equals(attribute1));
        
        assertTrue(!attribute1.equals(attribute3));
        assertTrue(!attribute3.equals(attribute1));
        
        assertTrue(!attribute1.equals(attribute4));
        assertTrue(!attribute4.equals(attribute1));
    }
    
    @Test
    public void testAttributeHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        
        assertTrue(attribute1.hashCode() == attribute2.hashCode());
    }
    
    @Test
    public void testAttributeClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNull(clonedAttribute.parent);
        assertTrue(attribute != clonedAttribute);
    }
}