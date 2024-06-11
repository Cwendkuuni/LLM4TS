package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    private Node node;
    private Document doc;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "test");
        attributes.put("class", "testClass");
        doc = new Document(baseUri, attributes);
        node = new Element("testElement", baseUri, attributes);
        doc.body().appendChild(node);
    }

    @Test
    public void testNodeName() {
        assertEquals("testElement", node.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("test", node.attr("id"));
        assertEquals("testClass", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttr"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = node.attributes();
        assertTrue(attributes.containsKey("id"));
        assertTrue(attributes.containsKey("class"));
        assertEquals("test", attributes.get("id"));
        assertEquals("testClass", attributes.get("class"));
        assertFalse(attributes.containsKey("nonExistentAttr"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testSetAttr() {
        node.attr("newAttr", "newValue");
        assertEquals("newValue", node.attr("newAttr"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("newAttr", "newValue");
        node.removeAttr("newAttr");
        assertEquals("", node.attr("newAttr"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(baseUri, node.baseUri());
        node.setBaseUri("http://newbase.com");
        assertEquals("http://newbase.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", node.absUrl("nonExistentAttr"));
        String href = "http://example.com/test.html";
        node.attr("href", href);
        assertEquals(href, node.absUrl("href"));
        assertEquals("http://example.com/test.html", node.absUrl("abs:href"));
        assertEquals("", node.absUrl("nonExistentAttr"));
    }

    @Test
    public void testChildNode() {
        Element child = new Element("child", baseUri);
        node.appendChild(child);
        Node actual = node.childNode(0);
        assertSame(child, actual);
    }

    @Test
    public void testChildren() {
        Element child1 = new Element("child1", baseUri);
        Element child2 = new Element("child2", baseUri);
        node.appendChild(child1);
        node.appendChild(child2);
        List<Node> children = node.childNodes();
        assertEquals(2, children.size());
        assertSame(child1, children.get(0));
        assertSame(child2, children.get(1));
    }

    @Test
    public void testChildNodesAsArray() {
        Element child1 = new Element("child1", baseUri);
        Element child2 = new Element("child2", baseUri);
        node.appendChild(child1);
        node.appendChild(child2);
        Node[] children = node.childNodesAsArray();
        assertEquals(2, children.length);
        assertSame(child1, children[0]);
        assertSame(child2, children[1]);
    }

    @Test
    public void testParent() {
        assertNull(node.parent());
        Element parent = new Element("parent", baseUri);
        parent.appendChild(node);
        Element child = new Element("child", baseUri);
        parent.appendChild(child);
        assertEquals(parent, node.parent());
        assertEquals(node, child.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertEquals(doc, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Element parent = new Element("parent", baseUri);
        parent.appendChild(node);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertSame(node, children.get(0));
        node.remove();
        assertEquals(0, children.size());
    }

    @Test
    public void testReplaceWith() {
        Element parent = new Element("parent", baseUri);
        Element existing = new Element("existing", baseUri);
        parent.appendChild(existing);
        parent.appendChild(node);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertSame(existing, children.get(0));
        assertSame(node, children.get(1));
        Node replacement = new Element("replacement", baseUri);
        node.replaceWith(replacement);
        assertEquals(1, children.size());
        assertSame(replacement, children.get(0));
    }

    @Test
    public void testSetParentNode() {
        Element parent1 = new Element("parent1", baseUri);
        Element parent2 = new Element("parent2", baseUri);
        node.setParentNode(parent1);
        assertEquals(parent1, node.parentNode());
        node.setParentNode(parent2);
        assertEquals(parent2, node.parentNode());
    }

    @Test
    public void testReplaceChild() {
        Element parent = new Element("parent", baseUri);
        Element child1 = new Element("child1", baseUri);
        Element child2 = new Element("child2", baseUri);
        parent.appendChild(child1);
        parent.appendChild(child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        int index = 0;
        node.replaceChild(children.get(index++), new TextNode("replacedText"));
        assertEquals(1, children.size());
        assertSame(new TextNode("replacedText"), children.get(0));
    }

    @Test
    public void testAddChildren() {
        Element parent = new Element("parent", baseUri);
        Node child1 = new TextNode("child1Text");
        Node child2 = new TextNode("child2Text");
        parent.addChildren(child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertSame(child1, children.get(0));
        assertSame(child2, children.get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Element parent = new Element("parent", baseUri);
        Node child1 = new TextNode("child1Text");
        Node child2 = new TextNode("child2Text");
        parent.addChildren(1, child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        Node firstChild = children.get(0);
        assertEquals(child1, firstChild);
        assertSame(child2, children.get(1));
    }

    @Test
    public void testSiblingIndex() {
        Element parent = new Element("parent", baseUri);
        Element sibling1 = new Element("sibling1", baseUri);
        Element sibling2 = new Element("sibling2", baseUri);
        Element thisNode = new Element("thisNode", baseUri);
        parent.appendChild(sibling1);
        parent.appendChild(thisNode);
        parent.appendChild(sibling2);
        assertEquals(-1, thisNode.siblingIndex());
        assertEquals(1, sibling1.siblingIndex());
        assertEquals(2, sibling2.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        StringBuilder expected = new StringBuilder();
        node.outerHtml(expected);
        StringBuilder actual = new StringBuilder();
        node.outerHtml();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder expected = new StringBuilder();
        node.outerHtmlHead(expected, 0, doc.outputSettings());
        StringBuilder actual = new StringBuilder();
        node.outerHtmlHead(actual, 0, doc.outputSettings());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder expected = new StringBuilder();
        node.outerHtmlTail(expected, 0, doc.outputSettings());
        StringBuilder actual = new StringBuilder();
        node.outerHtmlTail(actual, 0, doc.outputSettings());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        assertEquals(node.outerHtml(), node.toString());
    }

    @Test
    public void testEquals() {
        Node node2 = new Element("testElement", baseUri, attributes);
        assertTrue(node.equals(node2));
        node2.remove();
        assertFalse(node.equals(node2));
    }

    @Test
    public void testHashCode() {
        Node node2 = new Element("testElement", baseUri, attributes);
        int expected = node.hashCode();
        int actual = node2.hashCode();
        assertEquals(expected, actual);
    }
}