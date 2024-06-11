package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @Test
    public void testAbstractNodeMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");

        Node node1 = new Element("div", baseUri, attributes);
        Node node2 = new TextNode("Hello World");
        Node node3 = new Element("p", baseUri, attributes);

        // Test constructor with baseUri and attributes
        assertEquals("div", node1.nodeName());
        assertEquals(baseUri, node1.baseUri());
        assertEquals(attributes, node1.attributes());

        // Test constructor without baseUri and attributes
        Node node4 = new Node();
        assertNull(node4.baseUri());
        assertNull(node4.attributes());

        // Test getNodeName method
        assertEquals("div", node1.nodeName());
        assertEquals("p", node3.nodeName());

        // Test getAttr method
        assertEquals("myClass", node1.attr("class"));
        assertEquals("", node1.attr("nonexistent"));
        assertEquals("", node2.attr("nonexistent"));

        // Test getAttributes method
        assertEquals(attributes, node1.attributes());

        // Test setAttr method
        Node node5 = new Element("div", baseUri, attributes);
        node5.attr("id", "myId");
        assertEquals("myId", node5.attr("id"));

        // Test hasAttr method
        assertTrue(node1.hasAttr("class"));
        assertFalse(node1.hasAttr("nonexistent"));
        assertFalse(node2.hasAttr("class"));

        // Test removeAttr method
        Node node6 = new Element("div", baseUri, attributes);
        node6.removeAttr("class");
        assertFalse(node6.hasAttr("class"));

        // Test absUrl method
        assertEquals("http://example.com/myClass", node1.absUrl("class"));
        assertEquals("", node2.absUrl("class"));

        // Test childNode method
        node1.appendChild(node2);
        node3.appendChild(node2);
        Node childNode1 = node1.childNode(0);
        Node childNode2 = node3.childNode(0);
        assertEquals(node2, childNode1);
        assertEquals(node2, childNode2);

        // Test childNodes method
        List<Node> childNodes1 = node1.childNodes();
        List<Node> childNodes2 = node3.childNodes();
        assertEquals(childNode1, childNodes1.get(0));
        assertEquals(childNode2, childNodes2.get(0));
        assertEquals(1, childNodes1.size());
        assertEquals(1, childNodes2.size());

        // Test parent method
        assertEquals(node1, childNode1.parent());
        assertNull(node2.parent());

        // Test ownerDocument method
        Document doc = new Document();
        doc.appendElement("html", baseUri).body();
        doc.body().appendChild(node1);
        assertEquals(doc, node1.ownerDocument());
        assertNull(node2.ownerDocument());

        // Test remove method
        node1.remove();
        assertEquals(0, doc.body().childNodes().size());

        // Test replaceWith method
        Node node7 = new Element("span", baseUri, attributes);
        node1.replaceWith(node7);
        assertEquals(node7, doc.body().childNodes().get(0));
    }

    @Test
    public void testNodeMethodsWithTraversal() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");

        Document doc = new Document();
        doc.setBaseUri(baseUri);

        Element div1 = doc.createElement("div");
        Element p1 = doc.createElement("p");
        Element p2 = doc.createElement("p");
        Text text1 = doc.createTextNode("Hello World");

        div1.appendChild(p1);
        p1.appendChild(text1);
        div1.appendChild(p2);

        doc.body().appendChild(div1);

        NodeTraversor traversor = new NodeTraversor();
        traversor.addRule("div", new NodeVisitor() {
            int count = 0;

            @Override
            public void head(Node node, int depth) {
                assertEquals("div", node.nodeName());
                count++;
            }

            @Override
            public void tail(Node node, int depth) {
                count++;
            }
        });

        traversor.traverse(div1);
        assertEquals(2, count);

        traversor.addRule("p", new NodeVisitor() {
            int count = 0;

            @Override
            public void head(Node node, int depth) {
                assertEquals("p", node.nodeName());
                count++;
            }

            @Override
            public void tail(Node node, int depth) {
                count++;
            }
        });

        traversor.traverse(div1);
        assertEquals(3, count);

        traversor.addRule("text", new NodeVisitor() {
            int count = 0;

            @Override
            public void head(Node node, int depth) {
                assertEquals("text", node.nodeName());
                count++;
            }

            @Override
            public void tail(Node node, int depth) {
                count++;
            }
        });

        traversor.traverse(div1);
        assertEquals(4, count);
    }
}