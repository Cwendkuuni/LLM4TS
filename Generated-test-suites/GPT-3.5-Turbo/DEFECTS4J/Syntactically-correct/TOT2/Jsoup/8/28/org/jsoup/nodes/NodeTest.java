import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    
    @Test
    public void testNodeConstructorWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        Node node = new Element("http://example.com", attributes);
        
        assertEquals("http://example.com", node.baseUri());
        assertTrue(node.hasAttr("key1"));
        assertEquals("value1", node.attr("key1"));
        assertTrue(node.hasAttr("key2"));
        assertEquals("value2", node.attr("key2"));
    }
    
    @Test
    public void testNodeConstructorWithoutAttributes() {
        Node node = new Element("http://example.com");
        
        assertEquals("http://example.com", node.baseUri());
        assertFalse(node.hasAttr("key1"));
        assertEquals("", node.attr("key1"));
    }
    
    @Test
    public void testNodeAttr() {
        Node node = new Element("http://example.com");
        node.attr("key", "value");
        
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }
    
    @Test
    public void testNodeHasAttr() {
        Node node = new Element("http://example.com");
        node.attr("key", "value");
        
        assertTrue(node.hasAttr("key"));
        assertFalse(node.hasAttr("nonexistent"));
    }
    
    @Test
    public void testNodeRemoveAttr() {
        Node node = new Element("http://example.com");
        node.attr("key", "value");
        
        assertTrue(node.hasAttr("key"));
        
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }
    
    // More test cases for other methods can be added here
    
}