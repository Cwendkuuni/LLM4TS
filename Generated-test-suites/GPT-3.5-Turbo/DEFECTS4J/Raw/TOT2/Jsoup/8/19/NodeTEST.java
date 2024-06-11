import org.jsoup.nodes.Node;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    
    Node node;

    @Before
    public void setUp() {
        node = new Node("https://www.example.com");
    }

    @Test
    public void testNodeCreation() {
        assertNotNull(node);
        assertEquals("https://www.example.com", node.baseUri());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNodeAttributes() {
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testNodeChildNodes() {
        Node childNode = new Node("child", new Attributes());
        node.addChild(childNode);
        assertEquals(childNode, node.childNode(0));
    }

    @Test
    public void testSiblingNodes() {
        Node sibling1 = new Node("sibling1", new Attributes());
        Node sibling2 = new Node("sibling2", new Attributes());
        node.addChildren(sibling1, sibling2);
        assertEquals(2, node.siblingNodes().size());
    }

    @Test
    public void testParentNode() {
        Node parent = new Node("parent", new Attributes());
        node.setParentNode(parent);
        assertEquals(parent, node.parent());
    }

    @Test
    public void testReplaceChild() {
        Node child1 = new Node("child1", new Attributes());
        Node child2 = new Node("child2", new Attributes());
        node.addChild(child1);
        node.replaceChild(child1, child2);
        assertEquals(child2, node.childNode(0));
    }

    @Test
    public void testReindexChildren() {
        Node child1 = new Node("child1", new Attributes());
        Node child2 = new Node("child2", new Attributes());
        node.addChild(child1, child2);
        node.removeChildren(0);
        assertEquals(child2, node.childNode(0));
    }

    @Test
    public void testOwnerDocument() {
        Node documentNode = new Document("https://www.example.com");
        node.addChild(documentNode);
        assertEquals(documentNode, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Node("parent", new Attributes());
        node.setParentNode(parent);
        node.remove();
        assertFalse(parent.childNodes().contains(node));
    }

    @Test
    public void testEquals() {
        Node node1 = new Node("https://www.example.com");
        Node node2 = new Node("https://www.example.com");
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        assertEquals(node.parentNode != null ? node.parentNode.hashCode() : 0, node.hashCode());
    }
}