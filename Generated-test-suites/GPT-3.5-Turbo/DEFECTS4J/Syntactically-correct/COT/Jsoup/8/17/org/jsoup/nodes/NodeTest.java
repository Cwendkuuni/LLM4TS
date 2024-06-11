
import org.junit.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class NodeTest {
    private Node node;
    
    @Before
    public void setUp() {
        node = new Node();
    }
    
    @Test
    public void testNodeName() {
        assertEquals("Node", node.nodeName());
    }
    
    @Test
    public void testAttr() {
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }
    
    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";
        node.setBaseUri(baseUri);
        assertEquals(baseUri, node.baseUri());
    }
    
    @Test
    public void testChildNode() {
        Node childNode = new Node();
        node.addChildren(childNode);
        assertEquals(childNode, node.childNode(0));
    }
    
    @Test
    public void testParent() {
        Node parentNode = new Node();
        node.setParentNode(parentNode);
        assertEquals(parentNode, node.parent());
    }
    
    @Test
    public void testSiblingNodes() {
        Node siblingNode = new Node();
        Node parentNode = new Node();
        parentNode.addChildren(node, siblingNode);
        List<Node> expectedSiblings = new ArrayList<>();
        expectedSiblings.add(node);
        expectedSiblings.add(siblingNode);
        assertEquals(expectedSiblings, node.siblingNodes());
    }
    
    @Test
    public void testNextSibling() {
        Node siblingNode = new Node();
        Node parentNode = new Node();
        parentNode.addChildren(node, siblingNode);
        assertEquals(siblingNode, node.nextSibling());
    }
    
    @Test
    public void testPreviousSibling() {
        Node siblingNode = new Node();
        Node parentNode = new Node();
        parentNode.addChildren(siblingNode, node);
        assertEquals(siblingNode, node.previousSibling());
    }
}
