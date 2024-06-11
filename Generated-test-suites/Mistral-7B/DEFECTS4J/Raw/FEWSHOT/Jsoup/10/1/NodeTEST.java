package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Node node = new Node(baseUri, attributes);
        Assert.assertEquals("http://example.com", node.baseUri());
        Assert.assertEquals("value", node.attr("key"));
        Assert.assertNull(node.childNodes());
        Assert.assertNull(node.attributes());

        Node node2 = new Node(baseUri);
        Assert.assertEquals("http://example.com", node2.baseUri());
        Assert.assertNull(node2.childNodes());
        Assert.assertNull(node2.attributes());
    }

    @Test
    public void testNodeName() {
        // Abstract class, cannot instantiate directly
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Node node = new Node(baseUri, attributes);
        Assert.assertEquals("value", node.attr("key"));
        Assert.assertEquals("", node.attr("nonExistentKey"));

        node.attr("key2", "value2");
        Assert.assertEquals("value2", node.attr("key2"));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Node node = new Node(baseUri, attributes);
        Assert.assertEquals(2, node.attributes().size());
        Assert.assertEquals("value1", node.attributes().get("key1"));
        Assert.assertEquals("value2", node.attributes().get("key2"));

        node.removeAttr("key1");
        Assert.assertEquals(1, node.attributes().size());
        Assert.assertNull(node.attributes().get("key1"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Node node = new Node(baseUri, attributes);
        Assert.assertTrue(node.hasAttr("key"));
        Assert.assertFalse(node.hasAttr("nonExistentKey"));

        node.removeAttr("key");
        Assert.assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Node node = new Node(baseUri, attributes);
        node.removeAttr("key1");
        Assert.assertEquals(1, node.attributes().size());
        Assert.assertNull(node.attributes().get("key1"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String relUrl = "/relative/path";

        Node node = new Node(baseUri, new Attributes());
        node.attr("href", relUrl);

        String absUrl = node.absUrl("href");
        Assert.assertEquals("http://example.com/relative/path", absUrl);

        try {
            new URL(absUrl);
        } catch (MalformedURLException e) {
            Assert.fail();
        }

        String badRelUrl = "javascript:alert('XSS')";
        node.attr("onclick", badRelUrl);
        absUrl = node.absUrl("onclick");
        Assert.assertEquals("", absUrl);
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals(childNode1, childNodes.get(0));
        Assert.assertEquals(childNode2, childNodes.get(1));

        Node childNode3 = new Node(baseUri, attributes);
        parentNode.addChildren(0, childNode3);

        childNodes = parentNode.childNodes();
        Assert.assertEquals(3, childNodes.size());
        Assert.assertEquals(childNode3, childNodes.get(0));
        Assert.assertEquals(childNode1, childNodes.get(1));
        Assert.assertEquals(childNode2, childNodes.get(2));

        parentNode.removeChild(childNode1);
        childNodes = parentNode.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertNotEquals(childNode1, childNodes.get(0));
        Assert.assertEquals(childNode2, childNodes.get(1));
    }

    @Test
    public void testParent() {
        String baseUri1 = "http://parent.com";
        String baseUri2 = "http://child.com";
        Attributes attributes = new Attributes();

        Node parentNode1 = new Node(baseUri1, attributes);
        Node childNode = new Node(baseUri2, attributes);

        parentNode1.addChild(childNode);
        Assert.assertEquals(childNode, childNode.parent());
        Assert.assertEquals(parentNode1, parentNode1.childNode(0));

        parentNode1.removeChild(childNode);
        Assert.assertNull(childNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Node(baseUri, attributes);
        Assert.assertNull(node.ownerDocument());

        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChild(node);

        Assert.assertEquals(parentNode, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);
        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(childNode, childNodes.get(0));

        parentNode.remove();
        Assert.assertNull(parentNode.parent());
        Assert.assertNull(childNode.parent());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node(baseUri, attributes);
        Node newChildNode = new Node(baseUri, attributes);

        parentNode.addChild(oldChildNode);
        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(oldChildNode, childNodes.get(0));

        parentNode.replaceChild(oldChildNode, newChildNode);
        childNodes = parentNode.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(newChildNode, childNodes.get(0));
        Assert.assertEquals(newChildNode, oldChildNode.parent());
        Assert.assertEquals(parentNode, newChildNode.parent());

        parentNode.removeChild(newChildNode);
        Assert.assertNull(newChildNode.parent());
    }

    @Test
    public void testSetParentNode() {
        String baseUri1 = "http://parent1.com";
        String baseUri2 = "http://parent2.com";
        Attributes attributes = new Attributes();

        Node childNode = new Node(baseUri1, attributes);
        Node parentNode1 = new Node(baseUri2, attributes);

        childNode.setParentNode(parentNode1);
        Assert.assertEquals(parentNode1, childNode.parent());
        Assert.assertEquals(childNode, parentNode1.childNodes().get(0));
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node(baseUri, attributes);
        Node newChildNode = new Node(baseUri, attributes);

        parentNode.addChild(oldChildNode);
        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(oldChildNode, childNodes.get(0));

        parentNode.replaceChild(oldChildNode, newChildNode);
        childNodes = parentNode.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(newChildNode, childNodes.get(0));
        Assert.assertEquals(newChildNode, oldChildNode.parent());
        Assert.assertEquals(parentNode, newChildNode.parent());

        parentNode.removeChild(newChildNode);
        Assert.assertNull(newChildNode.parent());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);
        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(childNode, childNodes.get(0));

        parentNode.removeChild(childNode);
        childNodes = parentNode.childNodes();
        Assert.assertEquals(0, childNodes.size());

        childNode.setParentNode(null);
        Assert.assertNull(childNode.parent());
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);
        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals(childNode1, childNodes.get(0));
        Assert.assertEquals(childNode2, childNodes.get(1));

        Node childNode3 = new Node(baseUri, attributes);
        parentNode.addChildren(0, childNode3);

        childNodes = parentNode.childNodes();
        Assert.assertEquals(3, childNodes.size());
        Assert.assertEquals(childNode3, childNodes.get(0));
        Assert.assertEquals(childNode1, childNodes.get(1));
        Assert.assertEquals(childNode2, childNodes.get(2));
    }

    @Test
    public void testAddChildrenWithArray() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);

        Node[] children = new Node[3];
        for (int i = 0; i < children.length; i++) {
            children[i] = new Node(baseUri, attributes);
        }

        parentNode.addChildren(children);
        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(3, childNodes.size());
        Assert.assertArrayEquals(children, childNodes.toArray(new Node[0]));
    }

    @Test
    public void testToString() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Node node = new Node(baseUri, attributes);
        node.outerHtmlHead(new StringBuilder(), 0, new Document.OutputSettings());
        node.outerHtmlTail(new StringBuilder(), 0, new Document.OutputSettings());

        String expected = "<node key=\"value\"></node>";
        StringBuilder actual = new StringBuilder(expected.length());
        node.outerHtml(actual);

        Assert.assertEquals(expected, actual.toString());
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Node originalNode = new Node(baseUri, attributes);
        Node clonedNode = originalNode.clone();

        Assert.assertEquals(originalNode.baseUri(), clonedNode.baseUri());
        Assert.assertEquals(originalNode.attributes(), clonedNode.attributes());
        Assert.assertNotSame(originalNode.childNodes(), clonedNode.childNodes());

        List<Node> originalChildren = originalNode.childNodesAsArray();
        List<Node> clonedChildren = clonedNode.childNodesAsArray();

        for (int i = 0; i < originalChildren.length; i++) {
            Node originalChild = originalChildren[i];
            Node clonedChild = clonedChildren[i];
            Assert.assertEquals(originalChild.nodeName(), clonedChild.nodeName());
            Assert.assertEquals(originalChild.attributes(), clonedChild.attributes());
            Assert.assertNotSame(originalChild.childNodes(), clonedChild.childNodes());
        }
    }
}