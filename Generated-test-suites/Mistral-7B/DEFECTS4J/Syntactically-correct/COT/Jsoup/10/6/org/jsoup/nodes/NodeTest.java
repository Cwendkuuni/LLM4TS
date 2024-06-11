package org.jsoup.nodes; // import necessary packages here if needed

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    private Node node;
    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
        node = new Element("div", "baseUrl", attributes);
    }

    

    // Test nodeName() method
    @Test
    public void testNodeName() {
        assertEquals("div", node.nodeName());
    }

    // Test attr() method with valid attribute key
    @Test
    public void testAttrWithValidAttributeKey() {
        node.attr("data-test", "value");
        assertEquals("value", node.attr("data-test"));
    }

    // Test attr() method with invalid attribute key
    @Test(expected = IllegalArgumentException.class)
    public void testAttrWithInvalidAttributeKey() {
        node.attr("invalidKey", "");
    }

    // Test attr() method with empty baseUri
    @Test
    public void testAttrWithEmptyBaseUri() {
        node = new Node("", attributes);
        assertNull(node.baseUri());
        assertEquals("", node.absUrl("href"));
    }

    // Test absUrl() method with absolute URL attribute value
    @Test
    public void testAbsUrlWithAbsoluteUrlAttributeValue() {
        node.attr("abs:href", "http://example.com");
        assertEquals("http://example.com", node.absUrl("abs:href"));
    }

    // Test absUrl() method with relative URL attribute value
    @Test
    public void testAbsUrlWithRelativeUrlAttributeValue() {
        node.attr("href", "relative.html");
        URL base = new URL("http://example.com/base");
        node.setBaseUri(base.toString());
        assertEquals("http://example.com/base/relative.html", node.absUrl("href"));
    }

    // Test absUrl() method with missing attribute
    @Test
    public void testAbsUrlWithMissingAttribute() {
        assertEquals("", node.absUrl("missingAttr"));
    }

    // Test hasAttr() method
    @Test
    public void testHasAttr() {
        node.attr("data-test", "value");
        assertTrue(node.hasAttr("data-test"));
    }

    // Test hasAttr() method with missing attribute
    @Test
    public void testHasAttrWithMissingAttribute() {
        assertFalse(node.hasAttr("missingAttr"));
    }

    // Test attributes() method
    @Test
    public void testAttributes() {
        node.attr("data-test", "value");
        Attributes attributes = node.attributes();
        assertEquals("value", attributes.get("data-test"));
    }

    // Test attr() method with existing attribute
    @Test
    public void testAttrWithExistingAttribute() {
        node.attr("data-test", "existingValue");
        node.attr("data-test", "newValue");
        assertEquals("newValue", node.attr("data-test"));
    }

    // Test attr() method with missing attribute and "abs:" prefix
    @Test
    public void testAttrWithMissingAbsoluteAttribute() {
        assertEquals("", node.absUrl("abs:missingAttr"));
    }

    // Test attr() method with existing absolute attribute and "abs:" prefix
    @Test
    public void testAttrWithExistingAbsoluteAttribute() {
        node.attr("abs:href", "http://example.com");
        assertEquals("http://example.com", node.absUrl("abs:href"));
    }

    // Test removeAttr() method with existing attribute
    @Test
    public void testRemoveAttrWithExistingAttribute() {
        node.attr("data-test", "value");
        node.removeAttr("data-test");
        assertFalse(node.hasAttr("data-test"));
    }

    // Test removeAttr() method with missing attribute
    @Test
    public void testRemoveAttrWithMissingAttribute() {
        node.removeAttr("missingAttr");
        assertFalse(node.hasAttr("missingAttr"));
    }

    // Test baseUri() method
    @Test
    public void testBaseUri() {
        assertEquals("baseUrl", node.baseUri());
    }

    // Test setBaseUri() method
    @Test
    public void testSetBaseUri() {
        String newBaseUri = "http://example.com";
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    // Test childNode() method
    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildNodeWithInvalidIndex() {
        node.childNodes().add(new Element("p", attributes));
        node.childNodes().add(new Element("p", attributes));
        node.childNodes().add(new Element("p", attributes));
        Node childNode = node.childNode(4);
    }

    // Test childNodes() method
    @Test
    public void testChildNodes() {
        Node childNode1 = new Element("p", attributes);
        Node childNode2 = new Element("p", attributes);
        node.addChildren(childNode1, childNode2);
        List<Node> childNodes = node.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));
    }

    // Test parent() method
    @Test
    public void testParent() {
        Node parentNode = new Element("body", attributes);
        node.setParentNode(parentNode);
        assertEquals(parentNode, node.parent());
    }

    // Test ownerDocument() method
    @Test
    public void testOwnerDocument() {
        Document document = new Document();
        node.setOwnerDocument(document);
        assertEquals(document, node.ownerDocument());
    }

    // Test remove() method
    @Test
    public void testRemove() {
        Node parentNode = new Element("body", attributes);
        parentNode.addChildren(node);
        assertEquals(1, parentNode.childNodes().size());
        node.remove();
        assertEquals(0, parentNode.childNodes().size());
    }

    // Test replaceWith() method
    @Test
    public void testReplaceWith() {
        Node parentNode = new Element("body", attributes);
        parentNode.addChildren(node);
        Node newNode = new Element("p", attributes);
        node.replaceWith(newNode);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(newNode, parentNode.childNodes().get(0));
    }

    // Test setParentNode() method
    @Test
    public void testSetParentNode() {
        Node parentNode = new Element("body", attributes);
        Node childNode = new Element("p", attributes);
        childNode.setParentNode(parentNode);
        assertEquals(parentNode, childNode.parent());
    }

    // Test replaceChild() method
    @Test
    public void testReplaceChild() {
        Node parentNode = new Element("body", attributes);
        Node childNode1 = new Element("p", attributes);
        Node childNode2 = new Element("p", attributes);
        parentNode.addChildren(childNode1);
        parentNode.replaceChild(childNode1, childNode2);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(childNode2, parentNode.childNodes().get(0));
    }

    // Test removeChild() method
    @Test
    public void testRemoveChild() {
        Node parentNode = new Element("body", attributes);
        Node childNode1 = new Element("p", attributes);
        Node childNode2 = new Element("p", attributes);
        parentNode.addChildren(childNode1);
        parentNode.addChildren(childNode2);
        parentNode.removeChild(childNode1);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(childNode2, parentNode.childNodes().get(0));
    }

    // Test addChildren() method with null children
    @Test(expected = NullPointerException.class)
    public void testAddChildrenWithNullChildren() {
        Node parentNode = new Element("body", attributes);
        parentNode.addChildren((Node[]) null);
    }

    // Test addChildren() method with empty array
    @Test
    public void testAddChildrenWithEmptyArray() {
        Node parentNode = new Element("body", attributes);
        parentNode.addChildren();
        assertEquals(0, parentNode.childNodes().size());
    }

    // Test addChildren() method with valid children
    @Test
    public void testAddChildrenWithValidChildren() {
        Node parentNode = new Element("body", attributes);
        Node childNode1 = new Element("p", attributes);
        Node childNode2 = new Element("p", attributes);
        parentNode.addChildren(childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNodes().get(0));
        assertEquals(childNode2, parentNode.childNodes().get(1));
    }

    // Test siblingNodes() method
    @Test
    public void testSiblingNodes() {
        Node parentNode = new Element("body", attributes);
        Node sibling1 = new Element("p", attributes);
        Node node = new Element("div", attributes);
        Node sibling2 = new Element("p", attributes);
        parentNode.addChildren(sibling1, node, sibling2);
        List<Node> siblingNodes = node.siblingNodes();
        assertEquals(2, siblingNodes.size());
        assertEquals(sibling1, siblingNodes.get(0));
        assertEquals(sibling2, siblingNodes.get(1));
    }

    // Test nextSibling() method
    @Test
    public void testNextSibling() {
        Node parentNode = new Element("body", attributes);
        Node sibling1 = new Element("p", attributes);
        Node node = new Element("div", attributes);
        Node sibling2 = new Element("p", attributes);
        parentNode.addChildren(sibling1, node, sibling2);
        Node nextSibling = node.nextSibling();
        assertEquals(sibling2, nextSibling);
    }

    // Test previousSibling() method
    @Test
    public void testPreviousSibling() {
        Node parentNode = new Element("body", attributes);
        Node sibling1 = new Element("p", attributes);
        Node node = new Element("div", attributes);
        Node sibling2 = new Element("p", attributes);
        parentNode.addChildren(sibling1, node, sibling2);
        Node previousSibling = node.previousSibling();
        assertEquals(sibling1, previousSibling);
    }

    // Test siblingIndex() method
    @Test
    public void testSiblingIndex() {
        Node parentNode = new Element("body", attributes);
        Node sibling1 = new Element("p", attributes);
        Node node = new Element("div", attributes);
        Node sibling2 = new Element("p", attributes);
        parentNode.addChildren(sibling1, node, sibling2);
        assertEquals(1, node.siblingIndex());
    }

    // Test setSiblingIndex() method
    @Test
    public void testSetSiblingIndex() {
        Node parentNode = new Element("body", attributes);
        Node sibling1 = new Element("p", attributes);
        Node node = new Element("div", attributes);
        Node sibling2 = new Element("p", attributes);
        parentNode.addChildren(sibling1, node, sibling2);
        node.setSiblingIndex(0);
        assertEquals(sibling2, node.previousSibling());
        assertEquals(sibling1, node.nextSibling());
    }

    // Test outerHtml() method
    @Test
    public void testOuterHtml() {
        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        assertEquals("<div baseUrl=''></div>", accum.toString());
    }

    // Test clone() method
    @Test
    public void testClone() {
        Node originalNode = new Element("div", "baseUrl", attributes);
        Node clonedNode = originalNode.clone();
        assertEquals(originalNode.hashCode(), clonedNode.hashCode());
        assertFalse(originalNode == clonedNode);
        assertEquals(originalNode.nodeName(), clonedNode.nodeName());
        assertEquals(originalNode.attributes(), clonedNode.attributes());
        assertEquals(originalNode.baseUri(), clonedNode.baseUri());
    }

    
}