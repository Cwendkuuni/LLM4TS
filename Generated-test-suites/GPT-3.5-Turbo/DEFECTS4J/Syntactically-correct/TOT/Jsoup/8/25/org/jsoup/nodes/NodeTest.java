import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void attrTest() {
        Node node = new Node("https://www.example.com");
        node.attr("attribute1", "value1");
        node.attr("attribute2", "value2");
        
        String attribute1 = node.attr("attribute1");
        assertEquals("value1", attribute1);
        
        String attribute2 = node.attr("attribute2");
        assertEquals("value2", attribute2);
        
        boolean hasAttribute1 = node.hasAttr("attribute1");
        assertTrue(hasAttribute1);
        
        boolean hasAttribute3 = node.hasAttr("attribute3");
        assertFalse(hasAttribute3);
        
        node.removeAttr("attribute1");
        boolean hasAttribute1AfterRemoval = node.hasAttr("attribute1");
        assertFalse(hasAttribute1AfterRemoval);
    }
    
    @Test
    public void baseUriTest() {
        Node node = new Node("https://www.example.com");
        String baseUri = node.baseUri();
        assertEquals("https://www.example.com", baseUri);
        
        node.setBaseUri("https://www.example2.com");
        String updatedBaseUri = node.baseUri();
        assertEquals("https://www.example2.com", updatedBaseUri);
    }
    
    @Test
    public void absUrlTest() {
        Node node = new Node("https://www.example.com");
        node.attr("href", "/test.html");
        
        String absUrl = node.absUrl("href");
        assertEquals("https://www.example.com/test.html", absUrl);
        
        node.setBaseUri("https://www.example2.com");
        
        String updatedAbsUrl = node.absUrl("href");
        assertEquals("https://www.example2.com/test.html", updatedAbsUrl);
    }
    
    @Test
    public void childNodeTest() {
        Node parent = new Node("https://www.example.com");
        Node child1 = new Node("Child 1");
        Node child2 = new Node("Child 2");
        parent.addChildren(child1, child2);
        
        Node retrievedChild1 = parent.childNode(0);
        assertEquals(child1, retrievedChild1);
        
        Node retrievedChild2 = parent.childNode(1);
        assertEquals(child2, retrievedChild2);
        
        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));
    }
    
    @Test
    public void siblingNodesTest() {
        Node parent = new Node("https://www.example.com");
        Node sibling1 = new Node("Sibling 1");
        Node sibling2 = new Node("Sibling 2");
        Node node = new Node("Node");
        parent.addChildren(sibling1, node, sibling2);
        
        List<Node> siblingNodes = node.siblingNodes();
        assertEquals(3, siblingNodes.size());
        assertEquals(sibling1, siblingNodes.get(0));
        assertEquals(node, siblingNodes.get(1));
        assertEquals(sibling2, siblingNodes.get(2));
        
        Node previousSibling = node.previousSibling();
        assertEquals(sibling1, previousSibling);
        
        Node nextSibling = node.nextSibling();
        assertEquals(sibling2, nextSibling);
    }
    
    @Test
    public void replaceWithTest() {
        Node parent = new Node("https://www.example.com");
        Node node1 = new Node("Node 1");
        Node node2 = new Node("Node 2");
        parent.addChildren(node1, new Node("Node 3"), new Node("Node 4"));
        
        node1.replaceWith(node2);
        
        List<Node> childNodes = parent.childNodes();
        assertEquals(3, childNodes.size());
        assertEquals(node2, childNodes.get(0));
        assertEquals("Node 2", childNodes.get(0).nodeName());
        assertEquals("Node 3", childNodes.get(1).nodeName());
        assertEquals("Node 4", childNodes.get(2).nodeName());
    }
    
    @Test
    public void removeTest() {
        Node parent = new Node("https://www.example.com");
        Node node1 = new Node("Node 1");
        Node node2 = new Node("Node 2");
        parent.addChildren(node1, new Node("Node 3"), node2);
        
        node1.remove();
        
        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals("Node 3", childNodes.get(0).nodeName());
        assertEquals(node2, childNodes.get(0));
        assertEquals("Node 2", childNodes.get(1).nodeName());
    }

}