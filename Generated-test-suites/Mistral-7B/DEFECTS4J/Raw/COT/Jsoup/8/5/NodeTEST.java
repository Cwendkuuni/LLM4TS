package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        assertEquals("node1", node.attr("id"));
        assertEquals(baseUri, node.baseUri());
        assertNull(node.childNodes());
        assertNull(node.parentNode());
    }

    @Test
    public void testNodeConstructorWithBaseUriOnly() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals(baseUri, node.baseUri());
        assertNull(node.childNodes());
        assertNull(node.parentNode());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();

        assertNull(node.baseUri());
        assertNull(node.childNodes());
        assertNull(node.parentNode());
    }

    @Test
    public void testNodeAbstractMethodgetNodeName() {
        // Abstract method, cannot be instantiated and tested directly
    }

    @Test
    public void testAttrMethodWithValidAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        assertEquals("node1", node.attr("id"));
    }

    @Test
    public void testAttrMethodWithInvalidAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        assertEquals("", node.attr("invalidKey"));
    }

    @Test
    public void testAbsUrlMethodWithAbsoluteUrlAttributeValue() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("abs:href", "http://another-example.com");

        Node node = new Node(baseUri, attributes);

        assertEquals("http://another-example.com", node.absUrl("abs:href"));
    }

    @Test
    public void testAbsUrlMethodWithRelativeUrlAttributeValue() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "/path/to/another.html");

        Node node = new Node(baseUri, attributes);

        URL url = null;
        try {
            url = new URL(node.baseUri(), node.absUrl("href"));
        } catch (MalformedURLException e) {
            // Test should fail with MalformedURLException if baseUri is not valid
        }

        Assert.assertNotNull(url);
        assertEquals("http://example.com/path/to/another.html", url.toExternalForm());
    }

    @Test
    public void testAbsUrlMethodWithMissingAttribute() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());

        assertEquals("", node.absUrl("missingKey"));
    }

    @Test
    public void testChildNodeMethodWithValidIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        List<Node> childNodes = new ArrayList<>(3);
        childNodes.add(childNode1);
        childNodes.add(childNode2);
        parentNode.setChildNodes(childNodes);

        Node expectedChild = childNode1;
        Node actualChild = parentNode.childNode(0);

        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testChildNodesMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        List<Node> childNodes = new ArrayList<>(3);
        childNodes.add(childNode1);
        childNodes.add(childNode2);
        parentNode.setChildNodes(childNodes);

        List<Node> actualChildNodes = parentNode.childNodes();
        List<Node> expectedChildNodes = parentNode.childNodesAsArray();

        assertEquals(expectedChildNodes.length, actualChildNodes.size());

        for (int i = 0; i < expectedChildNodes.length; i++) {
            Node expectedChild = expectedChildNodes[i];
            Node actualChild = actualChildNodes.get(i);

            assertEquals(expectedChild.getClass(), actualChild.getClass());
            assertEquals(expectedChild.nodeName(), actualChild.nodeName());
            assertEquals(expectedChild.baseUri(), actualChild.baseUri());
            assertEquals(expectedChild.attributes(), actualChild.attributes());
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildNodeMethodWithInvalidIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        List<Node> childNodes = new ArrayList<>(3);
        childNodes.add(childNode1);
        childNodes.add(childNode2);
        parentNode.setChildNodes(childNodes);

        parentNode.childNode(10);
    }

    @Test
    public void testSetParentNodeMethod() {
        String baseUri1 = "http://example1.com";
        Attributes attributes1 = new Attributes();
        Node childNode = new Node(baseUri1, attributes1);

        String baseUri2 = "http://example2.com";
        Attributes attributes2 = new Attributes();
        Node parentNode = new Node(baseUri2, attributes2);

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parentNode());
    }

    @Test
    public void testRemoveChildMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        List<Node> childNodes = new ArrayList<>(3);
        childNodes.add(childNode1);
        childNodes.add(childNode2);
        parentNode.setChildNodes(childNodes);

        parentNode.removeChild(childNode1);

        List<Node> actualChildNodes = parentNode.childNodes();

        Node expectedChildNode2 = childNode2;
        Node actualChildNode2 = actualChildNodes.get(0);

        assertEquals(expectedChildNode2, actualChildNode2);
        assertEquals(1, actualChildNodes.size());
    }

    @Test
    public void testReplaceChildMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node oldChildNode1 = new Node(baseUri, attributes);
        Node oldChildNode2 = new Node(baseUri, attributes);
        Node newChildNode = new Node(baseUri, attributes);

        List<Node> childNodes = new ArrayList<>(3);
        childNodes.add(oldChildNode1);
        childNodes.add(oldChildNode2);
        parentNode.setChildNodes(childNodes);

        parentNode.replaceChild(oldChildNode1, newChildNode);

        List<Node> actualChildNodes = parentNode.childNodes();

        Node expectedChildNode1 = newChildNode;
        Node actualChildNode1 = actualChildNodes.get(0);

        Node expectedChildNode2 = oldChildNode2;
        Node actualChildNode2 = actualChildNodes.get(1);

        assertEquals(expectedChildNode1, actualChildNode1);
        assertEquals(expectedChildNode2, actualChildNode2);
        assertEquals(2, actualChildNodes.size());
    }

    @Test
    public void testHasAttrMethodWithValidAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        assertTrue(node.hasAttr("id"));
    }

    @Test
    public void testHasAttrMethodWithInvalidAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        assertFalse(node.hasAttr("invalidKey"));
    }

    @Test
    public void testRemoveAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        node.removeAttr("id");

        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testSetAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        node.attr("class", "newClass");

        assertEquals("newClass", node.attr("class"));
    }

    @Test
    public void testSetBaseUriMethod() {
        String oldBaseUri = "http://example1.com";
        Attributes attributes = new Attributes();
        Node node = new Node(oldBaseUri, attributes);

        String newBaseUri = "http://example2.com";
        node.setBaseUri(newBaseUri);

        assertEquals(newBaseUri, node.baseUri());
    }
}