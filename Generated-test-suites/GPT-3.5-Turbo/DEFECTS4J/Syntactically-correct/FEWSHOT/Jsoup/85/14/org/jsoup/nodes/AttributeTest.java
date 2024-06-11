package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class AttributeTest {
    
    @Test
    public void testConstructor() {
        Attribute attribute1 = new Attribute("key1", "value1");
        assertEquals("key1", attribute1.getKey());
        assertEquals("value1", attribute1.getValue());
        assertNull(attribute1.parent);
        
        Attribute attribute2 = new Attribute("key2", "value2", null);
        assertEquals("key2", attribute2.getKey());
        assertEquals("value2", attribute2.getValue());
        assertNull(attribute2.parent);
        
        Attribute attribute3 = new Attribute(null, "value3", null);
        assertNull(attribute3.getKey());
        assertEquals("value3", attribute3.getValue());
        assertNull(attribute3.parent);
    }
    
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        
        attribute.parent = new Attributes();
        attribute.setKey("newKey2");
        assertEquals("newKey2", attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        
        attribute.parent = new Attributes();
        attribute.setValue("newValue2");
        assertEquals("newValue2", attribute.getValue());
    }
    
    @Test
    public void testHtml() throws IOException {
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
        Attribute attribute = Attribute.createFromEncoded("key", "encodedValue");
        assertEquals("key", attribute.getKey());
        assertEquals("encodedValue", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-key"));
        assertFalse(Attribute.isDataAttribute("key"));
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        
        assertTrue(Attribute.shouldCollapseAttribute("key", null, outputSettings));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", outputSettings));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", outputSettings));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", outputSettings));
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key1", "value1");
        
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
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
    }
}