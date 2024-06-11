import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;

public class NodeTest {
    
    @Test
    public void testNodeCreation() {
        Attributes attributes = new Attributes();
        Node node = new Node("http://example.com", attributes);
        assertNotNull(node);
        assertEquals("http://example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
    }
    
    @Test
    public void testNodeCreationWithBaseUriOnly() {
        Node node = new Node("http://example.com");
        assertNotNull(node);
        assertEquals("http://example.com", node.baseUri());
        assertNotNull(node.attributes());
    }
    
    @Test
    public void testNodeName() {
        Node node = new Node("http://example.com");
        assertEquals("Node", node.nodeName());
    }
    
    @Test
    public void testAttributeOperations() {
        Node node = new Node("http://example.com");
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }
    
    @Test
    public void testAbsUrl() {
        Node node = new Node("http://example.com");
        node.attr("href", "/path");
        assertEquals("http://example.com/path", node.absUrl("href"));
    }
    
    @Test
    public void testChildNodes() {
        Node node = new Node("http://example.com");
        Node child1 = new Node("child1");
        Node child2 = new Node("child2");
        node.addChildren(child1, child2);
        assertEquals(2, node.childNodes().size());
        assertEquals(child1, node.childNode(0));
        assertEquals(child2, node.childNode(1));
    }
    
    @Test
    public void testParentAndSiblingNodes() {
        Node parent = new Node("http://example.com");
        Node child1 = new Node("child1");
        Node child2 = new Node("child2");
        parent.addChildren(child1, child2);
        assertEquals(parent, child1.parent());
        assertEquals(child2, child1.nextSibling());
        assertEquals(null, child1.previousSibling());
        assertEquals(child1, child2.previousSibling());
        assertEquals(null, child2.nextSibling());
        assertEquals(0, child1.siblingIndex().intValue());
        assertEquals(1, child2.siblingIndex().intValue());
        assertEquals(2, parent.siblingNodes().size());
    }

    @Test
    public void testNodeRemovalAndReplacement() {
        Node parent = new Node("http://example.com");
        Node child1 = new Node("child1");
        Node child2 = new Node("child2");
        parent.addChildren(child1, child2);
        
        child1.remove();
        assertEquals(1, parent.childNodes().size());
        
        Node newChild = new Node("newChild");
        child2.replaceWith(newChild);
        assertEquals(newChild, parent.childNode(0));
    }
}