import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        // Test Node constructor with baseUri and attributes
        Node node = new Element("http://example.com", new Attributes());
        assertEquals("http://example.com", node.baseUri());
        assertNotNull(node.attributes());
    }

    @Test
    public void testNodeConstructorWithBaseUri() {
        // Test Node constructor with baseUri
        Node node = new Element("http://example.com");
        assertEquals("http://example.com", node.baseUri());
        assertNotNull(node.attributes());
    }

    @Test
    public void testNodeDefaultConstructor() {
        // Test Node default constructor
        Node node = new Element();
        assertEquals("", node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeAttr() {
        // Test attr method
        Node node = new Element();
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testNodeHasAttr() {
        // Test hasAttr method
        Node node = new Element();
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertFalse(node.hasAttr("nonExistentKey"));
    }

    @Test
    public void testNodeRemoveAttr() {
        // Test removeAttr method
        Node node = new Element();
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    // Test more methods similarly including edge cases and error scenarios

}