import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        // Test constructor with baseUri and attributes
        Attributes attributes = new Attributes();
        Node node = new Element("baseUri", attributes);
        
        assertNotNull(node);
        assertEquals("baseUri", node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithBaseUri() {
        // Test constructor with baseUri only
        Node node = new Element("baseUri");

        assertNotNull(node);
        assertEquals("baseUri", node.baseUri());
    }

    @Test
    public void testNodeAttributes() {
        // Test adding and getting attributes
        Node node = new Element("baseUri");

        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
        
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    // Add more test cases for other methods as needed

}