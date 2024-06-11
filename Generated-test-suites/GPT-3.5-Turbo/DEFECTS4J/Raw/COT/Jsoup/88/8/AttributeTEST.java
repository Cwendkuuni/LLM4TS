
import org.junit.Test;

public class AttributeTest {
    
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        String key = attribute.getKey();
        assertEquals("key", key);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("oldKey", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        String value = attribute.getValue();
        assertEquals("value", value);
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "oldValue");
        String oldValue = attribute.setValue("newValue");
        assertEquals("oldValue", oldValue);
        assertEquals("newValue", attribute.getValue());
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
        String toString = attribute.toString();
        assertEquals("key=\"value\"", toString);
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value&amp;");
        assertEquals("key", attribute.getKey());
        assertEquals("value&", attribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        boolean isDataAttribute = attribute.isDataAttribute();
        assertTrue(isDataAttribute);
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "key");
        Document.OutputSettings out = new Document.OutputSettings();
        boolean shouldCollapseAttribute = attribute.shouldCollapseAttribute(out);
        assertTrue(shouldCollapseAttribute);
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", "");
        boolean isBooleanAttribute = attribute.isBooleanAttribute();
        assertTrue(isBooleanAttribute);
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "differentValue");
        
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        
        assertNotNull(clone);
        assertNotSame(attribute, clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }
}

