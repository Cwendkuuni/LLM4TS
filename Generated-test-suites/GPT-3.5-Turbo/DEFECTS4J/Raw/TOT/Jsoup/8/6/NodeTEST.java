
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testAttr() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        Node node = new DummyNode("http://example.com", attributes);

        // Test for existing attribute
        assertEquals("value1", node.attr("key1"));
        assertEquals("value2", node.attr("key2"));
        assertEquals("value3", node.attr("key3"));

        // Test for non-existing attribute
        assertEquals("", node.attr("key4"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        Node node = new DummyNode("http://example.com", attributes);

        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testHasAttr() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        Node node = new DummyNode("http://example.com", attributes);

        // Test for existing attribute
        assertTrue(node.hasAttr("key1"));
        assertTrue(node.hasAttr("key2"));
        assertTrue(node.hasAttr("key3"));

        // Test for non-existing attribute
        assertFalse(node.hasAttr("key4"));
    }

    @Test
    public void testRemoveAttr() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        Node node = new DummyNode("http://example.com", attributes);

        // Remove existing attribute
        node.removeAttr("key1");
        assertFalse(node.hasAttr("key1"));

        // Remove non-existing attribute
        node.removeAttr("key4");
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";

        Node node = new DummyNode(baseUri);
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri1 = "http://example.com";
        String baseUri2 = "http://example.org";

        Node node = new DummyNode(baseUri1);
        assertEquals(baseUri1, node.baseUri());

        node.setBaseUri(baseUri2);
        assertEquals(baseUri2, node.baseUri());
    }

    @Test
    public void testChildNode() {
        Node child1 = new DummyNode();
        Node child2 = new DummyNode();
        Node child3 = new DummyNode();
        List<Node> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        children.add(child3);

        Node node = new DummyNode();
        node.setChildNodes(children);

        assertEquals(child1, node.childNode(0));
        assertEquals(child2, node.childNode(1));
        assertEquals(child3, node.childNode(2));
    }

    @Test
    public void testChildNodes() {
        Node child1 = new DummyNode();
        Node child2 = new DummyNode();
        Node child3 = new DummyNode();
        List<Node> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        children.add(child3);

        Node node = new DummyNode();
        node.setChildNodes(children);

        assertEquals(children, node.childNodes());
    }

    @Test
    public void testParent() {
        Node parent = new DummyNode();
        Node child = new DummyNode();
        child.setParentNode(parent);

        assertEquals(parent, child.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document("");
        Element element = new Element("div");

        document.appendChild(element);
        assertEquals(document, element.ownerDocument());
    }

    private static class DummyNode extends Node {

        public DummyNode(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }

        public DummyNode(String baseUri) {
            super(baseUri);
        }

        public DummyNode() {
            super();
        }

        @Override
        public String nodeName() {
            return null;
        }

        @Override
        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {

        }

        @Override
        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {

        }
    }
}
