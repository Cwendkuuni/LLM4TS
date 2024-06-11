
import org.jsoup.nodes.Attribute;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
    
    // Test cases for the constructor
    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
    
    // Test cases for setKey() method
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyEmpty() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }
    
    // Test cases for setValue() method
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }
    
    // Test cases for html() method
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        String html = attribute.html();
        assertEquals("href=\"index.html\"", html);
    }
    
    // Test cases for toString() method (which calls html() method)
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("href", "index.html");
        String html = attribute.toString();
        assertEquals("href=\"index.html\"", html);
    }
    
    // Test cases for createFromEncoded() method
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("href", "&lt;index.html&gt;");
        assertEquals("href", attribute.getKey());
        assertEquals("<index.html>", attribute.getValue());
    }
    
    // Test cases for isDataAttribute() method
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }
    
    @Test
    public void testIsDataAttributeFalse() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }
    
    // Test cases for shouldCollapseAttribute() method
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("checked", "");
        assertTrue(attribute.shouldCollapseAttribute(null));
    }
    
    @Test
    public void testShouldCollapseAttributeFalse() {
        Attribute attribute = new Attribute("href", "index.html");
        assertFalse(attribute.shouldCollapseAttribute(null));
    }
    
    // Test cases for isBooleanAttribute() method
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", "");
        assertTrue(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testIsBooleanAttributeFalse() {
        Attribute attribute = new Attribute("href", "index.html");
        assertFalse(attribute.isBooleanAttribute());
    }
    
    // Test cases for equals() method
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testEqualsDifferentKey() {
        Attribute attribute1 = new Attribute("key1", "value");
        Attribute attribute2 = new Attribute("key2", "value");
        assertFalse(attribute1.equals(attribute2));
    }
    
    @Test
    public void testEqualsDifferentValue() {
        Attribute attribute1 = new Attribute("key", "value1");
        Attribute attribute2 = new Attribute("key", "value2");
        assertFalse(attribute1.equals(attribute2));
    }
    
    @Test
    public void testEqualsNull() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.equals(null));
    }
    
    @Test
    public void testEqualsSame() {
        Attribute attribute = new Attribute("key", "value");
        assertTrue(attribute.equals(attribute));
    }
    
    // Test cases for hashCode() method
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.hashCode() == attribute2.hashCode());
    }
    
    // Test case for clone() method
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assertEquals(attribute, clone);
    }
}
