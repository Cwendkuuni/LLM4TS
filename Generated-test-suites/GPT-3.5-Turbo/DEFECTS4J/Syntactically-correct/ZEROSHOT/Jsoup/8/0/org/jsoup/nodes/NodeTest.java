import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeName() {
        Node node = new CustomNode();
        assertEquals("Custom Node", node.nodeName());
    }

    @Test
    public void testAttr() {
        Node node = new CustomNode();
        node.attr("attributeKey", "attributeValue");
        assertTrue(node.hasAttr("attributeKey"));
        assertEquals("attributeValue", node.attr("attributeKey"));
    }

    @Test
    public void testAttributes() {
        Node node = new CustomNode();
        node.attr("attributeKey1", "attributeValue1");
        node.attr("attributeKey2", "attributeValue2");
        node.attr("attributeKey3", "attributeValue3");
        assertEquals(3, node.attributes().size());
        assertTrue(node.attributes().hasKey("attributeKey1"));
        assertTrue(node.attributes().hasKey("attributeKey2"));
        assertTrue(node.attributes().hasKey("attributeKey3"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new CustomNode();
        node.attr("attributeKey", "attributeValue");
        node.removeAttr("attributeKey");
        assertFalse(node.hasAttr("attributeKey"));
    }

    @Test
    public void testBaseUri() {
        Node node = new CustomNode();
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testChildNode() {
        Node node1 = new CustomNode();
        Node node2 = new CustomNode();
        node1.addChildren(node2);
        assertEquals(node2, node1.childNode(0));
    }

    @Test
    public void testChildNodes() {
        Node node1 = new CustomNode();
        Node node2 = new CustomNode();
        Node node3 = new CustomNode();
        node1.addChildren(node2, node3);
        assertEquals(Arrays.asList(node2, node3), node1.childNodes());
    }

    @Test
    public void testParent() {
        Node parentNode = new CustomNode();
        Node childNode = new CustomNode();
        parentNode.addChildren(childNode);
        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document();
        Node node = new CustomNode();
        document.addChildren(node);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parentNode = new CustomNode();
        Node childNode = new CustomNode();
        parentNode.addChildren(childNode);
        childNode.remove();
        assertFalse(parentNode.childNodes().contains(childNode));
    }

    @Test
    public void testReplaceWith() {
        Node parentNode = new CustomNode();
        Node childNode1 = new CustomNode();
        Node childNode2 = new CustomNode();
        parentNode.addChildren(childNode1);
        childNode1.replaceWith(childNode2);
        assertFalse(parentNode.childNodes().contains(childNode1));
        assertTrue(parentNode.childNodes().contains(childNode2));
    }

    @Test
    public void testSiblingNodes() {
        Node parentNode = new CustomNode();
        Node childNode1 = new CustomNode();
        Node childNode2 = new CustomNode();
        parentNode.addChildren(childNode1, childNode2);
        assertEquals(Arrays.asList(childNode1, childNode2), childNode1.siblingNodes());
    }

    @Test
    public void testNextSibling() {
        Node parentNode = new CustomNode();
        Node childNode1 = new CustomNode();
        Node childNode2 = new CustomNode();
        Node childNode3 = new CustomNode();
        parentNode.addChildren(childNode1, childNode2, childNode3);
        assertEquals(childNode2, childNode1.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parentNode = new CustomNode();
        Node childNode1 = new CustomNode();
        Node childNode2 = new CustomNode();
        Node childNode3 = new CustomNode();
        parentNode.addChildren(childNode1, childNode2, childNode3);
        assertEquals(childNode2, childNode3.previousSibling());
    }

    @Test
    public void testOuterHtml() {
        Node node = new CustomNode();
        assertEquals("<custom></custom>", node.outerHtml());
    }

    private static class CustomNode extends Node {

        public CustomNode(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }

        public CustomNode(String baseUri) {
            super(baseUri);
        }

        public CustomNode() {
            super();
        }

        @Override
        public String nodeName() {
            return "Custom Node";
        }

        @Override
        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
            accum.append("<custom>");
        }

        @Override
        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
            accum.append("</custom>");
        }
    }
}