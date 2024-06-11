package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Node class.
 */
public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNodeConstructorWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();

        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullBaseUri() {
        Attributes attributes = new Attributes();
        new Node(null, attributes);
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullAttributes() {
        String baseUri = "http://example.com";
        new Node(baseUri, null);
    }

    @Test
    public void testNodeGetNodeName() {
        // Abstract class Node doesn't have a nodeName, so this test case cannot be executed.
    }

    @Test
    public void testNodeAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Node node = new Node(baseUri, attributes);

        assertEquals("value", node.attr("key"));
        assertEquals("", node.attr("nonExistentKey"));
        assertEquals("value", node.attr("key").trim());
    }

    @Test
    public void testNodeAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Node node = new Node(baseUri, attributes);

        assertEquals(attributes, node.attributes());
        assertTrue(node.hasAttr("key1"));
        assertTrue(node.hasAttr("key2"));
        assertFalse(node.hasAttr("nonExistentKey"));
    }

    @Test
    public void testNodeHasAttrWithEmptyAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        assertFalse(node.hasAttr(""));
    }

    @Test
    public void testNodeSetAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("key", "value");
        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testNodeRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Node node = new Node(baseUri, attributes);

        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
        assertEquals("", node.attr("key"));
    }

    @Test
    public void testNodeAbsUrl() {
        String baseUri = "http://example.com/index.html";
        Node node = new Elements.Tag("a").attr("abs:href", "relative/path.html");

        assertEquals("http://example.com/relative/path.html", node.absUrl("href"));
        assertEquals("", node.absUrl("nonExistentKey"));
    }

    @Test(expected = MalformedURLException.class)
    public void testNodeMalformedUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        node.absUrl("javascript:alert('hello')");
    }

    @Test
    public void testNodeChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Elements.Tag("div");

        parentNode.addChild(childNode);

        assertEquals(childNode, parentNode.childNode(0));
        assertEquals(childNode, parentNode.childNodes().get(0));
        assertNotSame(parentNode, childNode.parentNode());
    }

    @Test
    public void testNodeChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Elements.Tag("div");
        Node childNode2 = new Elements.Tag("div");

        parentNode.addChildren(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testNodeGetParent() {
        String baseUri1 = "http://example1.com";
        Attributes attributes1 = new Attributes();
        Node parentNode1 = new Node(baseUri1, attributes1);

        String baseUri2 = "http://example2.com";
        Attributes attributes2 = new Attributes();
        Node childNode = new Node(baseUri2, attributes2);
        childNode.setParentNode(parentNode1);

        assertEquals(parentNode1, childNode.parent());
        assertNull(childNode.parentNode());
    }

    @Test
    public void testNodeOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        Document document = new Document();
        document.setBaseUri(baseUri);
        node.setOwnerDocument(document);

        assertEquals(document, node.ownerDocument());
        assertNull(node.ownerDocument());
    }

    @Test
    public void testNodeRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);
        parentNode.remove();

        assertNull(childNode.parentNode());
    }

    @Test
    public void testNodeReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node(baseUri, attributes);
        Node newChildNode = new Node(baseUri, attributes);

        parentNode.addChild(oldChildNode);
        parentNode.replaceWith(newChildNode);

        assertEquals(newChildNode, parentNode.childNode(0));
        assertNull(oldChildNode.parentNode());
    }

    @Test
    public void testNodeSetParentNode() {
        String baseUri1 = "http://example1.com";
        Attributes attributes1 = new Attributes();
        Node parentNode1 = new Node(baseUri1, attributes1);

        String baseUri2 = "http://example2.com";
        Attributes attributes2 = new Attributes();
        Node childNode = new Node(baseUri2, attributes2);

        childNode.setParentNode(parentNode1);

        assertEquals(parentNode1, childNode.parentNode());
    }

    @Test
    public void testNodeReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node(baseUri, attributes);
        Node newChildNode = new Node(baseUri, attributes);

        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(newChildNode, oldChildNode);

        assertEquals(newChildNode, parentNode.childNode(0));
        assertNull(oldChildNode.parentNode());
    }

    @Test
    public void testNodeRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);

        assertNull(childNode.parentNode());
    }

    @Test
    public void testNodeAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testNodeAddChildrenWithIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(1, childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(1));
        assertEquals(childNode2, children.get(2));
    }
}