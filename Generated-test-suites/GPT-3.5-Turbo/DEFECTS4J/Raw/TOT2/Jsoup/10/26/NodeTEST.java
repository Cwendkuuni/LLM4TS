import org.jsoup.nodes.Node;
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
    public void testAttrAndGetAttribute() {
        Node node = new Node("https://www.example.com", new Attributes());
        node.attr("testAttr", "testValue");
        assertEquals("testValue", node.attributes.get("testAttr"));
        assertEquals("testValue", node.attr("testAttr"));
    }
    
    @Test
    public void testHasAttr() {
        Node node = new Node("https://www.example.com", new Attributes());
        node.attr("testAttr", "testValue");
        assertTrue(node.hasAttr("testAttr"));
        assertFalse(node.hasAttr("nonexistentAttr"));
    }
    
    @Test
    public void testRemoveAttr() {
        Node node = new Node("https://www.example.com", new Attributes());
        node.attr("testAttr", "testValue");
        node.removeAttr("testAttr");
        assertFalse(node.hasAttr("testAttr"));
    }
    
    @Test
    public void testBaseUri() {
        Node node = new Node("https://www.example.com", new Attributes());
        assertEquals("https://www.example.com", node.baseUri());
        
        node.setBaseUri("https://www.test.com");
        assertEquals("https://www.test.com", node.baseUri());
    }
    
    // Add more test cases for other methods
    
}