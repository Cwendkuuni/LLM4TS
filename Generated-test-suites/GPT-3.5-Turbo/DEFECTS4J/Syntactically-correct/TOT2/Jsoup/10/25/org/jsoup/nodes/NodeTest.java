import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        Attributes attributes = new Attributes();
        Node node = new Element("https://www.example.com", attributes);
        
        assertEquals("https://www.example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithBaseUri() {
        Node node = new Element("https://www.example.com");
        
        assertEquals("https://www.example.com", node.baseUri());
        assertEquals(0, node.attributes().size());
    }

    @Test
    public void testNodeChildNode() {
        Element childNode = new Element("https://www.example.com");
        Node parentNode = new Element("https://www.example.com", new Attributes());
        parentNode.addChildren(childNode);
        
        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testNodeSiblingNodes() {
        Element siblingNode1 = new Element("https://www.example.com");
        Element siblingNode2 = new Element("https://www.example.com");
        Node parentNode = new Element("https://www.example.com", new Attributes());
        parentNode.addChildren(siblingNode1, siblingNode2);
        
        assertEquals(2, parentNode.siblingNodes().size());
        assertTrue(parentNode.siblingNodes().contains(siblingNode1));
        assertTrue(parentNode.siblingNodes().contains(siblingNode2));
    }

    @Test
    public void testNodeParent() {
        Node childNode = new Element("https://www.example.com");
        Node parentNode = new Element("https://www.example.com", new Attributes());
        parentNode.addChildren(childNode);
        
        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testNodeAttr() {
        Node node = new Element("https://www.example.com");
        node.attr("key", "value");
        
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeRemoveAttr() {
        Node node = new Element("https://www.example.com");
        node.attr("key", "value");
        
        assertTrue(node.hasAttr("key"));
        
        node.removeAttr("key");
        
        assertFalse(node.hasAttr("key"));
        node.removeAttr("key"); // should throw exception
    }

    @Test
    public void testNodeReplaceWith() {
        Node node1 = new Element("https://www.example.com");
        Node node2 = new Element("https://www.example-2.com");
        Node parentNode = new Element("https://www.example.com", new Attributes());
        parentNode.addChildren(node1);
        
        parentNode.replaceWith(node1, node2);
        
        assertFalse(parentNode.childNodes().contains(node1));
        assertTrue(parentNode.childNodes().contains(node2));
    }

    @Test
    public void testNodeClone() {
        Element node = new Element("https://www.example.com");
        node.attr("key", "value");
        Element clonedNode = node.clone();
        
        assertEquals(node.baseUri(), clonedNode.baseUri());
        assertEquals(node.attributes(), clonedNode.attributes());
        assertEquals(node.attr("key"), clonedNode.attr("key"));
        assertNotSame(node, clonedNode);
    }

}