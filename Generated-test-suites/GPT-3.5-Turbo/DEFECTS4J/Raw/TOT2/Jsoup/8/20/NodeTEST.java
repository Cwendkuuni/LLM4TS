import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    
    @Test
    public void testNodeInitialization() {
        Node node = new Element("http://example.com", new Attributes());
        
        assertNotNull(node);
        assertEquals("http://example.com", node.baseUri());
        assertEquals(0, node.childNodes().size());
        assertFalse(node.hasAttr("test"));
    }
    
    @Test
    public void testNodeAttributes() {
        Node node = new Element("http://example.com", new Attributes());
        
        node.attr("test", "value");
        assertTrue(node.hasAttr("test"));
        assertEquals("value", node.attr("test"));
        
        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));
        assertEquals("", node.attr("test"));
    }
    
    @Test
    public void testAbsUrl() {
        Node node = new Element("http://example.com", new Attributes());
        
        node.attr("href", "/path/to/page");
        assertEquals("http://example.com/path/to/page", node.absUrl("href"));
        
        node.attr("src", "http://example.com/image.jpg");
        assertEquals("http://example.com/image.jpg", node.absUrl("src"));
    }

    // Additional test cases for other methods can be added here
    
}