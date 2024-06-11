import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreationWithBaseUriAndAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Node node = new MockNode("baseUri", attributes);

        assertNotNull(node);
        assertEquals("baseUri", node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeCreationWithBaseUriOnly() {
        Node node = new MockNode("baseUri");

        assertNotNull(node);
        assertEquals("baseUri", node.baseUri());
        assertNotNull(node.attributes());
        assertTrue(node.attributes().isEmpty());
    }

    // Additional test cases for other methods can be added here

}

class MockNode extends Node {

    MockNode(String baseUri, Attributes attributes) {
        super(baseUri, attributes);
    }

    MockNode(String baseUri) {
        super(baseUri);
    }

    @Override
    public String nodeName() {
        return "MockNode";
    }
}