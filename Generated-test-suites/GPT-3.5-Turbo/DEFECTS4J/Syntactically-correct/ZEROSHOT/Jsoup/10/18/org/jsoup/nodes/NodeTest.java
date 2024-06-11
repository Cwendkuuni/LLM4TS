import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructorWithAttributes() {
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Node node = new NodeImpl(baseUri, attributes);

        assertNotNull(node);
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithoutAttributes() {
        String baseUri = "https://www.example.com";

        Node node = new NodeImpl(baseUri);

        assertNotNull(node);
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testAttr() {
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Node node = new NodeImpl(baseUri, attributes);

        assertEquals("value1", node.attr("key1"));
        assertEquals("value2", node.attr("key2"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Node node = new NodeImpl(baseUri, attributes);

        assertTrue(node.hasAttr("key1"));
        assertTrue(node.hasAttr("key2"));
        assertFalse(node.hasAttr("key3"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Node node = new NodeImpl(baseUri, attributes);

        assertTrue(node.hasAttr("key1"));
        node.removeAttr("key1");
        assertFalse(node.hasAttr("key1"));
    }

    private static class NodeImpl extends Node {
        public NodeImpl(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }

        public NodeImpl(String baseUri) {
            super(baseUri);
        }

        public String nodeName() {
            return null;
        }

        public void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {

        }

        public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {

        }
    }
}