import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import java.io.IOException;

public class AttributeTest {
    
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
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
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String str = attribute.toString();
        assertEquals("key=\"value\"", str);
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }
}