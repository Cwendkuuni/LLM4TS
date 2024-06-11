import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    // Sample test cases for the methods in the Node class

    @Test
    public void testNodeCreationWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element(baseUri, attributes);
        
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeCreationWithBaseUriOnly() {
        String baseUri = "http://example.com";
        Node node = new Element(baseUri);
        
        assertEquals(baseUri, node.baseUri());
        assertTrue(node.attributes().isEmpty());
    }

    // Add more test cases for other methods as per their functionality

    // Test cases for edge cases and exceptions

    @Test
    public void testInvalidAttributeKey() {
        Node node = new Element("http://example.com");

        // Testing for exception handling scenario
        assertThrows(IllegalArgumentException.class, () -> {
            node.attr(null, "value");
        });
    }

    // Add more test cases for edge cases and exceptions

}