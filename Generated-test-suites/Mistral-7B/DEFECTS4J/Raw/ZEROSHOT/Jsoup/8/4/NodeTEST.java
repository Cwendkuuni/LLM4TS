package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for Node.
 */
public class NodeTest {

    private Node simpleNode;
    private Node nodeWithChildren;
    private Node nodeWithAttributes;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        simpleNode = new Element("div", baseUri, attributes);

        List<Node> children = new ArrayList<>();
        children.add(new TextNode("Child 1"));
        children.add(simpleNode);
        nodeWithChildren = new Element("p", baseUri, attributes).children(children);

        attributes = new Attributes();
        attributes.put("id", "myId");
        attributes.put("title", "My Title");
        nodeWithAttributes = new Element("a", baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", simpleNode.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("test", simpleNode.attr("class"));
        assertEquals("http://example.com", simpleNode.baseUri());
        assertEquals("", simpleNode.attr("nonExistentAttr"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = simpleNode.attributes();
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("test", attributes.get("class"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(simpleNode.hasAttr("class"));
        assertFalse(simpleNode.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        simpleNode.removeAttr("class");
        assertTrue(simpleNode.attributes().isEmpty());
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", simpleNode.absUrl("nonExistentAttr"));
        assertEquals("http://example.com/test", simpleNode.absUrl("class"));
    }

    @Test
    public void testChildNode() {
        Node child = nodeWithChildren.childNode(0);
        assertEquals("Child 1", child.toString());
    }

    @Test
    public void testChildNodes() {
        List<Node> children = nodeWithChildren.childNodes();
        assertEquals(2, children.size());
        assertEquals(simpleNode, children.get(1));
    }

    @Test
    public void testChildNodesAsArray() {
        Node[] children = nodeWithChildren.childNodesAsArray();
        assertEquals(2, children.length);
        assertEquals(simpleNode, children[1]);
    }

    @Test
    public void testParent() {
        Node parent = nodeWithChildren.parent();
        assertNull(parent);
    }

    @Test
    public void testOwnerDocument() {
        Document doc = new Document();
        Node node = doc.body();
        assertEquals(doc, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        List<Node> children = new ArrayList<>();
        children.add(simpleNode);
        Node parent = new Element("div", "", new Attributes()).children(children);
        parent.removeChild(simpleNode);
        assertTrue(parent.childNodes().isEmpty());
    }

    @Test
    public void testReplaceChild() {
        List<Node> children = new ArrayList<>();
        children.add(simpleNode);
        Node parent = new Element("div", "", new Attributes()).children(children);
        Node newChild = new TextNode("New Child");
        parent.replaceChild(simpleNode, newChild);
        assertEquals(1, parent.childNodes().size());
        assertEquals("New Child", parent.childNodes().get(0).toString());
    }

    @Test
    public void testAddChildren() {
        Node parent = new Element("div", "", new Attributes());
        Node child1 = new TextNode("Child 1");
        Node child2 = new TextNode("Child 2");
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals("Child 1", parent.childNodes().get(0).toString());
        assertEquals("Child 2", parent.childNodes().get(1).toString());
    }

    @Test
    public void testAddChildrenInt() {
        Node parent = new Element("div", "", new Attributes());
        Node child1 = new TextNode("Child 1");
        Node child2 = new TextNode("Child 2");
        parent.addChildren(1, child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals("Child 1", parent.childNodes().get(1).toString());
        assertEquals("Child 2", parent.childNodes().get(2).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenNull() {
        Node parent = new Element("div", "", new Attributes());
        parent.addChildren((Node) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenNullElements() {
        Node parent = new Element("div", "", new Attributes());
        parent.addChildren((Node[]) null);
    }

    @Test
    public void testSiblingNodes() {
        List<Node> siblings = nodeWithChildren.siblingNodes();
        assertTrue(siblings.isEmpty());
    }

    @Test
    public void testNextSibling() {
        Node sibling = new TextNode("Sibling");
        Node parent = new Element("div", "", new Attributes()).children(simpleNode, sibling);
        assertNull(simpleNode.nextSibling());
        assertEquals(sibling, simpleNode.nextSibling());
        assertEquals(simpleNode, sibling.previousSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node sibling = new TextNode("Sibling");
        Node parent = new Element("div", "", new Attributes()).children(simpleNode, sibling);
        assertNull(simpleNode.previousSibling());
    }

    @Test
    public void testSetParentNode() {
        Node child = new TextNode("Child");
        Node parent = new Element("div", "", new Attributes());
        child.setParentNode(parent);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testReplaceChildWithSelf() {
        Node child = new TextNode("Child");
        Node parent = new Element("div", "", new Attributes());
        parent.replaceChild(child, child);
        assertEquals(child, parent.childNodes().get(0));
    }

    @Test
    public void testSetSiblingIndex() {
        Node sibling1 = new TextNode("Sibling 1");
        Node sibling2 = new TextNode("Sibling 2");
        Node parent = new Element("div", "", new Attributes()).children(simpleNode, sibling1, sibling2);
        simpleNode.setSiblingIndex(0);
        assertEquals(simpleNode, parent.childNodes().get(0));
        assertEquals(sibling1, parent.childNodes().get(1));
        assertEquals(sibling2, parent.childNodes().get(2));
    }

    @Test
    public void testOuterHtml() {
        StringBuilder expected = new StringBuilder();
        simpleNode.outerHtml(expected);
        assertEquals("<div></div>", expected.toString());

        expected.setLength(0);
        nodeWithChildren.outerHtml(expected);
        assertEquals("<p><text>Child 1</text><div class=\"test\"></div></p>", expected.toString());

        expected.setLength(0);
        nodeWithAttributes.outerHtml(expected);
        assertEquals("<a href=\"\" class=\"id myId title\"My Title\"</a>", expected.toString());
    }
}