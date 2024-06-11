package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class NodeTest {

    @Test
    public void testNewNode() {
        Node node = new Node("baseUri", new Attributes());
        assertEquals("baseUri", node.nodeName());
        assertNull(node.parentNode);
        assertNull(node.childNodes);
        assertNull(node.attributes);
        assertEquals("baseUri", node.baseUri());
        assertEquals("", node.absUrl(""));
    }

    @Test
    public void testNewNodeWithAttributes() {
        Node node = new Node("baseUri", new Attributes().put("attr1", "value1").put("attr2", "value2"));
        assertEquals("baseUri", node.nodeName());
        assertNull(node.parentNode);
        assertNotNull(node.childNodes);
        assertNotNull(node.attributes);
        assertEquals("baseUri", node.baseUri());
        assertEquals("value1", node.attr("attr1"));
        assertEquals("value2", node.attr("attr2"));
        assertEquals("value1", node.absUrl("abs:attr1"));
    }

    @Test
    public void testNewNodeEmptyList() {
        Node node = new Node("baseUri");
        assertEquals("baseUri", node.nodeName());
        assertNull(node.parentNode);
        assertSame(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes);
        assertEquals("baseUri", node.baseUri());
        assertEquals("", node.absUrl(""));
    }

    @Test
    public void testNewNodeWithChildren() {
        Node parent = new Node("parent", new Attributes());
        Node child1 = new Node("child1", new Attributes());
        Node child2 = new Node("child2", new Attributes());
        parent.addChildren(child1, child2);
        assertEquals("parent", parent.nodeName());
        assertNull(parent.parentNode);
        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals("child1", childNodes.get(0).nodeName());
        assertEquals("child2", childNodes.get(1).nodeName());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testSetBaseUri() {
        Node node = new Node("baseUri", new Attributes());
        String baseUri = "http://example.com";
        node.setBaseUri(baseUri);
        assertEquals(baseUri, node.baseUri());
        assertEquals("http://example.com/foo", node.absUrl("foo"));
        assertEquals("http://example.com/bar", node.absUrl("bar"));
    }

    @Test
    public void testAbsUrlWithAbsoluteUrl() {
        Node node = new Node("baseUri", new Attributes());
        String baseUri = "http://example.com";
        node.setBaseUri(baseUri);
        assertEquals(baseUri, node.baseUri());
        assertEquals(baseUri, node.absUrl(""));
        assertEquals("http://example.com/path/to/file.html", node.absUrl("path/to/file.html"));
    }

    @Test
    public void testAbsUrlWithRelativeUrl() {
        Node node = new Node("baseUri", new Attributes());
        String baseUri = "http://example.com";
        node.setBaseUri(baseUri);
        assertEquals(baseUri, node.baseUri());
        assertEquals("http://example.com/path/to/file.html", node.absUrl("path/to/file.html"));
        assertEquals("http://example.com/foo", node.absUrl("foo"));
        assertEquals("http://example.com/foo/bar", node.absUrl("foo/bar"));
    }

    @Test
    public void testAbsUrlWithMissingAttribute() {
        Node node = new Node("baseUri", new Attributes());
        assertEquals("", node.absUrl(""));
        assertEquals("", node.absUrl("foo"));
        assertEquals("", node.absUrl("foo/bar"));
    }

    @Test
    public void testChildNodes() {
        Node parent = new Node("parent", new Attributes());
        Node child1 = new Node("child1", new Attributes());
        Node child2 = new Node("child2", new Attributes());
        parent.addChildren(child1, child2);
        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals("child1", childNodes.get(0).nodeName());
        assertEquals("child2", childNodes.get(1).nodeName());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
    }

    @Test
    public void testChildNodesAsArray() {
        Node parent = new Node("parent", new Attributes());
        Node child1 = new Node("child1", new Attributes());
        Node child2 = new Node("child2", new Attributes());
        parent.addChildren(child1, child2);
        Node[] childNodes = parent.childNodesAsArray();
        assertEquals(2, childNodes.length);
        assertEquals("child1", childNodes[0].nodeName());
        assertEquals("child2", childNodes[1].nodeName());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
        assertEquals(child1, childNodes[0]);
        assertEquals(child2, childNodes[1]);
    }

    @Test
    public void testRemoveChild() {
        Node parent = new Node("parent", new Attributes());
        Node child1 = new Node("child1", new Attributes());
        Node child2 = new Node("child2", new Attributes());
        parent.addChildren(child1, child2);
        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        parent.removeChild(child1);
        childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("child2", childNodes.get(0).nodeName());
        assertEquals(child2, parent.childNode(0));
    }

    @Test
    public void testReplaceChild() {
        Node parent = new Node("parent", new Attributes());
        Node child1 = new Node("child1", new Attributes());
        Node child2 = new Node("child2", new Attributes());
        Node newChild = new Node("newChild", new Attributes());
        parent.addChildren(child1, child2);
        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        parent.replaceChild(child1, newChild);
        childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals("newChild", childNodes.get(0).nodeName());
        assertEquals("child2", childNodes.get(1).nodeName());
        assertEquals(newChild, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testSetParentNode() {
        Node child = new Node("child", new Attributes());
        Node parent = new Node("parent", new Attributes());
        child.setParentNode(parent);
        assertEquals(parent, child.parentNode);
        parent.removeChild(child);
        assertEquals(null, child.parentNode);
    }

    @Test
    public void testRemove() {
        Node parent = new Node("parent", new Attributes());
        Node child = new Node("child", new Attributes());
        parent.addChildren(child);
        List<Node> childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        parent.remove();
        childNodes = parent.childNodes();
        assertEquals(0, childNodes.size());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Node("parent", new Attributes());
        Node oldChild = new Node("oldChild", new Attributes());
        Node newChild = new Node("newChild", new Attributes());
        parent.addChildren(oldChild);
        List<Node> childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        parent.replaceWith(newChild);
        childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("newChild", childNodes.get(0).nodeName());
        assertEquals(newChild, parent.childNode(0));
    }
}