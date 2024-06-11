package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("Node", node1.getClass().getSimpleName());
        assertEquals("Node", node2.getClass().getSimpleName());
        assertEquals("Node", node3.getClass().getSimpleName());

        assertEquals("", node3.attr("attr1"));
        assertNull(node3.attributes());
        assertNull(node3.baseUri());

        assertEquals("", node2.attr("attr1"));
        assertNotNull(node2.attributes());
        assertEquals(attributes, node2.attributes());
        assertEquals(baseUri, node2.baseUri());

        assertEquals(baseUri, node1.baseUri());
        assertEquals(attributes, node1.attributes());
    }

    @Test
    public void testAbstractNodeMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");

        Node parentNode = new Element("div");
        List<Node> childNodes = new ArrayList<>();
        childNodes.add(new TextNode("Child 1"));
        childNodes.add(new Element("p").attributes(attributes));

        Element node = new Element("span").attributes(attributes).baseUri(baseUri);
        node.setParentNode(parentNode);
        node.setChildNodes(childNodes);

        assertEquals("span", node.nodeName());
        assertEquals("div", node.parent().nodeName());
        assertEquals(childNodes, node.childNodes());
        assertEquals("", node.attr("attr1"));
        assertEquals(attributes, node.attributes());
        assertEquals(baseUri, node.baseUri());

        assertEquals("Child 1", node.childNode(0).nodeName());
        assertEquals("p", node.childNode(1).nodeName());

        Node childNode = node.childNodes().get(0);
        assertEquals(node, childNode.parentNode());

        node.remove();
        assertEquals(0, parentNode.childNodes().size());

        node.replaceWith(new TextNode("Replaced Node"));
        assertEquals("Replaced Node", node.parentNode().childNode(0).nodeName());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("abs:href", "/path/to/file.html");

        Node node = new Element("a").attributes(attributes).baseUri(baseUri);

        String absUrl = node.absUrl("href");
        assertEquals("/path/to/file.html", absUrl);

        absUrl = node.absUrl("abs:href");
        assertEquals("/path/to/file.html", absUrl);

        absUrl = node.absUrl("nonExistentAttr");
        assertEquals("", absUrl);
    }
}