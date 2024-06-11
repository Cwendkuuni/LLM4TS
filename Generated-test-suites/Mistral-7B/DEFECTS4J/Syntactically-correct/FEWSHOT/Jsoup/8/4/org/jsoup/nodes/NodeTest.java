package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    @Test
    public void testNewInstance() {
        Node node = new Node("", new Attributes());
        assertEquals("", node.nodeName());
        assertEquals("", node.baseUri());
        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testNewInstanceWithBaseUri() {
        Node node = new Node("", new Attributes(), "http://example.com");
        assertEquals("", node.nodeName());
        assertEquals("http://example.com", node.baseUri());
        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testNewInstanceEmpty() {
        Node node = new Node();
        assertEquals("", node.nodeName());
        assertNull(node.baseUri());
        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testAttributes() {
        Node node = new Node("", new Attributes().put("id", "foo"));
        assertEquals("foo", node.attr("id"));
        assertEquals(1, node.attributes().size());
        assertFalse(node.hasAttr("class"));
        node.attr("class", "bar");
        assertEquals("bar", node.attr("class"));
        assertEquals(2, node.attributes().size());
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testAbsUrl() {
        Node node = new Node("", new Attributes().put("href", "/path/to/file"));
        assertEquals("", node.absUrl(""));
        assertEquals("/path/to/file", node.absUrl("href"));
        assertEquals("http://example.com/path/to/file", node.absUrl("href", "http://example.com"));
        assertEquals("", node.absUrl("non-existent"));
        assertEquals("", node.absUrl("")); // baseuri is already absolute
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedUrl() {
        new Node("", new Attributes().put("href", "http://example.com/foo:bar"));
    }

    @Test
    public void testChildNodes() {
        Node parent = new Node("", new Attributes());
        Node child1 = new Node("child1", new Attributes());
        Node child2 = new Node("child2", new Attributes());

        parent.addChildren(child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));

        Node child3 = new Node("child3", new Attributes());
        parent.addChildren(child3);
        children = parent.childNodes();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));

        Node child4 = new Node("child4", new Attributes());
        parent.replaceChild(child1, child4);
        children = parent.childNodes();
        assertEquals(3, children.size());
        assertEquals(child4, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));

        parent.removeChild(child3);
        children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child4, children.get(0));
        assertEquals(child2, children.get(1));

        List<Node> empty = Collections.emptyList();
        assertEquals(empty, parent.childNodes());
        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testParent() {
        Node parent = new Node("parent", new Attributes());
        Node child1 = new Node("child1", new Attributes());
        Node child2 = new Node("child2", new Attributes());

        child1.setParentNode(parent);
        child2.setParentNode(parent);

        assertEquals(parent, child1.parent());
        assertEquals(parent, child2.parent());

        Node grandparent = new Node("grandparent", new Attributes());
        parent.setParentNode(grandparent);

        assertEquals(grandparent, child1.parent());
        assertEquals(grandparent, child2.parent());

        child1.remove();
        assertEquals(1, grandparent.childNodes().size());
        assertEquals(child2, grandparent.childNodes().get(0));
    }

    @Test
    public void testSiblingIndex() {
        Node parent = new Node("parent", new Attributes());
        Node sibling1 = new Node("sibling1", new Attributes());
        Node current = new Node("current", new Attributes());
        Node sibling2 = new Node("sibling2", new Attributes());

        parent.addChildren(sibling1, current, sibling2);
        assertEquals(0, current.siblingIndex());
        assertEquals(1, sibling1.siblingIndex());
        assertEquals(2, sibling2.siblingIndex());

        Node newSibling = new Node("newSibling", new Attributes());
        parent.addChildren(newSibling);
        assertEquals(1, current.siblingIndex());
        assertEquals(2, sibling1.siblingIndex());
        assertEquals(3, sibling2.siblingIndex());
        assertEquals(4, newSibling.siblingIndex());

        parent.removeChild(sibling2);
        assertEquals(0, current.siblingIndex());
        assertEquals(1, sibling1.siblingIndex());
        assertEquals(-1, sibling2.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        Node node = new Node("", new Attributes().put("id", "foo"));
        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        assertEquals("<html><body><node id=\"foo\"></node></body></html>", accum.toString());

        StringBuilder accum2 = new StringBuilder();
        node.outerHtmlHead(accum2, 0, new Document.OutputSettings());
        accum2.append("<html><body>");
        StringBuilder accum3 = new StringBuilder();
        node.outerHtmlTail(accum3, 0, new Document.OutputSettings());
        accum3.append("</node></body></html>");
        assertEquals(accum2.toString() + node.toString() + accum3.toString(), accum.toString());
    }
}