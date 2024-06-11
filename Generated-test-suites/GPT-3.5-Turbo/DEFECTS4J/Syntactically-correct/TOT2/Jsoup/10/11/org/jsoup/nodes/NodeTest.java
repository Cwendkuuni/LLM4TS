import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    
    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        // Test creating a Node with base URI and attributes
        Attributes attributes = new Attributes();
        Node node = new ElementNode("http://example.com", attributes);
        
        assertEquals("http://example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
    }
    
    @Test
    public void testNodeConstructorWithBaseUriOnly() {
        // Test creating a Node with base URI only
        Node node = new ElementNode("http://example.com");
        
        assertEquals("http://example.com", node.baseUri());
        assertNotNull(node.attributes());
    }
    
    @Test
    public void testNodeConstructorDefault() {
        // Test creating a default Node
        Node node = new ElementNode();
        
        assertEquals("", node.baseUri());
        assertNotNull(node.attributes());
    }
    
    @Test
    public void testNodeAttr() {
        // Test getting and setting attributes
        Node node = new ElementNode();
        node.attr("key", "value");
        
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
        
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }
    
    @Test
    public void testNodeChildNodes() {
        // Test adding and retrieving child nodes
        Node parent = new ElementNode();
        Node child1 = new ElementNode();
        Node child2 = new ElementNode();
        
        parent.addChildren(child1, child2);
        
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }
    
    @Test
    public void testNodeParent() {
        // Test getting the parent node
        Node parent = new ElementNode();
        Node child = new ElementNode();
        
        parent.addChildren(child);
        
        assertEquals(parent, child.parent());
    }
    
    @Test
    public void testNodeRemove() {
        // Test removing a node from the tree
        Node parent = new ElementNode();
        Node child = new ElementNode();
        
        parent.addChildren(child);
        child.remove();
        
        assertNull(child.parent());
    }
    
    @Test
    public void testNodeClone() {
        // Test cloning a node
        Node node = new ElementNode();
        Node clone = node.clone();
        
        assertNotSame(node, clone);
        assertEquals(node.baseUri(), clone.baseUri());
        assertEquals(node.attributes(), clone.attributes());
    }
}