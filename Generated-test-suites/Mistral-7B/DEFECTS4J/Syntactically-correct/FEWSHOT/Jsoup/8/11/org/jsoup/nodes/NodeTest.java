package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node1 = new Node(baseUri, attributes);
        assertEquals("test", node1.nodeName());
        assertEquals(baseUri, node1.baseUri());
        assertNull(node1.parentNode());
        assertEquals(0, node1.siblingIndex());
        assertNull(node1.attributes());
        assertEquals("", node1.attr("nonexistent"));
        assertEquals("test", node1.attr("class"));
    }

    @Test
    public void testNodeWithChildren() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parent.addChildren(child1, child2);

        assertEquals("", parent.nodeName());
        assertEquals(baseUri, parent.baseUri());
        assertEquals(2, parent.childNodes().size());
        assertSame(child1, parent.childNode(0));
        assertSame(child2, parent.childNode(1));

        assertEquals(parent, child1.parent());
        assertEquals(parent, child2.parent());
        assertEquals(0, child1.siblingIndex());
        assertEquals(1, child2.siblingIndex());
        assertEquals(Arrays.asList(child1, child2), parent.childNodesAsArray());
    }

    @Test
    public void testNodeWithAttributes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test1");
        attributes.put("id", "test2");

        Node node = new Node(baseUri, attributes);
        assertEquals("test1", node.nodeName());
        assertEquals("test1 test2", node.toString());
        assertEquals("test1", node.attr("class"));
        assertEquals("test2", node.attr("id"));
        assertEquals("test2", node.absUrl("id"));
    }

    @Test
    public void testNodeWithBaseUri() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node node = new Node(baseUri, attributes);
        String childUri = "relative.html";
        Node child = new Node(childUri, attributes);
        node.addChild(child);

        assertEquals(childUri, child.absUrl(""));
        assertEquals(baseUri + "/relative.html", child.absUrl(""));
        assertEquals(baseUri, child.baseUri());
    }

    @Test
    public void testNodeWithNestedAttributes() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes();
        parentAttributes.put("class", "parent");

        Attributes childAttributes = new Attributes();
        childAttributes.put("class", "child");

        Node parent = new Node(baseUri, parentAttributes);
        Node child = new Node(baseUri, childAttributes);
        parent.addChild(child);

        assertEquals("parent", parent.nodeName());
        assertEquals("parent child", parent.toString());
        assertEquals("parent", parent.attr("class"));

        assertEquals("child", child.nodeName());
        assertEquals("child", child.attr("class"));
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parent.addChildren(child1, child2);

        parent.removeChild(child1);
        assertEquals(1, parent.childNodes().size());
        assertSame(child2, parent.childNode(0));
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parent.addChildren(child1, child2);

        Node newChild = new Element("div");
        parent.replaceChild(child1, newChild);
        assertEquals(2, parent.childNodes().size());
        assertSame(newChild, parent.childNode(0));
        assertSame(child2, parent.childNode(1));
    }

    @Test
    public void testGetSiblingNodes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div");
        Node sibling1 = new Node(baseUri, attributes);
        Node current = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);
        parent.addChildren(sibling1, current, sibling2);

        List<Node> siblings = current.siblingNodes();
        assertEquals(2, siblings.size());
        assertEquals(sibling1, siblings.get(0));
        assertEquals(sibling2, siblings.get(1));
    }

    @Test
    public void testGetPreviousAndNextSiblingNodes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div");
        Node prevSibling = new Node(baseUri, attributes);
        Node current = new Node(baseUri, attributes);
        Node nextSibling = new Node(baseUri, attributes);
        parent.addChildren(prevSibling, current, nextSibling);

        Node previous = current.previousSibling();
        assertSame(prevSibling, previous);

        Node next = current.nextSibling();
        assertSame(nextSibling, next);

        Node onlyChild = new Node(baseUri, attributes);
        parent.addChild(onlyChild);

        previous = onlyChild.previousSibling();
        assertNull(previous);

        next = onlyChild.nextSibling();
        assertNull(next);
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        Node parent = new Node(baseUri);

        child1.setParentNode(parent);
        child2.setParentNode(parent);
        parent.addChildren(child1, child2);

        assertEquals(parent, child1.parent());
        assertEquals(parent, child2.parent());
    }

    @Test
    public void testSetSiblingIndex() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div");
        Node sibling1 = new Node(baseUri, attributes);
        Node current = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);
        parent.addChildren(sibling1, current, sibling2);

        List<Node> siblings = parent.childNodes();
        siblings.get(0).setSiblingIndex(1);
        siblings.get(1).setSiblingIndex(0);

        assertEquals(sibling2, siblings.get(0));
        assertEquals(sibling1, siblings.get(1));
    }

    @Test
    public void testNodeTraversal() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div");
        Node div1 = new Node(baseUri, attributes);
        Node div2 = new Node(baseUri, attributes);
        Node p1 = new Node(baseUri, attributes);
        Node p2 = new Node(baseUri, attributes);
        Node span1 = new TextNode("text1");
        Node span2 = new TextNode("text2");

        div1.addChildren(p1, span1);
        div2.addChildren(p2, span2);
        parent.addChildren(div1, div2);

        Elements textNodes = new Elements(parent).text();
        assertEquals(4, textNodes.size());
        assertEquals("text1", textNodes.get(0).toString());
        assertEquals("text2", textNodes.get(1).toString());

        StringBuilder accum = new StringBuilder(64);
        new NodeTraversor(new OuterHtmlVisitor(accum, null)).traverse(parent);
        String expected = "<div>\n<div>\n<p></p>\n<text>text1</text>\n</div>\n<div>\n<p></p>\n<text>text2</text>\n</div>\n</div>";
        assertEquals(expected, accum.toString());
    }

    private static class OuterHtmlVisitor implements NodeVisitor {
        private StringBuilder accum;
        private Document.OutputSettings out;

        OuterHtmlVisitor(StringBuilder accum, Document.OutputSettings out) {
            this.accum = accum;
            this.out = out;
        }

        @Override
        public void head(Node node, int depth) {
            node.outerHtmlHead(accum, depth, out);
        }

        @Override
        public void tail(Node node, int depth) {
            node.outerHtmlTail(accum, depth, out);
        }

        @Override
        public void visit(TextNode text) {
            accum.append(text.toString());
        }
    }
}