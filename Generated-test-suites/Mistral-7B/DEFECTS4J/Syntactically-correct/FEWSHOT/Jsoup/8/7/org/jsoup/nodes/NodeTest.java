package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    private Node node;

    @Before
    public void setup() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        node = new Node(baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        assertEquals("Abstract Node", node.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("", node.attr(""));
        assertEquals("test", node.attr("class"));
        assertEquals("http://example.com", node.attr("abs:baseUri"));
    }

    @Test
    public void testAttributes() {
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testAttrSet() {
        node.attr("newAttribute", "newValue");
        assertEquals("newValue", node.attr("newAttribute"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistent"));
    }

    @Test
    public void testRemoveAttr() {
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("http://example.com", node.baseUri());
        String newBaseUri = "http://newexample.com";
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String attributeUrl = "http://sub.example.com";
        node.attr("abs:href", attributeUrl);
        assertEquals(attributeUrl, node.absUrl("href"));
    }

    @Test
    public void testAbsUrlMissing() {
        assertEquals("", node.absUrl("nonexistent"));
    }

    @Test
    public void testChildNode() {
        Node child = new Node();
        node.addChild(child);
        assertEquals(child, node.childNode(0));
    }

    @Test
    public void testChildren() {
        Node child1 = new Node();
        Node child2 = new Node();
        node.addChildren(child1, child2);
        List<Node> children = node.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testParent() {
        Node parent = new Node();
        parent.addChild(node);
        assertEquals(parent, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document();
        document.appendChild(node);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Node();
        parent.appendChild(node);
        node.remove();
        assertNull(parent.childNode(0));
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Node();
        Node newNode = new Node();
        parent.appendChild(node);
        parent.replaceChild(node, newNode);
        assertEquals(newNode, parent.childNode(0));
    }

    @Test
    public void testSetParentNode() {
        Node parent = new Node();
        Node child = new Node();
        child.setParentNode(parent);
        assertEquals(parent, child.parentNode);
    }

    @Test
    public void testReplaceChild() {
        Node parent = new Node();
        Node oldChild = new Node();
        Node newChild = new Node();
        parent.addChild(oldChild);
        parent.replaceChild(oldChild, newChild);
        assertEquals(newChild, parent.childNode(0));
    }

    @Test
    public void testRemoveChild() {
        Node parent = new Node();
        Node child = new Node();
        parent.addChild(child);
        parent.removeChild(child);
        assertNull(parent.childNode(0));
    }

    @Test
    public void testAddChildren() {
        Node parent = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        parent.addChildren(child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node parent = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        parent.addChildren(1, child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0)); // index 0 is actually child1
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testReindexChildren() {
        Node parent = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        parent.addChildren(child1, child2);
        parent.reindexChildren();
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testSiblingNodes() {
        Node parent = new Node();
        Node sibling1 = new Node();
        Node node = new Node();
        Node sibling2 = new Node();
        parent.addChildren(sibling1, node, sibling2);
        List<Node> siblings = node.siblingNodes();
        assertEquals(2, siblings.size());
        assertEquals(sibling1, siblings.get(0));
        assertEquals(sibling2, siblings.get(1));
    }

    @Test
    public void testNextSibling() {
        Node parent = new Node();
        Node prevSibling = new Node();
        Node node = new Node();
        Node nextSibling = new Node();
        parent.addChildren(prevSibling, node, nextSibling);
        assertEquals(nextSibling, node.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parent = new Node();
        Node nextSibling = new Node();
        Node node = new Node();
        Node prevSibling = new Node();
        parent.addChildren(nextSibling, node, prevSibling);
        assertEquals(prevSibling, node.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        Node parent = new Node();
        Node prevSibling = new Node();
        Node node = new Node();
        Node nextSibling = new Node();
        parent.addChildren(prevSibling, node, nextSibling);
        assertEquals(1, node.siblingIndex());
    }
}