import static org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        // Test constructor with baseUri and attributes
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new ElementNode(baseUri, attributes);
        
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithBaseUri() {
        // Test constructor with baseUri only
        String baseUri = "http://example.com";
        Node node = new ElementNode(baseUri);
        
        assertEquals(baseUri, node.baseUri());
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testNodeConstructorDefault() {
        // Test default constructor
        Node node = new ElementNode();
        
        assertNull(node.baseUri());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes());
    }

    // Add more test cases for other public methods in Node class

}