package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @Test
    public void testEmptyNode() {
        Node node = new Node();
        assertEquals("", node.nodeName());
        assertEquals(null, node.attributes());
        assertNull(node.parent());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.baseUri());
        assertNull(node.attr("test"));;
    }

    @Test
    public void testNodeWithAttributes() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        Node node = new Element("testNode", baseUri, attributes);

        assertEquals("testNode", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parent());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertEquals("testValue", node.attr("testKey"));
        assertEquals("testValue", node.attr("test:Key")); // test case for abs: prefix
    }

    @Test
    public void testNodeWithChildren() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("parentNode", baseUri, attributes);

        Node child1 = new Element("child1", baseUri, attributes);
        Node child2 = new TextNode("child2 text");
        parentNode.appendChild(child1);
        parentNode.appendChild(child2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));

        Node child = parentNode.childNode(0);
        assertEquals(child1, child);

        Node grandChild = new Element("grandChild", baseUri, attributes).appendChild(new TextNode("grandChild text"));
        parentNode.appendChild(grandChild);
        children = parentNode.childNodes();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(grandChild, children.get(2));
    }

    @Test
    public void testAbsoluteUrl() {
        String baseUri = "http://www.example.com";
        Node node = new Element("aNode", baseUri, new Attributes());
        node.attr("href", "relativeUrl");

        String absUrl = node.absUrl("href");
        assertEquals("http://www.example.com/relativeUrl", absUrl);

        String absUrlWithSlash = node.absUrl("/relativeUrl");
        assertEquals("http://www.example.com/relativeUrl", absUrlWithSlash);

        String absUrlWithDoubleSlash = node.absUrl("//relativeUrl");
        assertEquals("http://www.example.com/relativeUrl", absUrlWithDoubleSlash);

        String absUrlWithProtocol = node.absUrl("http://www.example2.com/relativeUrl");
        assertEquals("http://www.example2.com/relativeUrl", absUrlWithProtocol);
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("parentNode", baseUri, attributes);

        Node child1 = new Element("child1", baseUri, attributes);
        Node child2 = new TextNode("child2 text");
        parentNode.appendChild(child1);
        parentNode.appendChild(child2);

        parentNode.removeChild(child1);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(child2, children.get(0));
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("parentNode", baseUri, attributes);

        Node child1 = new Element("child1", baseUri, attributes);
        Node child2 = new TextNode("child2 text");
        parentNode.appendChild(child1);
        parentNode.appendChild(child2);

        Node newChild = new Element("newChild", baseUri, attributes);
        parentNode.replaceChild(child1, newChild);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(newChild, children.get(0));
        assertEquals(child2, children.get(1));
    }
}