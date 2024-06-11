package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.jsoup.nodes.traversing.DocumentTraversal;
import org.jsoup.nodes.traversing.TraversalUtils;
import org.jsoup.select.Elements;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "testNode");
        Node node1 = new Node(baseUri, attributes);

        Assert.assertEquals("testNode", node1.nodeName());
        Assert.assertEquals(baseUri, node1.baseUri());
        Assert.assertEquals(attributes, node1.attributes());
        Assert.assertNull(node1.parentNode());
        Assert.assertFalse(node1.hasChildNodes());
    }

    @Test
    public void testEmptyNodeCreation() {
        Node emptyNode = new Node();
        Assert.assertEquals(Collections.EMPTY_LIST, emptyNode.childNodes());
        Assert.assertNull(emptyNode.attributes());
        Assert.assertNull(emptyNode.baseUri());
        Assert.assertNull(emptyNode.parentNode());
    }

    @Test
    public void testAttributeGettersAndSetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "testNode");
        Node node = new Node(baseUri, attributes);

        String attrValue = "testValue";
        node.attr("testAttribute", attrValue);
        Assert.assertEquals(attrValue, node.attr("testAttribute"));
        Assert.assertEquals("", node.attr("nonexistentAttribute"));
        node.removeAttr("testAttribute");
        Assert.assertEquals("", node.attr("testAttribute"));
    }

    @Test
    public void testAttributeAbsUrl() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("href", "/testPage.html");
        Node node = new Node(baseUri, attributes);

        String absUrl = node.absUrl("href");
        Assert.assertEquals("http://example.com/testPage.html", absUrl);
    }

    @Test(expected = IllegalStateException.class)
    public void testAttributeAbsUrlNullBaseUri() {
        Attributes attributes = new AttributesImpl();
        attributes.put("href", "/testPage.html");
        Node node = new Node();

        node.absUrl("href");
    }

    @Test
    public void testChildNodesGettersAndSetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, new AttributesImpl());
        Node childNode2 = new Element("div", new AttributesImpl().put("id", "child2"));
        parentNode.addChildren(childNode1, childNode2);

        Node firstChild = parentNode.childNode(0);
        Assert.assertEquals(childNode1, firstChild);

        List<Node> children = parentNode.childNodes();
        Assert.assertEquals(2, children.size());
        Assert.assertEquals(childNode1, children.get(0));
        Assert.assertEquals(childNode2, children.get(1));

        Node lastChild = parentNode.childNode(children.size() - 1);
        Assert.assertEquals(childNode2, lastChild);

        parentNode.removeChild(childNode1);
        children = parentNode.childNodes();
        Assert.assertEquals(1, children.size());
        Assert.assertEquals(childNode2, children.get(0));
    }

    @Test
    public void testParentNodeGettersAndSetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node childNode = new Node(baseUri, attributes);

        Node parentNode = new Node(baseUri, new AttributesImpl());
        childNode.setParentNode(parentNode);

        Assert.assertEquals(parentNode, childNode.parentNode());
        Assert.assertEquals(childNode, parentNode.childNode(0));

        parentNode.removeChild(childNode);
        Assert.assertNull(childNode.parentNode());
    }

    @Test
    public void testTraversing() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node root = new Document(baseUri, new AttributesImpl(), new Document.OutputSettings());
        Node parentNode = new Element("body", attributes);
        Node childNode1 = new Element("p", new AttributesImpl().put("id", "child1"));
        Node childNode2 = new Element("p", new AttributesImpl().put("id", "child2"));
        parentNode.addChildren(childNode1, childNode2);
        root.setBody(parentNode);

        DocumentTraversal traversal = root.traversingDocument();
        Elements elements = TraversalUtils.selectElements(traversal, "body > p");
        Assert.assertEquals(2, elements.size());

        int index = 0;
        Node currentNode = elements.get(index++);
        Assert.assertEquals("child1", currentNode.attr("id"));
        Assert.assertEquals("p", currentNode.nodeName());
        currentNode = elements.get(index++);
        Assert.assertEquals("child2", currentNode.attr("id"));
        Assert.assertEquals("p", currentNode.nodeName());
    }
}