import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeName() {
        // Create a node with a name
        Node node = new Node("http://example.com");
        node.nodeName();

        // Test that the node name is not null
        assertNotNull(node.nodeName());

        // Test that the node name is correct
        assertEquals("Node", node.nodeName());
    }

    @Test
    public void testAttr() {
        // Create a node with attributes
        Node node = new Node("http://example.com");
        node.attr("key", "value");

        // Test that the attribute value is correct
        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testAttributes() {
        // Create a node with attributes
        Node node = new Node("http://example.com");
        node.attr("key1", "value1");
        node.attr("key2", "value2");

        // Get the attributes of the node
        Attributes attributes = node.attributes();

        // Test that the attributes are not null
        assertNotNull(attributes);

        // Test that the attributes size is correct
        assertEquals(2, attributes.size());

        // Test that the attributes contain the correct key-value pairs
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));

        assertTrue(attributes.hasKey("key2"));
        assertEquals("value2", attributes.get("key2"));
    }

    // Add more test methods for the other methods in the Node class
}