
package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 A single key + value attribute. (Only used for presentation.)
 */
public class AttributeTest {

    public static void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        
        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertNotNull(attributeWithParent.parent);
    }
    
    public static void testSetKey() {
        Attribute attribute = new Attribute("oldKey", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        
        attribute.parent = new Attributes();
        attribute.parent.put("oldKey", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertNull(attribute.parent.get("oldKey"));
        assertEquals("value", attribute.parent.get("newKey"));
    }
    
    public static void testSetValue() {
        Attribute attribute = new Attribute("key", "oldValue");
        String oldValue = attribute.setValue("newValue");
        assertEquals("oldValue", oldValue);
        assertEquals("newValue", attribute.getValue());
        
        attribute.parent = new Attributes();
        attribute.parent.put("key", "oldValue");
        oldValue = attribute.setValue("newValue");
        assertEquals("oldValue", oldValue);
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", attribute.parent.get("key"));
    }
    
    public static void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
        
        Attribute attributeWithEscaping = new Attribute("key", "<value&>");
        html = attributeWithEscaping.html();
        assertEquals("key=\"&lt;value&amp;&gt;\"", html);
    }
    
    public static void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String string = attribute.toString();
        assertEquals("key=\"value\"", string);
        
        Attribute attributeWithEscaping = new Attribute("key", "<value&>");
        string = attributeWithEscaping.toString();
        assertEquals("key=\"&lt;value&amp;&gt;\"", string);
    }
    
    public static void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&lt;value&amp;&gt;");
        assertEquals("key", attribute.getKey());
        assertEquals("<value&>", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    public static void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
        
        Attribute nonDataAttribute = new Attribute("key", "value");
        assertFalse(nonDataAttribute.isDataAttribute());
    }
    
    public static void testShouldCollapseAttribute() {
        Attribute booleanAttribute = new Attribute("boolean", null);
        assertTrue(booleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
        
        Attribute nonBooleanAttribute = new Attribute("key", "value");
        assertFalse(nonBooleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }
    
    public static void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "differentValue");
        Attribute attribute4 = new Attribute("differentKey", "value");
        
        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));
    }
    
    public static void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }
    
    public static void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
        assertNotSame(attribute, clone);
        assertNotSame(attribute.parent, clone.parent);
    }
}
