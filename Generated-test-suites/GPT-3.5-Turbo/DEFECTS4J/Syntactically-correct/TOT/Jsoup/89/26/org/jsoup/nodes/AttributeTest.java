import org.junit.Test;

public class AttributeTest {
    
    @Test
    public void testCreateAttribute() {
        Attribute attribute = new Attribute("key", "value");
        
        assertNotNull(attribute);
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
        
        String html = attribute.html();
        
        assertNotNull(html);
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testFromString() {
        Attribute attribute = Attribute.createFromEncoded("key", "&lt;value&gt;");
        
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("<value>", attribute.getValue());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        
        assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        
        assertNotNull(clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
        assertFalse(attribute == clone);
    }
}