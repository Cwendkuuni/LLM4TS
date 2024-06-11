import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testConstructorWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.parent);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorKeyNull() {
        new Attribute(null, "value");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorKeyEmpty() {
        new Attribute("", "value");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorValNull() {
        new Attribute("key", null);
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("oldKey", "value");
        
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
    
    @Test
    public void testSetValue() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "oldValue", parent);
        
        String oldValue = attribute.setValue("newValue");
        
        assertEquals("newValue", attribute.getValue());
        assertEquals("oldValue", oldValue);
        assertEquals("newValue", parent.get("key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        
        attribute.setValue(null);
    }
    
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        
        String key = attribute.getKey();
        
        assertEquals("key", key);
    }
    
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        
        String value = attribute.getValue();
        
        assertEquals("value", value);
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        
        String html = attribute.html();
        
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        
        String string = attribute.toString();
        
        assertEquals("key=\"value\"", string);
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("\"value\"", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        
        boolean isDataAttribute = attribute.isDataAttribute();
        
        assertTrue(isDataAttribute);
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html);
        
        boolean shouldCollapseAttribute = Attribute.shouldCollapseAttribute("checked", "checked", out);
        
        assertTrue(shouldCollapseAttribute);
    }
    
    @Test
    public void testIsBooleanAttribute() {
        boolean isBooleanAttribute = Attribute.isBooleanAttribute("checked");
        
        assertTrue(isBooleanAttribute);
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        
        boolean equals = attribute1.equals(attribute2);
        
        assertTrue(equals);
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        
        int hashCode = attribute.hashCode();
        
        assertEquals(961524, hashCode);
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        
        Attribute clone = attribute.clone();
        
        assertNotNull(clone);
        assertNotSame(attribute, clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
        assertNull(clone.parent);
    }
}