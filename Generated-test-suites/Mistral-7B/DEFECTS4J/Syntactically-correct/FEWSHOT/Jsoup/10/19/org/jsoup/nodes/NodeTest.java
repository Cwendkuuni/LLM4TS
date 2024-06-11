package org.jsoup.nodes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class NodeTest {

    @Test
    public void testNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        Node node = new Node(baseUri, attributes);

        assertEquals("http://example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
        assertFalse(node.hasChildren());
        assertNull(node.parent());
        assertNull(node.ownerDocument());

        Node node2 = new Node(baseUri);
        assertEquals("", node2.baseUri());
        assertNull(node2.attributes());
        assertFalse(node2.hasChildren());
        assertNull(node2.parent());
        assertNull(node2.ownerDocument());

        Node node3 = new Node();
        assertEquals(Collections.EMPTY_LIST, node3.childNodes());
        assertNull(node3.attributes());
        assertNull(node3.baseUri());
        assertNull(node3.parent());
        assertNull(node3.ownerDocument());
    }

    @Test
    public void testAbstractMethods() {
        throw new UnsupportedOperationException("Abstract methods not testable directly.");
    }

    @Test
    public void testGetNodeName() {
        Node node = new Elements.SingleTagElement("div");
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        Node node = new Node(baseUri, attributes);

        assertEquals("foo", node.attr("class"));
        assertEquals("", node.attr("nonexistent"));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        Node node = new Node(baseUri, attributes);

        List<String> keys = new ArrayList<>();
        keys.add("class");
        keys.add("nonexistent");
        List<String> values = new ArrayList<>();
        values.add("foo");

        for (String key : keys) {
            assertEquals(values.get(keys.indexOf(key)), node.attr(key));
        }
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        Node node = new Node(baseUri, attributes);

        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        Node node = new Node(baseUri, attributes);

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com/";
        Node node = new Elements.SingleTagElement("a", new Attributes().put("href", "relative.html"));
        node.setBaseUri(baseUri);

        assertEquals("http://example.com/relative.html", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node parent = new Elements.SingleTagElement("div");
        Node child = new Elements.SingleTagElement("p");

        parent.appendChild(child);

        assertEquals(child, parent.childNode(0));
        assertEquals(0, parent.childNodes().indexOf(child));
    }

    @Test
    public void testChildren() {
        Node parent = new Elements.SingleTagElement("div");
        Node child1 = new Elements.SingleTagElement("p");
        Node child2 = new Elements.SingleTagElement("p");

        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> children = parent.childNodes();
        List<Node> expected = Arrays.asList(child1, child2);

        assertEquals(expected, children);
    }

    @Test
    public void testChildNodesAsArray() {
        Node parent = new Elements.SingleTagElement("div");
        Node child1 = new Elements.SingleTagElement("p");
        Node child2 = new Elements.SingleTagElement("p");

        parent.appendChild(child1);
        parent.appendChild(child2);

        Node[] children = parent.childNodesAsArray();

        assertArrayEquals(new Node[]{child1, child2}, children);
    }

    @Test
    public void testParent() {
        Node parent = new Elements.SingleTagElement("div");
        Node child = new Elements.SingleTagElement("p");
        parent.appendChild(child);

        assertEquals(parent, child.parent());
        assertSame(child, parent.childNode(0));
    }

    @Test
    public void testOwnerDocument() {
        Node node = new Elements.SingleTagElement("p");

        Document document = new Document();
        document.appendChild(node);

        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Elements.SingleTagElement("div");
        Node child = new Elements.SingleTagElement("p");
        parent.appendChild(child);

        parent.removeChild(child);

        List<Node> children = parent.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Elements.SingleTagElement("div");
        Node child1 = new Elements.SingleTagElement("p");
        Node child2 = new Elements.SingleTagElement("p");
        parent.appendChild(child1);

        parent.replaceChild(child1, child2);

        List<Node> children = parent.childNodes();
        assertEquals(child2, children.get(0));
    }

    @Test
    public void testSetParentNode() {
        Node parent1 = new Elements.SingleTagElement("div");
        Node child = new Elements.SingleTagElement("p");
        parent1.appendChild(child);

        Node parent2 = new Elements.SingleTagElement("div");
        child.setParentNode(parent2);

        assertEquals(parent2, child.parent());
        assertNull(child.parentNode);
        assertNull(parent1.childNode(0));
    }

    @Test
    public void testReplaceChild() {
        Node parent = new Elements.SingleTagElement("div");
        Node oldChild = new Elements.SingleTagElement("p");
        Node newChild = new Elements.SingleTagElement("p");

        parent.appendChild(oldChild);
        parent.replaceChild(oldChild, newChild);

        List<Node> children = parent.childNodes();
        assertEquals(newChild, children.get(0));
    }

    @Test
    public void testAddChildren() {
        Node parent = new Elements.SingleTagElement("div");
        Node child1 = new Elements.SingleTagElement("p");
        Node child2 = new Elements.SingleTagElement("p");

        parent.addChildren(child1, child2);

        List<Node> children = parent.childNodes();
        List<Node> expected = Arrays.asList(child1, child2);

        assertEquals(expected, children);
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node parent = new Elements.SingleTagElement("div");
        Node child1 = new Elements.SingleTagElement("p");
        Node child2 = new Elements.SingleTagElement("p");

        parent.addChildren(1, child1, child2);

        List<Node> children = parent.childNodes();
        List<Node> expected = Arrays.asList(child1, child2);

        assertEquals(expected, children);
    }

    @Test
    public void testHasChildren() {
        Node parent = new Elements.SingleTagElement("div");
        Node child1 = new Elements.SingleTagElement("p");
        Node child2 = new Elements.SingleTagElement("p");

        parent.appendChild(child1);
        parent.appendChild(child2);

        assertTrue(parent.hasChildren());
    }

    @Test
    public void testNextSibling() {
        Node parent = new Elements.SingleTagElement("div");
        Node child1 = new Elements.SingleTagElement("p");
        Node child2 = new Elements.SingleTagElement("p");
        parent.appendChild(child1);
        parent.appendChild(child2);

        assertEquals(child2, child1.nextSibling());
        assertEquals(null, child2.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parent = new Elements.SingleTagElement("div");
        Node child1 = new Elements.SingleTagElement("p");
        Node child2 = new Elements.SingleTagElement("p");
        parent.appendChild(child1);
        parent.appendChild(child2);

        assertEquals(child1, child2.previousSibling());
        assertEquals(null, child1.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        Node parent = new Elements.SingleTagElement("div");
        Node child1 = new Elements.SingleTagElement("p");
        Node child2 = new Elements.SingleTagElement("p");
        parent.appendChild(child1);
        parent.appendChild(child2);

        assertEquals(0, child1.siblingIndex());
        assertEquals(1, child2.siblingIndex());
    }

    @Test
    public void testSetSiblingIndex() {
        Node parent = new Elements.SingleTagElement("div");
        Node child1 = new Elements.SingleTagElement("p");
        Node child2 = new Elements.SingleTagElement("p");
        Node child3 = new Elements.SingleTagElement("p");
        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.appendChild(child3);

        child2.setSiblingIndex(1);

        List<Node> children = parent.childNodes();
        List<Node> expected = Arrays.asList(child1, child3, child2);

        assertEquals(expected, children);
    }

    @Test
    public void testOuterHtml() {
        Node node = new Elements.SingleTagElement("p", new Attributes().put("class", "foo"));
        StringBuilder out = new StringBuilder();
        node.outerHtml(out);

        assertEquals("<p class=\"foo\"></p>", out.toString());
    }

    @Test
    public void testOuterHtmlHead() {
        Node node = new Elements.SingleTagElement("p");

        StringBuilder out = new StringBuilder();
        node.outerHtmlHead(out, 0, new Document.OutputSettings().indentAmount(0));

        assertEquals("", out.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Node node = new Elements.SingleTagElement("p");

        StringBuilder out = new StringBuilder();
        node.outerHtmlTail(out, 0, new Document.OutputSettings().indentAmount(0));

        assertEquals("", out.toString());
    }
}