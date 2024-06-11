package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.jsoup.nodes.Node.NodeType.ELEMENT_NODE;
import static org.jsoup.nodes.Node.NodeType.TEXT_NODE;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test-id");
        Node node = new Node(baseUri, attributes);
        Assert.assertEquals("test-id", node.attr("id"));
        Assert.assertNull(node.childNodes().size());
        Assert.assertNull(node.parentNode());
    }

    @Test
    public void testNodeCreationWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        Assert.assertNull(node.attributes().size());
        Assert.assertNull(node.childNodes().size());
        Assert.assertNull(node.parentNode());
    }

    @Test
    public void testNodeCreationWithoutBaseUriOrAttributes() {
        Node node = new Node();
        Assert.assertNull(node.baseUri());
        Assert.assertNull(node.attributes().size());
        Assert.assertNull(node.childNodes().size());
        Assert.assertNull(node.parentNode());
    }

    @Test
    public void testAbstractNodeMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test-id");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(ELEMENT_NODE, node.nodeType());

        node.attr("test-attr", "test-value");
        Assert.assertEquals("test-value", node.attr("test-attr"));
        Assert.assertEquals(1, node.childNodes().size());
        Assert.assertNull(node.parentNode());

        node.removeAttr("test-attr");
        Assert.assertEquals("", node.attr("test-attr"));

        String absUrl = node.absUrl("test-attr");
        Assert.assertEquals("", absUrl);

        Node child = new Node();
        node.addChild(child);
        Assert.assertEquals(1, node.childNodes().size());
        Assert.assertEquals(node, child.parentNode());

        Node sibling = new Node();
        node.addChild(sibling);
        Assert.assertEquals(2, node.childNodes().size());
        Assert.assertEquals(sibling, node.nextSibling());

        Node grandChild = new Node();
        child.addChild(grandChild);
        Assert.assertEquals(1, child.childNodes().size());
        Assert.assertEquals(grandChild, child.childNodes().get(0));
        Assert.assertEquals(grandChild, node.childNodes().get(0).childNodes().get(0));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "relative.html");
        Node node = new Elements.Text("", baseUri, attributes);

        String absUrl = node.absUrl("href");
        Assert.assertEquals("http://example.com/relative.html", absUrl);

        String baseUri2 = "https://example2.com";
        Node node2 = new Elements.Text("", baseUri2, attributes);
        String absUrl2 = node2.absUrl("href");
        Assert.assertEquals("https://example2.com/relative.html", absUrl2);

        String baseUri3 = "http://example.com";
        Node node3 = new Elements.Text("", baseUri, null);
        String absUrl3 = node3.absUrl("abs:href");
        Assert.assertEquals("http://example.com/", absUrl3);
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Elements.Div(baseUri, attributes);

        Node child1 = new Elements.P(baseUri, attributes);
        Node child2 = new Elements.P(baseUri, attributes);
        Node child3 = new Elements.Text("Child 3", baseUri, null);

        parent.addChildren(child1, child2, child3);

        List<Node> children = parent.childNodes();
        Assert.assertEquals(3, children.size());
        Assert.assertEquals(child1, children.get(0));
        Assert.assertEquals(child2, children.get(1));
        Assert.assertEquals(child3, children.get(2));

        Node grandChild = new Elements.Text("Grandchild", baseUri, null);
        child1.addChild(grandChild);
        children = parent.childNodes();
        Assert.assertEquals(4, children.size());
        Assert.assertEquals(child1, children.get(0));
        Assert.assertEquals(child2, children.get(1));
        Assert.assertEquals(child3, children.get(2));
        Assert.assertEquals(grandChild, children.get(3));
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Elements.Div(baseUri, attributes);
        Node child = new Elements.P(baseUri, attributes);

        child.setParentNode(parent);
        Assert.assertEquals(parent, child.parentNode());

        Node grandParent = new Elements.Div(baseUri, attributes);
        grandParent.setParentNode(null); // reset grandParent to be a root node
        child.setParentNode(grandParent);
        Assert.assertEquals(grandParent, child.parentNode());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Elements.Div(baseUri, attributes);
        Node child1 = new Elements.P(baseUri, attributes);
        Node child2 = new Elements.P(baseUri, attributes);

        parent.addChildren(child1, child2);
        Assert.assertEquals(2, parent.childNodes().size());

        parent.removeChild(child1);
        Assert.assertEquals(1, parent.childNodes().size());
        Assert.assertNull(child1.parentNode());

        parent.removeChild(child2);
        Assert.assertEquals(0, parent.childNodes().size());
        Assert.assertNull(child2.parentNode());
    }
}