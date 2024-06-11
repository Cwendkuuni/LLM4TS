import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.junit.Test;

public class AttributeTest {
    @Test
    public void testCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
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
        Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        assertEquals("key", attribute.getKey());
        assertEquals("\"value\"", attribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute1 = new Attribute("data-key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.isDataAttribute());
        assertFalse(attribute2.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("key", "key");
        Attribute attribute2 = new Attribute("key", "");
        assertTrue(attribute1.shouldCollapseAttribute());
        assertTrue(attribute2.shouldCollapseAttribute());
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("booleanAttribute", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.isBooleanAttribute());
        assertFalse(attribute2.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "differentValue");
        Attribute attribute4 = new Attribute("differentKey", "value");
        assertEquals(attribute1, attribute2);
        assertNotEquals(attribute1, attribute3);
        assertNotEquals(attribute1, attribute4);
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        assertEquals(attribute1, attribute2);
        assertNotSame(attribute1, attribute2);
    }
}