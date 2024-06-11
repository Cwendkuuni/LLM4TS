import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        Node node = new Element("http://example.com", new Attributes());
        assertEquals("http://example.com", node.baseUri());
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testNodeConstructorWithBaseUri() {
        Node node = new Element("http://example.com");
        assertEquals("http://example.com", node.baseUri());
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Element();
        assertNotNull(node);
    }

    @Test
    public void testNodeNodeName() {
        Node node = new Element();
        assertNotNull(node.nodeName());
    }

    @Test
    public void testNodeAttr() {
        Node node = new Element("http://example.com", new Attributes());
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testNodeRemoveAttr() {
        Node node = new Element("http://example.com", new Attributes());
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testNodeSiblingNodes() {
        Node parent = new Element("http://example.com", new Attributes());
        Node child = new Element("http://example.com", new Attributes());
        parent.addChild(child);
        assertTrue(parent.siblingNodes().contains(child));
    }

    // Add more test methods for the remaining public methods

}