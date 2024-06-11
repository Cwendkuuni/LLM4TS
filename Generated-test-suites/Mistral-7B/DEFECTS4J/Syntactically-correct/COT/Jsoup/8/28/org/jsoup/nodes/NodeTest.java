package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.NodeTraversor;
import org.jsoup.nodes.NodeVisitor;
import org.junit.Test;

/**
 * Test class for Node.
 */
public class NodeTest {

    @Test
    public void testNodeName() {
        Node node = new MyNode("baseUri", new Attributes());
        assertEquals("MyNode", node.nodeName());
    }

    @Test
    public void testAttr() {
        Node node = new MyNode("baseUri", new Attributes().put("key", "value"));
        assertEquals("value", node.attr("key"));
        assertEquals("", node.attr("nonExistentKey"));
    }

    @Test
    public void testAttributes() {
        Node node = new MyNode("baseUri", new Attributes().put("key1", "value1").put("key2", "value2"));
        Attributes attributes = node.attributes();
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals(2, attributes.size());
        assertTrue(attributes.containsKey("key1"));
        assertTrue(attributes.containsKey("key2"));
    }

    @Test
    public void testHasAttr() {
        Node node = new MyNode("baseUri", new Attributes().put("key", "value"));
        assertTrue(node.hasAttr("key"));
        assertFalse(node.hasAttr("nonExistentKey"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new MyNode("baseUri", new Attributes().put("key", "value"));
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testBaseUri() {
        Node node = new MyNode("baseUri1", new Attributes());
        assertEquals("baseUri1", node.baseUri());
        node.setBaseUri("baseUri2");
        assertEquals("baseUri2", node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Node("", null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        new Node("baseUri", null);
    }

    @Test
    public void testAbsUrl() {
        Node node = new MyNode("baseUri", new Attributes().put("abs:href", "relativeUrl"));
        assertEquals("baseUri/relativeUrl", node.absUrl("abs:href"));
        assertEquals("", node.absUrl("nonExistentKey"));
        assertEquals("", node.absUrl("href")); // not prefixed
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedURL() {
        Node node = new MyNode("baseUri", new Attributes());
        String baseUri = node.baseUri();
        new URL(baseUri + "/relativeUrl");
        node.absUrl("relativeUrl");
    }

    @Test
    public void testChildNode() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node child = new MyNode("baseUri", new Attributes());
        parent.appendChild(child);
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testChildren() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node child1 = new MyNode("baseUri", new Attributes());
        Node child2 = new MyNode("baseUri", new Attributes());
        parent.appendChild(child1);
        parent.appendChild(child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testChildrenAsArray() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node child1 = new MyNode("baseUri", new Attributes());
        Node child2 = new MyNode("baseUri", new Attributes());
        parent.appendChild(child1);
        parent.appendChild(child2);
        Node[] children = parent.childNodesAsArray();
        assertEquals(2, children.length);
        assertEquals(child1, children[0]);
        assertEquals(child2, children[1]);
    }

    @Test
    public void testParent() {
        Node child = new MyNode("baseUri", new Attributes());
        Node parent = new MyNode("baseUri", new Attributes());
        child.setParentNode(parent);
        assertEquals(parent, child.parent());
        assertEquals(null, child.parent()); // no parent initially
    }

    @Test
    public void testOwnerDocument() {
        Node node = new MyNode("baseUri", new Attributes());
        if (node instanceof Document) {
            Document doc = (Document) node;
            assertEquals(node, doc.getDocumentElement());
        } else {
            assertEquals(null, node.ownerDocument());
        }
    }

    @Test
    public void testRemove() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node child = new MyNode("baseUri", new Attributes());
        parent.appendChild(child);
        parent.removeChild(child);
        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node oldChild = new MyNode("baseUri", new Attributes());
        Node newChild = new MyNode("baseUri", new Attributes());
        parent.appendChild(oldChild);
        parent.replaceWith(newChild, oldChild);
        assertEquals(newChild, parent.childNodes().get(0));
    }

    @Test
    public void testSetParentNode() {
        Node child = new MyNode("baseUri", new Attributes());
        Node parent = new MyNode("baseUri", new Attributes());
        child.setParentNode(parent);
        assertEquals(parent, child.parentNode());
    }

    @Test
    public void testReplaceChild() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node child1 = new MyNode("baseUri", new Attributes());
        Node child2 = new MyNode("baseUri", new Attributes());
        parent.appendChild(child1);
        parent.replaceChild(child2, child1);
        assertEquals(child2, parent.childNodes().get(0));
    }

    @Test
    public void testRemoveChild() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node child1 = new MyNode("baseUri", new Attributes());
        Node child2 = new MyNode("baseUri", new Attributes());
        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.removeChild(child1);
        assertEquals(1, parent.childNodes().size());
        assertEquals(child2, parent.childNodes().get(0));
    }

    @Test
    public void testAddChildren() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node child1 = new MyNode("baseUri", new Attributes());
        Node child2 = new MyNode("baseUri", new Attributes());
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
    }

    @Test
    public void testAddChildrenArray() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node child1 = new MyNode("baseUri", new Attributes());
        Node child2 = new MyNode("baseUri", new Attributes());
        parent.addChildren(new Node[] { child1, child2 });
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
    }

    @Test
    public void testAddChildrenList() {
        Node parent = new MyNode("baseUri", new Attributes());
        List<Node> children = new ArrayList<>();
        children.add(new MyNode("baseUri", new Attributes()));
        children.add(new MyNode("baseUri", new Attributes()));
        parent.addChildren(children);
        assertEquals(2, parent.childNodes().size());
        assertEquals(children.get(0), parent.childNodes().get(0));
        assertEquals(children.get(1), parent.childNodes().get(1));
    }

    @Test
    public void testSiblingNodes() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node sibling1 = new MyNode("baseUri", new Attributes());
        Node node = new MyNode("baseUri", new Attributes());
        Node sibling2 = new MyNode("baseUri", new Attributes());
        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);
        List<Node> siblings = parent.siblingNodes();
        assertEquals(3, siblings.size());
        assertEquals(sibling1, siblings.get(0));
        assertEquals(node, siblings.get(1));
        assertEquals(sibling2, siblings.get(2));
    }

    @Test
    public void testNextSibling() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node sibling1 = new MyNode("baseUri", new Attributes());
        Node node = new MyNode("baseUri", new Attributes());
        Node sibling2 = new MyNode("baseUri", new Attributes());
        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);
        assertEquals(sibling2, node.nextSibling());
        assertEquals(null, sibling1.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node sibling1 = new MyNode("baseUri", new Attributes());
        Node node = new MyNode("baseUri", new Attributes());
        Node sibling2 = new MyNode("baseUri", new Attributes());
        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);
        assertEquals(sibling1, node.previousSibling());
        assertEquals(null, sibling2.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node sibling1 = new MyNode("baseUri", new Attributes());
        Node node = new MyNode("baseUri", new Attributes());
        Node sibling2 = new MyNode("baseUri", new Attributes());
        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);
        assertEquals(1, node.siblingIndex());
    }

    @Test
    public void testSetSiblingIndex() {
        Node parent = new MyNode("baseUri", new Attributes());
        Node sibling1 = new MyNode("baseUri", new Attributes());
        Node node = new MyNode("baseUri", new Attributes());
        parent.appendChild(sibling1);
        parent.appendChild(node);
        node.setSiblingIndex(0);
        List<Node> siblings = parent.siblingNodes();
        assertEquals(node, siblings.get(0));
        assertEquals(sibling1, siblings.get(1));
    }

    @Test
    public void testOuterHtml() {
        Node node = new MyNode("baseUri", new Attributes());
        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        assertEquals("", accum.toString());
        node.setAttribute("outerHtml", "<div>Test</div>");
        accum.setLength(0);
        node.outerHtml(accum);
        assertEquals("<div>Test</div>", accum.toString());
    }

    @Test
    public void testOuterHtmlHead() {
        Node node = new MyNode("baseUri", new Attributes());
        StringBuilder accum = new StringBuilder();
        node.outerHtmlHead(accum, 0, null);
        assertEquals("", accum.toString());
        node.setAttribute("outerHtmlHead", "<div>Test</div>");
        accum.setLength(0);
        node.outerHtmlHead(accum, 0, null);
        assertEquals("<div>Test</div>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Node node = new MyNode("baseUri", new Attributes());
        StringBuilder accum = new StringBuilder();
        node.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
        node.setAttribute("outerHtmlTail", "<div>Test</div>");
        accum.setLength(0);
        node.outerHtmlTail(accum, 0, null);
        assertEquals("<div>Test</div>", accum.toString());
    }

    @Test
    public void testToString() {
        Node node = new MyNode("baseUri", new Attributes());
        assertEquals("", node.toString());
        node.setAttribute("toString", "<div>Test</div>");
        assertEquals("<div>Test</div>", node.toString());
    }

    @Test
    public void testEquals() {
        Node node1 = new MyNode("baseUri1", new Attributes());
        Node node2 = new MyNode("baseUri2", new Attributes());
        Node node3 = new MyNode("baseUri1", new Attributes());
        assertFalse(node1.equals(node2));
        assertTrue(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        Node node1 = new MyNode("baseUri1", new Attributes());
        Node node2 = new MyNode("baseUri2", new Attributes());
        Node node3 = new MyNode("baseUri1", new Attributes());
        int hashCode1 = node1.hashCode();
        assertEquals(hashCode1, node1.hashCode());
        assertEquals(hashCode1, node3.hashCode());
        int hashCode2 = node2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
    }

    private static class MyNode extends Node {
        MyNode(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }

        MyNode(String baseUri) {
            super(baseUri);
        }

        MyNode() {
            super();
        }

        @Override
        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
            accum.append("<div>outerHtmlHead</div>");
        }

        @Override
        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
            accum.append("<div>outerHtmlTail</div>");
        }
    }
}