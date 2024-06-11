package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        Assert.assertEquals("test", node1.nodeName());
        Assert.assertEquals(baseUri, node1.baseUri());
        Assert.assertEquals(attributes, node1.attributes());

        Assert.assertNull(node2.nodeName());
        Assert.assertEquals(baseUri, node2.baseUri());
        Assert.assertNull(node2.attributes());

        Assert.assertNull(node3.nodeName());
        Assert.assertNull(node3.baseUri());
        Assert.assertNull(node3.attributes());
    }

    @Test
    public void testAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        attributes.put("href", "https://example.com/link");

        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("test", node.nodeName());
        Assert.assertEquals(baseUri, node.baseUri());

        Assert.assertEquals("test", node.attr("class"));
        Assert.assertEquals("https://example.com/link", node.absUrl("href"));

        Assert.assertEquals("", node.attr("non-existent-attr"));
        Assert.assertEquals("", node.attr("abs:non-existent-attr"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node = new Node(baseUri, attributes);

        Assert.assertTrue(node.hasAttr("class"));
        Assert.assertFalse(node.hasAttr("non-existent-attr"));
        Assert.assertFalse(node.hasAttr("abs:non-existent-attr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("test", node.attr("class"));
        Assert.assertEquals(1, node.attributes().size());

        node.removeAttr("class");

        Assert.assertEquals("", node.attr("class"));
        Assert.assertEquals(0, node.attributes().size());
    }

    @Test
    public void testChildNodes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        Node childNode3 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2, childNode3);

        List<Node> childNodes = parentNode.childNodes();

        Assert.assertEquals(3, childNodes.size());
        Assert.assertEquals(childNode1, childNodes.get(0));
        Assert.assertEquals(childNode2, childNodes.get(1));
        Assert.assertEquals(childNode3, childNodes.get(2));

        Assert.assertEquals(parentNode, childNode1.parent());
        Assert.assertEquals(parentNode, childNode2.parent());
        Assert.assertEquals(parentNode, childNode3.parent());
    }

    @Test
    public void testGetParent() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        childNode.setParentNode(parentNode);

        Assert.assertNull(childNode.parent());
        Assert.assertEquals(childNode, parentNode.childNode(0));

        parentNode.removeChild(childNode);
    }

    @Test
    public void testRemove() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChildren(childNode);
        List<Node> childNodes = parentNode.childNodes();

        parentNode.removeChild(childNode);

        Assert.assertFalse(childNodes.contains(childNode));
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node(baseUri, attributes);
        Node newChildNode = new Node(baseUri, attributes);

        parentNode.addChildren(oldChildNode);
        List<Node> childNodes = parentNode.childNodes();

        parentNode.replaceChild(oldChildNode, newChildNode);

        Assert.assertFalse(childNodes.contains(oldChildNode));
        Assert.assertFalse(oldChildNode.parent() == parentNode);
        Assert.assertTrue(childNodes.contains(newChildNode));
        Assert.assertTrue(newChildNode.parent() == parentNode);
    }

    @Test
    public void testAddChildren() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        Node childNode3 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2, childNode3);

        List<Node> childNodes = parentNode.childNodes();

        Assert.assertEquals(3, childNodes.size());
        Assert.assertEquals(childNode1, childNodes.get(0));
        Assert.assertEquals(childNode2, childNodes.get(1));
        Assert.assertEquals(childNode3, childNodes.get(2));
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node childNode = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);

        childNode.setParentNode(parentNode);

        Assert.assertNull(childNode.parent());
        Assert.assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testToString() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("<html></html>", node.toString());

        StringBuilder accum = new StringBuilder(32 * 1024);
        node.outerHtml(accum);

        Assert.assertEquals("<html class=\"test\"></html>", accum.toString());
    }
}