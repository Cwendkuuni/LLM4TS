package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

public class NodeTest {

    @Test
    public void testConstructor() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        assertEquals("Unexpected node name.", "Node", node.nodeName());
        assertEquals("Unexpected base URI.", baseUri, node.baseUri());
        assertNotNull("Unexpected null attributes.", node.attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Node(null, new Attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        new Node("http://test.com", null);
    }

    @Test
    public void testAttr() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr("class", "test");
        assertEquals("Unexpected attribute value.", "test", node.attr("class"));
        assertEquals("Unexpected empty attribute value.", "", node.attr("nonExistentAttr"));
        assertEquals("Unexpected base URI attribute value.", baseUri, node.absUrl("href"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributeKey() {
        new Node("http://test.com", new Attributes())
                .attr(null, "value");
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr("class", "test");
        assertTrue("Unexpected false result.", node.hasAttr("class"));
        assertFalse("Unexpected true result.", node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr("class", "test");
        node.removeAttr("class");
        assertFalse("Unexpected non-empty attributes after remove.", node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr("href", "relative.html");
        assertEquals("Unexpected empty absolute URL.", "", node.absUrl(""));
        assertEquals("Unexpected absolute URL.", "http://test.com/relative.html", node.absUrl("href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedUrlAbsUrl() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr("href", "malformed://test.com");
        node.absUrl("href");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildNodeNegativeIndex() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        Node childNode3 = parentNode.childNode(-1);
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals("Unexpected child node count.", 2, childNodes.size());
        assertEquals("Unexpected child node 1.", childNode1, childNodes.get(0));
        assertEquals("Unexpected child node 2.", childNode2, childNodes.get(1));
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals("Unexpected child node count.", 2, childNodes.size());
        assertEquals("Unexpected child node 1.", childNode1, childNodes.get(0));
        assertEquals("Unexpected child node 2.", childNode2, childNodes.get(1));
    }

    @Test(expected = NullPointerException.class)
    public void testNullChildNode() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChildren((Node) null);
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        parentNode.removeChild(childNode1);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals("Unexpected child node count.", 1, childNodes.size());
        assertEquals("Unexpected remaining child node.", childNode2, childNodes.get(0));
    }
}