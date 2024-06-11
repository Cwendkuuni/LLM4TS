package org.jsoup.nodes; // Make sure to import all required packages and classes at the beginning of your test file

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Node class
 */
public class NodeTest {

    private Node node;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        this.node = new Node(baseUri, attributes);
    }

    

    @Test
    public void testNodeName() {
        // This is an abstract class, so we cannot create an instance of the abstract method nodeName().
        // You would need to test nodeName() method by using subclasses that implement it.
        fail("Cannot test abstract method nodeName()");
    }

    @Test
    public void testAttr() {
        node.attr("testKey", "testValue");
        assertEquals("testValue", node.attr("testKey"));
    }

    @Test
    public void testAttributes() {
        assertEquals(node.attributes(), node.attributes());
    }

    @Test
    public void testHasAttr() {
        node.attr("testKey", "testValue");
        assertTrue(node.hasAttr("testKey"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("testKey", "testValue");
        node.removeAttr("testKey");
        assertFalse(node.hasAttr("testKey"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(node.baseUri(), baseUri);
    }

    @Test
    public void testSetBaseUri() {
        String newBaseUri = "http://another-example.com";
        node.setBaseUri(newBaseUri);
        assertEquals(node.baseUri(), newBaseUri);
    }

    @Test
    public void testAbsUrl() {
        String relativeUrl = "relativeUrl";
        node.attr("testKey", relativeUrl);
        String expectedAbsoluteUrl = "http://example.com/" + relativeUrl;
        assertEquals(node.absUrl("testKey"), expectedAbsoluteUrl);
    }

    @Test
    public void testAbsUrlForAbsoluteUrl() {
        String absoluteUrl = "http://example.com/absoluteUrl";
        node.attr("testKey", absoluteUrl);
        assertEquals(node.absUrl("testKey"), absoluteUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlForInvalidUrl() {
        node.attr("testKey", "invalidUrl");
        node.absUrl("testKey");
    }

    @Test
    public void testChildNode() {
        Node childNode = new Node("childNodeUri", new Attributes());
        node.childNodes.add(childNode);
        assertEquals(childNode, node.childNode(0));
    }

    @Test
    public void testChildNodes() {
        Node childNode1 = new Node("childNode1Uri", new Attributes());
        Node childNode2 = new Node("childNode2Uri", new Attributes());
        node.childNodes.addAll(List.of(childNode1, childNode2));
        List<Node> expectedChildNodes = List.of(childNode1, childNode2);
        assertEquals(expectedChildNodes, node.childNodes());
    }

    @Test
    public void testChildNodesAsArray() {
        Node childNode1 = new Node("childNode1Uri", new Attributes());
        Node childNode2 = new Node("childNode2Uri", new Attributes());
        node.childNodes.addAll(List.of(childNode1, childNode2));
        Node[] expectedChildNodesAsArray = {childNode1, childNode2};
        assertArrayEquals(expectedChildNodesAsArray, node.childNodesAsArray());
    }

    @Test
    public void testParent() {
        Node parentNode = new Node("parentUri", new Attributes());
        node.setParentNode(parentNode);
        assertEquals(parentNode, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document();
        document.appendChild(node);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parentNode = new Node("parentUri", new Attributes());
        parentNode.appendChild(node);
        node.remove();
        assertFalse(parentNode.childNodes().contains(node));
    }

    @Test
    public void testReplaceWith() {
        Node parentNode = new Node("parentUri", new Attributes());
        Node replacementNode = new Node("replacementNodeUri", new Attributes());
        parentNode.appendChild(node);
        parentNode.replaceChild(node, replacementNode);
        assertTrue(parentNode.childNodes().contains(replacementNode));
        assertFalse(parentNode.childNodes().contains(node));
    }

    @Test
    public void testSetParentNode() {
        Node newParentNode = new Node("newParentUri", new Attributes());
        node.setParentNode(newParentNode);
        assertEquals(newParentNode, node.parent());
    }

    @Test
    public void testReplaceChild() {
        Node oldChildNode = new Node("oldChildNodeUri", new Attributes());
        Node newChildNode = new Node("newChildNodeUri", new Attributes());
        Node parentNode = new Node("parentUri", new Attributes());
        parentNode.appendChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        assertTrue(parentNode.childNodes().contains(newChildNode));
        assertFalse(parentNode.childNodes().contains(oldChildNode));
    }

    @Test
    public void testRemoveChild() {
        Node oldChildNode = new Node("oldChildNodeUri", new Attributes());
        Node parentNode = new Node("parentUri", new Attributes());
        parentNode.appendChild(oldChildNode);
        parentNode.removeChild(oldChildNode);
        assertFalse(parentNode.childNodes().contains(oldChildNode));
    }

    @Test
    public void testAddChildren() {
        Node childNode1 = new Node("childNode1Uri", new Attributes());
        Node childNode2 = new Node("childNode2Uri", new Attributes());
        Node parentNode = new Node("parentUri", new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        List<Node> expectedChildren = List.of(childNode1, childNode2);
        assertEquals(expectedChildren, parentNode.childNodes());
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node childNode1 = new Node("childNode1Uri", new Attributes());
        Node childNode2 = new Node("childNode2Uri", new Attributes());
        Node parentNode = new Node("parentUri", new Attributes());
        parentNode.addChildren(0, childNode1, childNode2);
        List<Node> expectedChildren = List.of(childNode1, childNode2);
        assertEquals(expectedChildren, parentNode.childNodes());
    }

    @Test
    public void testSiblingNodes() {
        Node siblingNode1 = new Node("siblingNode1Uri", new Attributes());
        Node siblingNode2 = new Node("siblingNode2Uri", new Attributes());
        Node parentNode = new Node("parentUri", new Attributes());
        parentNode.addChildren(node, siblingNode1, siblingNode2);
        List<Node> expectedSiblingNodes = List.of(siblingNode1, siblingNode2);
        assertEquals(expectedSiblingNodes, node.siblingNodes());
    }

    @Test
    public void testNextSibling() {
        Node siblingNode1 = new Node("siblingNode1Uri", new Attributes());
        Node siblingNode2 = new Node("siblingNode2Uri", new Attributes());
        Node parentNode = new Node("parentUri", new Attributes());
        parentNode.addChildren(node, siblingNode1, siblingNode2);
        Node nextSibling = node.nextSibling();
        assertEquals(siblingNode1, nextSibling);
    }

    @Test
    public void testPreviousSibling() {
        Node siblingNode1 = new Node("siblingNode1Uri", new Attributes());
        Node siblingNode2 = new Node("siblingNode2Uri", new Attributes());
        Node parentNode = new Node("parentUri", new Attributes());
        parentNode.addChildren(node, siblingNode1, siblingNode2);
        Node previousSibling = node.previousSibling();
        assertEquals(null, previousSibling);
    }

    @Test
    public void testSiblingIndex() {
        Node siblingNode1 = new Node("siblingNode1Uri", new Attributes());
        Node siblingNode2 = new Node("siblingNode2Uri", new Attributes());
        Node parentNode = new Node("parentUri", new Attributes());
        parentNode.addChildren(node, siblingNode1, siblingNode2);
        int siblingIndex = node.siblingIndex();
        assertEquals(0, siblingIndex);
    }

    @Test
    public void testSetSiblingIndex() {
        Node siblingNode1 = new Node("siblingNode1Uri", new Attributes());
        Node siblingNode2 = new Node("siblingNode2Uri", new Attributes());
        Node parentNode = new Node("parentUri", new Attributes());
        parentNode.addChildren(node, siblingNode1, siblingNode2);
        node.setSiblingIndex(1);
        int siblingIndex = node.siblingIndex();
        assertEquals(1, siblingIndex);
    }

    @Test
    public void testOuterHtml() {
        StringBuilder accum = new StringBuilder(1024);
        node.outerHtml(accum);
        String expectedOuterHtml = "<html><body><node id=\"testNode\"></node></body></html>";
        assertEquals(expectedOuterHtml, accum.toString());
    }

    @Test
    public void testEquals() {
        Node anotherNode = new Node("anotherUri", new Attributes());
        assertTrue(node.equals(node));
        assertFalse(node.equals(anotherNode));
    }

    @Test
    public void testHashCode() {
        Node anotherNode = new Node("anotherUri", new Attributes());
        int nodeHashCode = node.hashCode();
        int anotherNodeHashCode = anotherNode.hashCode();
        assertNotEquals(nodeHashCode, anotherNodeHashCode);
    }

    
}