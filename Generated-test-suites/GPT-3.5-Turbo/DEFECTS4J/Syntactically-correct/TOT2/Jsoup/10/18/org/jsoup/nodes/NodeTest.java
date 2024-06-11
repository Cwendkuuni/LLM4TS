import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructorWithAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new ElementNode(baseUri, attributes);
        
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithoutAttributes() {
        String baseUri = "http://example.com";
        Node node = new ElementNode(baseUri);
        
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
    }

    @Test
    public void testNodeAttributeName() {
        Node node = new ElementNode("http://example.com");
        String nodeName = node.nodeName();
        
        assertNotNull(nodeName);
    }

    // Additional test cases for other methods in the Node class can be added here
    
}