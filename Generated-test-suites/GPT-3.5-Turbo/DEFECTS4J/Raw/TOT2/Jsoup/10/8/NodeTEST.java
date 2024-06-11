import org.junit.*;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        // Test the constructor with baseUri and attributes
        Node node = new ConcreteNode("https://www.example.com", new Attributes());
        
        assertEquals("https://www.example.com", node.baseUri());
        assertNotNull(node.attributes());
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testNodeConstructorWithBaseUri() {
        // Test the constructor with baseUri only
        Node node = new ConcreteNode("https://www.example.com");
        
        assertEquals("https://www.example.com", node.baseUri());
        assertNotNull(node.attributes());
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testNodeAttr() {
        // Test getting and setting attributes
        Node node = new ConcreteNode("https://www.example.com");
        node.attr("key", "value");
        
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testNodeRemoveAttr() {
        // Test removing an attribute
        Node node = new ConcreteNode("https://www.example.com");
        node.attr("key", "value");
        node.removeAttr("key");
        
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        // Test getting absolute URL from a relative URL
        Node node = new ConcreteNode("https://www.example.com");
        node.attr("href", "/page");
        
        assertEquals("https://www.example.com/page", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        // Test getting child node by index
        Node parent = new ConcreteNode("https://www.example.com");
        Node child1 = new ConcreteNode("https://www.example.com/child1");
        Node child2 = new ConcreteNode("https://www.example.com/child2");
        
        parent.addChildren(child1, child2);
        
        assertEquals(child1, parent.childNode(0));
    }

    @Test
    public void testParent() {
        // Test getting parent node
        Node parent = new ConcreteNode("https://www.example.com");
        Node child = new ConcreteNode("https://www.example.com/child");
        
        parent.addChildren(child);
        
        assertEquals(parent, child.parent());
    }

    @Test
    public void testReplaceWith() {
        // Test replacing a node with another node
        Node parent = new ConcreteNode("https://www.example.com");
        Node original = new ConcreteNode("https://www.example.com/original");
        Node replacement = new ConcreteNode("https://www.example.com/replacement");
        
        parent.addChildren(original);
        original.replaceWith(replacement);
        
        assertNull(original.parent());
        assertEquals(replacement, parent.childNode(0));
    }

    @Test
    public void testClone() {
        // Test cloning a node
        Node original = new ConcreteNode("https://www.example.com");
        original.attr("key", "value");
        Node clone = original.clone();
        
        assertEquals(original.baseUri(), clone.baseUri());
        assertEquals(original.attr("key"), clone.attr("key"));
    }

}