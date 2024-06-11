package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeWithBaseUriAndAttributes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals("test", node.attr("id"));
        Assert.assertNull(node.attr("nonExistentAttribute"));
        Assert.assertEquals(Collections.singletonList(node), node.childNodes());
    }

    @Test
    public void testNodeWithoutBaseUriAndAttributes() {
        Node node = new Node();

        Assert.assertNull(node.baseUri());
        Assert.assertNull(node.attr("id"));
        Assert.assertEquals(Collections.emptyList(), node.childNodes());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "https://example.com/index.html";
        Node node = new Node(baseUri);
        node.attr("href", "relative.html");

        Assert.assertEquals("https://example.com/relative.html", node.absUrl("href"));
        Assert.assertEquals("", node.absUrl("nonExistentAttribute"));
    }

    @Test
    public void testChildNode() {
        Node parentNode = new Node();
        Node childNode = new Node();
        parentNode.addChild(childNode);

        Assert.assertEquals(childNode, parentNode.childNode(0));
        Assert.assertEquals(Collections.singletonList(childNode), parentNode.childNodes());
    }

    @Test
    public void testChildNodesAsArray() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        parentNode.addChildren(childNode1, childNode2);

        Node[] childNodes = parentNode.childNodesAsArray();
        Assert.assertArrayEquals(new Node[]{childNode1, childNode2}, childNodes);
    }

    @Test
    public void testParentNode() {
        Node childNode = new Node();
        Node parentNode = new Node();
        childNode.setParentNode(parentNode);

        Assert.assertEquals(parentNode, childNode.parent());
        Assert.assertNull(childNode.parent());
    }

    @Test
    public void testRemoveChild() {
        Node parentNode = new Node();
        Node childNode = new Node();
        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);

        Assert.assertNull(parentNode.childNode(0));
        Assert.assertEquals(Collections.emptyList(), parentNode.childNodes());
    }

    @Test
    public void testReplaceChild() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        parentNode.addChild(childNode1);
        parentNode.replaceChild(childNode1, childNode2);

        Assert.assertEquals(childNode2, parentNode.childNode(0));
        Assert.assertEquals(Collections.singletonList(childNode2), parentNode.childNodes());
    }

    @Test
    public void testAddChildren() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        parentNode.addChildren(childNode1, childNode2);

        Assert.assertEquals(Collections.singletonList(childNode1), parentNode.childNodes());
        Assert.assertEquals(Collections.singletonList(childNode2), parentNode.childNodes());
    }

    @Test
    public void testGetSiblingNodes() {
        Node parentNode = new Node();
        Node siblingNode1 = new Node();
        Node siblingNode2 = new Node();
        Node testNode = new Node();

        parentNode.addChildren(siblingNode1, testNode, siblingNode2);

        List<Node> siblingNodes = testNode.siblingNodes();

        Assert.assertEquals(Collections.singletonList(siblingNode1), siblingNodes);
        Assert.assertEquals(Collections.singletonList(siblingNode2), siblingNodes);
    }

    @Test
    public void testNextSibling() {
        Node parentNode = new Node();
        Node siblingNode1 = new Node();
        Node testNode = new Node();
        Node siblingNode2 = new Node();

        parentNode.addChildren(siblingNode1, testNode, siblingNode2);

        Assert.assertEquals(siblingNode2, testNode.nextSibling());
        Assert.assertNull(testNode.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parentNode = new Node();
        Node siblingNode1 = new Node();
        Node testNode = new Node();
        Node siblingNode2 = new Node();

        parentNode.addChildren(siblingNode1, testNode, siblingNode2);

        Assert.assertEquals(siblingNode1, testNode.previousSibling());
        Assert.assertNull(testNode.previousSibling());
    }
}