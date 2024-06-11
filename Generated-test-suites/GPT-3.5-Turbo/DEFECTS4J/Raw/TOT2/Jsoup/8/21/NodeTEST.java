import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        Node node = new Element("div");
        assertNotNull(node);
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttributeManipulation() {
        Node node = new Element("a");
        node.attr("href", "https://www.example.com");
        assertTrue(node.hasAttr("href"));
        assertEquals("https://www.example.com", node.attr("href"));
        node.removeAttr("href");
        assertFalse(node.hasAttr("href"));
    }

    @Test
    public void testBaseUri() {
        Node node = new Element("img", new Attributes());
        node.setBaseUri("https://www.example.com");
        assertEquals("https://www.example.com", node.baseUri());
    }

    @Test
    public void testSiblingOperations() {
        Node parent = new Element("body");
        Node child1 = new Element("div");
        Node child2 = new Element("span");
        
        parent.addChildren(child1, child2);
        assertEquals(1, child2.siblingIndex().intValue());
        
        Node nextSibling = child1.nextSibling();
        assertEquals(child2, nextSibling);
        
        Node previousSibling = child2.previousSibling();
        assertEquals(child1, previousSibling);
        
        assertEquals(2, parent.siblingNodes().size());
    }

    // Add more test cases for other methods in the Node class

}