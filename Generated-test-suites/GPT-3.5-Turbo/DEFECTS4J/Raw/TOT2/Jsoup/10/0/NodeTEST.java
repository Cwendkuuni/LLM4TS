import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    
    @Test
    public void testNodeCreation() {
        Node node = new Element("https://www.example.com", new Attributes());
        assertNotNull(node);
        assertEquals("https://www.example.com", node.baseUri());
        assertTrue(node.attributes().isEmpty());
    }
    
    @Test
    public void testAttr() {
        Node node = new Element("https://www.example.com", new Attributes());
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }
    
    @Test
    public void testAbsUrl() {
        Node node = new Element("https://www.example.com", new Attributes());
        node.attr("href", "/page.html");
        assertEquals("https://www.example.com/page.html", node.absUrl("href"));
    }
    
    @Test
    public void testChildNode() {
        Node parent = new Element("https://www.example.com", new Attributes());
        Node child1 = new Element("https://www.example.com", new Attributes());
        Node child2 = new Element("https://www.example.com", new Attributes());
        parent.addChildren(child1, child2);
        
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }
    
    @Test
    public void testRemove() {
        Node parent = new Element("https://www.example.com", new Attributes());
        Node child = new Element("https://www.example.com", new Attributes());
        parent.addChildren(child);
        
        assertEquals(1, parent.childNodes().size());
        child.remove();
        assertEquals(0, parent.childNodes().size());
    }
    
    @Test
    public void testClone() {
        Node original = new Element("https://www.example.com", new Attributes());
        Node clone = original.clone();
        
        assertNotSame(original, clone);
        assertEquals(original.nodeName(), clone.nodeName());
        assertEquals(original.baseUri(), clone.baseUri());
        assertEquals(original.attributes(), clone.attributes());
    }
    
    // Add more test cases for other methods
    
}