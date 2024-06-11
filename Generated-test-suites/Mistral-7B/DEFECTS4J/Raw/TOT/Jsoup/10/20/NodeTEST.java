package org.jsoup.nodes;

import org.junit.Assert;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @org.junit.Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node node1 = new Element("div", baseUri, attributes); // Test constructor with baseUri and attributes
        Node node2 = new Element("div", baseUri); // Test constructor with baseUri only
        Node node3 = new Node(); // Test default constructor

        Assert.assertEquals("div", node1.nodeName());
        Assert.assertEquals(baseUri, node1.baseUri());
        Assert.assertEquals(attributes, node1.attributes());
        Assert.assertNull(node2.attributes());
        Assert.assertEquals(baseUri, node2.baseUri());
        Assert.assertNull(node3.attributes());
        Assert.assertEquals(Collections.EMPTY_LIST, node3.childNodes());
    }

    @org.junit.Test
    public void testAttrGettersAndSetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node node = new Element("div", baseUri, attributes);

        Assert.assertEquals("id", node.attr("id"));
        Assert.assertEquals("testNode", node.attr("id"));
        Assert.assertEquals("", node.attr("nonexistentAttr"));
        Assert.assertEquals("", node.attr(""));
        Assert.assertFalse(node.hasAttr("nonexistentAttr"));

        node.attr("class", "test");
        Assert.assertEquals("test", node.attr("class"));
        Assert.assertTrue(node.hasAttr("class"));

        node.removeAttr("class");
        Assert.assertFalse(node.hasAttr("class"));
        Assert.assertEquals("", node.attr("class"));

        node.attr("abs:href", "http://example.com/test");
        Assert.assertEquals("http://example.com/test", node.absUrl("href"));
    }

    @org.junit.Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri);
        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> children = parent.childNodes();
        Assert.assertEquals(2, children.size());
        Assert.assertEquals(child1, children.get(0));
        Assert.assertEquals(child2, children.get(1));

        Node grandChild = new Element("span", baseUri, attributes);
        child1.appendChild(grandChild);

        Assert.assertEquals(3, parent.childNodes().size());
        Assert.assertEquals(child1, parent.childNode(0));
        Assert.assertEquals(child2, parent.childNode(1));
        Assert.assertEquals(grandChild, parent.childNode(2));

        Node sibling = new Element("p", baseUri, attributes);
        parent.insertBefore(sibling, child1);

        Assert.assertEquals(4, parent.childNodes().size());
        Assert.assertEquals(sibling, parent.childNode(0));
        Assert.assertEquals(child1, parent.childNode(1));
        Assert.assertEquals(child2, parent.childNode(2));
        Assert.assertEquals(grandChild, parent.childNode(3));
    }

    @org.junit.Test
    public void testParentNodeAndOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node child = new Element("p", baseUri, attributes);
        Node parent = new Element("div", baseUri);

        child.setParentNode(parent);
        Assert.assertEquals(parent, child.parent());
        Assert.assertEquals(parent, child.ownerDocument());

        parent.appendChild(child);
        Assert.assertEquals(child, parent.childNode(0));

        Node grandParent = new Element("body", baseUri);
        grandParent.appendChild(parent);
        Assert.assertEquals(grandParent, child.ownerDocument());

        child.remove();
        Assert.assertNull(child.parent());
        Assert.assertNull(child.ownerDocument());
    }

    @org.junit.Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";

        Node node = new Element("a", baseUri);
        node.attr("href", "/test.html");

        String absUrl = node.absUrl("href");
        Assert.assertEquals("http://example.com/test.html", absUrl);

        node.attr("href", "test.html");
        absUrl = node.absUrl("href");
        Assert.assertEquals("http://example.com/test.html", absUrl);

        node.attr("href", "./test.html");
        absUrl = node.absUrl("href");
        Assert.assertEquals("http://example.com/test.html", absUrl);

        node.attr("href", "../test.html");
        absUrl = node.absUrl("href");
        Assert.assertEquals("http://example.com/test.html", absUrl);

        node.attr("href", "http://sub.example.com/test.html");
        absUrl = node.absUrl("href");
        Assert.assertEquals("http://sub.example.com/test.html", absUrl);
    }

    @org.junit.Test
    public void testRemoveAndReplace() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri);
        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.appendChild(child1);
        parent.appendChild(child2);

        Node nodeToRemove = child1;
        Node nodeToReplace = new Element("span", baseUri, attributes);

        parent.removeChild(nodeToRemove);
        parent.replaceChild(nodeToReplace, nodeToRemove);

        List<Node> children = parent.childNodes();
        Assert.assertEquals(2, children.size());
        Assert.assertEquals(child2, children.get(0));
        Assert.assertEquals(nodeToReplace, children.get(1));

        String expectedHtml = "<div>" +
                "<p>" +
                "</p>" +
                "<span>" +
                "</span>" +
                "</div>";
        Assert.assertEquals(expectedHtml, parent.outerHtml());
    }

    @org.junit.Test
    public void testClone() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node original = new Element("div", baseUri, attributes);
        Node cloned = original.clone();

        Assert.assertEquals(original.nodeName(), cloned.nodeName());
        Assert.assertEquals(original.attributes(), cloned.attributes());
        Assert.assertEquals(original.childNodes(), cloned.childNodes());
        Assert.assertNotSame(original, cloned);

        // Test cloning with children
        Node child1 = new Element("p", baseUri, attributes);
        original.appendChild(child1);
        cloned.appendChild(child1.clone());

        Assert.assertEquals(original.childNodes(), cloned.childNodes());
        Assert.assertEquals(original.outerHtml(), cloned.outerHtml());

        // Test cloning with nested children
        Node child2 = new Element("span", baseUri, attributes);
        child1.appendChild(child2);
        original.appendChild(child1);
        cloned.appendChild(child1.clone());

        Assert.assertEquals(original.outerHtml(), cloned.outerHtml());
    }

    @org.junit.Test
    public void testEqualsAndHashCode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node1 = new Element("div", baseUri, attributes);
        Node node2 = new Element("div", baseUri, attributes);

        Assert.assertEquals(node1, node2);
        Assert.assertEquals(node1.hashCode(), node2.hashCode());

        Node node3 = new Element("div", baseUri);
        Assert.assertNotEquals(node1, node3);
        Assert.assertNotEquals(node1.hashCode(), node3.hashCode());

        node3.setAttribute("data-test", "value");
        Assert.assertNotEquals(node1, node3);
        Assert.assertNotEquals(node1.hashCode(), node3.hashCode());

        Node node4 = node3.clone();
        Assert.assertEquals(node3, node4);
        Assert.assertEquals(node3.hashCode(), node4.hashCode());
    }
}