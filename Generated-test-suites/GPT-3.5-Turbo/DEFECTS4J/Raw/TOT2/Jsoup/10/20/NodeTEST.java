import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeName() {
        Node node = new Node() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };
        
        assertEquals("TestNode", node.nodeName());
    }

    @Test
    public void testAttributeOperations() {
        Node node = new Node();
        node.attr("key1", "value1");
        node.attr("key2", "value2");
        
        assertEquals("value1", node.attr("key1"));
        assertEquals("value2", node.attr("key2"));
        assertTrue(node.hasAttr("key1"));
        
        node.removeAttr("key1");
        assertFalse(node.hasAttr("key1"));
    }

    @Test
    public void testChildNodes() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        
        parentNode.addChildren(childNode1, childNode2);
        
        assertEquals(childNode1, parentNode.childNode(0));
        assertEquals(childNode2, parentNode.childNode(1));
    }

    @Test
    public void testSiblingNodes() {
        Node parent = new Node();
        Node sibling1 = new Node();
        Node sibling2 = new Node();
        
        parent.addChildren(sibling1, sibling2);
        
        assertEquals(sibling1, sibling2.previousSibling());
        assertEquals(sibling2, sibling1.nextSibling());
    }

    @Test
    public void testClone() {
        Node originalNode = new Node();
        originalNode.attr("key", "value");
        
        Node clonedNode = originalNode.clone();
        
        assertNotEquals(originalNode, clonedNode);
        assertEquals(originalNode.attr("key"), clonedNode.attr("key"));
    }

    @Test
    public void testBaseUri() {
        Node node = new Node();
        node.setBaseUri("https://www.example.com");
        
        assertEquals("https://www.example.com", node.baseUri());
    }
}