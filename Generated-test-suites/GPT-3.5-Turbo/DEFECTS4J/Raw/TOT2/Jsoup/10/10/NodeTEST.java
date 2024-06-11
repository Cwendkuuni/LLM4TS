import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        Attributes attributes = new Attributes();
        Node node = new MockNode("http://example.com", attributes);
        
        assertEquals("http://example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithBaseUri() {
        Node node = new MockNode("http://example.com");
        
        assertEquals("http://example.com", node.baseUri());
        assertEquals(new Attributes(), node.attributes());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new MockNode();
        
        assertEquals("", node.baseUri());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeNodeName() {
        Node node = new MockNode();
        assertEquals("MockNode", node.nodeName());
    }

    @Test
    public void testNodeAttr() {
        Node node = new MockNode();
        node.attr("key", "value");
        
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }

    // Add more test cases for the remaining methods

}

class MockNode extends Node {
    MockNode(String baseUri, Attributes attributes) {
        super(baseUri, attributes);
    }

    MockNode(String baseUri) {
        super(baseUri);
    }

    MockNode() {
        super();
    }

    @Override
    public String nodeName() {
        return "MockNode";
    }

    @Override
    public void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {

    }

    @Override
    public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {

    }

    @Override
    void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {

    }

    @Override
    void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {

    }
}