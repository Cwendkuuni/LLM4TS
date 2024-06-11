package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    private static final String BASE_URI = "http://www.example.com";
    private static final String SUB_URI = "http://www.example.com/sub";

    private Node node;

    @Before
    public void setUp() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        node = new Node(BASE_URI, attributes);
    }

    @Test
    public void testNodeName() {
        fail("Method 'nodeName()' must be implemented in subclass");
    }

    @Test
    public void testConstructorWithBaseUriAndAttributes() {
        assertEquals(BASE_URI, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testConstructorWithBaseUri() {
        Node nodeWithEmptyAttributes = new Node(BASE_URI);
        assertEquals(BASE_URI, nodeWithEmptyAttributes.baseUri());
        assertNull(nodeWithEmptyAttributes.attributes());
    }

    @Test
    public void testDefaultConstructor() {
        Node nodeWithNullAttributes = new Node();
        assertNull(nodeWithNullAttributes.baseUri());
        assertNull(nodeWithNullAttributes.attributes());
        assertEquals(Collections.EMPTY_LIST, nodeWithNullAttributes.childNodes());
    }

    @Test
    public void testGetAttribute() {
        assertEquals("value", node.attr("test"));
        assertEquals(BASE_URI, node.absUrl("test"));
        assertNull(node.absUrl("nonexistent"));
    }

    @Test
    public void testGetAllAttributes() {
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testSetAttribute() {
        node.attr("test2", "value2");
        assertEquals(Arrays.asList("test", "test2"), node.attributes().keySet());
        assertEquals("value2", node.attr("test2"));
    }

    @Test
    public void testHasAttribute() {
        assertTrue(node.hasAttr("test"));
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testRemoveAttribute() {
        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));
    }

    @Test
    public void testAbsUrlWithAbsoluteUrlAttribute() throws MalformedURLException {
        URL baseUrl = new URL(BASE_URI);
        node.attr("test", baseUrl.toExternalForm());
        URL absUrl = node.absUrl("test");
        assertEquals(BASE_URI, absUrl.toString());
    }

    @Test
    public void testAbsUrlWithRelativeUrlAttribute() throws MalformedURLException {
        node.attr("test", "sub");
        URL absUrl = node.absUrl("test");
        assertEquals(new URL(BASE_URI, "/sub"), absUrl);
    }

    @Test
    public void testAbsUrlWithMissingAttribute() throws MalformedURLException {
        URL absUrl = node.absUrl("test");
        assertNull(absUrl);
    }

    @Test
    public void testGetChildNodeByIndex() {
        Node child1 = new Node(BASE_URI, new Attributes());
        Node child2 = new Node(BASE_URI, new Attributes());
        node.addChildren(child1, child2);
        assertEquals(child1, node.childNode(0));
        assertEquals(child2, node.childNode(1));
    }

    @Test
    public void testGetChildrenList() {
        Node child1 = new Node(BASE_URI, new Attributes());
        Node child2 = new Node(BASE_URI, new Attributes());
        node.addChildren(child1, child2);
        List<Node> children = node.childNodes();
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testGetChildrenArray() {
        Node child1 = new Node(BASE_URI, new Attributes());
        Node child2 = new Node(BASE_URI, new Attributes());
        node.addChildren(child1, child2);
        Node[] children = node.childNodesAsArray();
        assertEquals(child1, children[0]);
        assertEquals(child2, children[1]);
    }

    @Test
    public void testGetParentNode() {
        Node parent = new Node(BASE_URI);
        parent.addChild(node);
        assertEquals(parent, node.parent());
        assertNull(node.parent());
    }

    @Test
    public void testGetOwnerDocument() {
        Document doc = new Document(BASE_URI);
        doc.appendChild(node);
        assertEquals(doc, node.ownerDocument());
        assertNull(node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Node(BASE_URI);
        parent.appendChild(node);
        parent.removeChild(node);
        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Node(BASE_URI);
        Node nodeToReplace = new Node(BASE_URI);
        parent.appendChild(node);
        Node newNode = new Node(BASE_URI);
        parent.replaceChild(node, newNode);
        assertEquals(newNode, parent.childNode(0));
        assertEquals(node, nodeToReplace.parent());
        assertEquals(newNode, nodeToReplace.parent().childNode(1));
    }

    @Test
    public void testSetParentNode() {
        Node parent = new Node(BASE_URI);
        Node child = new Node(BASE_URI);
        child.setParentNode(parent);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testReplaceChild() {
        Node parent = new Node(BASE_URI);
        Node oldChild = new Node(BASE_URI);
        Node newChild = new Node(BASE_URI);
        parent.addChild(oldChild);
        parent.replaceChild(oldChild, newChild);
        assertEquals(newChild, parent.childNode(0));
        assertEquals(oldChild, oldChild.parent());
        assertNull(oldChild.parent());
    }

    @Test
    public void testRemoveChild() {
        Node parent = new Node(BASE_URI);
        Node childToRemove = new Node(BASE_URI);
        Node remainingChild = new Node(BASE_URI);
        parent.addChildren(childToRemove, remainingChild);
        parent.removeChild(childToRemove);
        assertEquals(1, parent.childNodes().size());
        assertEquals(remainingChild, parent.childNode(0));
        assertNull(childToRemove.parent());
    }

    @Test
    public void testAddChildren() {
        Node parent = new Node(BASE_URI);
        Node child1 = new Node(BASE_URI);
        Node child2 = new Node(BASE_URI);
        parent.addChildren(child1, child2);
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node parent = new Node(BASE_URI);
        Node child1 = new Node(BASE_URI);
        Node child2 = new Node(BASE_URI);
        parent.addChildren(1, child1, child2);
        assertEquals(child1, parent.childNode(1));
        assertEquals(child2, parent.childNode(2));
    }

    @Test
    public void testGetSiblingNodes() {
        Node parent = new Node(BASE_URI);
        Node node1 = new Node(BASE_URI);
        Node node2 = new Node(BASE_URI);
        parent.addChildren(node1, node2);
        List<Node> siblings = node2.siblingNodes();
        assertEquals(node1, siblings.get(0));
        assertEquals(node2, siblings.get(1));
    }

    @Test
    public void testGetNextSibling() {
        Node parent = new Node(BASE_URI);
        Node node1 = new Node(BASE_URI);
        Node node2 = new Node(BASE_URI);
        parent.addChildren(node1, node2);
        Node nextSibling = node2.nextSibling();
        assertEquals(node1, nextSibling);
    }

    @Test
    public void testGetPreviousSibling() {
        Node parent = new Node(BASE_URI);
        Node node1 = new Node(BASE_URI);
        Node node2 = new Node(BASE_URI);
        parent.addChildren(node1, node2);
        Node previousSibling = node1.previousSibling();
        assertNull(previousSibling);
    }

    @Test
    public void testGetSiblingIndex() {
        Node parent = new Node(BASE_URI);
        Node node1 = new Node(BASE_URI);
        Node node2 = new Node(BASE_URI);
        parent.addChildren(node1, node2);
        assertEquals(0, node1.siblingIndex());
        assertEquals(1, node2.siblingIndex());
    }

    @Test
    public void testSetSiblingIndex() {
        Node parent = new Node(BASE_URI);
        Node node1 = new Node(BASE_URI);
        Node node2 = new Node(BASE_URI);
        parent.addChildren(node1, node2);
        node1.setSiblingIndex(2);
        node2.setSiblingIndex(0);
        List<Node> siblings = parent.childNodes();
        assertEquals(node2, siblings.get(0));
        assertEquals(node1, siblings.get(2));
    }

    @Test
    public void testOuterHtml() {
        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        String expected = "<node name=\"\" test=\"value\" test2=\"value2\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testClone() {
        Node clonedNode = node.clone();
        assertEquals(node, clonedNode.parent());
        assertFalse(node == clonedNode);
        assertEquals(node.attributes(), clonedNode.attributes());
        assertEquals( Arrays.asList(child1, child2), Arrays.asList(clonedNode.childNodes()));
    }

    @Test
    public void testCloneWithParent() {
        Node parent = new Node(BASE_URI);
        Node nodeToClone = new Node(BASE_URI);
        parent.addChild(nodeToClone);
        Node clonedNode = nodeToClone.clone(parent);
        assertEquals(parent, clonedNode.parent());
        assertFalse(nodeToClone == clonedNode);
        assertEquals(nodeToClone.attributes(), clonedNode.attributes());
        assertEquals( Arrays.asList(nodeToClone.childNodes()), Arrays.asList(clonedNode.childNodes()));
    }
}