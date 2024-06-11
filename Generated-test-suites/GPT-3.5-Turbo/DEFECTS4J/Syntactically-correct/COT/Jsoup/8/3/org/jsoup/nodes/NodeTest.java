package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.select.NodeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;


public abstract class NodeTest {
    
    Node node;
    
    @Test
    public void testNodeName() {
        String nodeName = node.nodeName();
        assertNotNull(nodeName);
        assertFalse(nodeName.isEmpty());
    }

    @Test
    public void testAttr() {
        String attributeKey = "attributeKey";
        String attributeValue = "attributeValue";
        
        node.attr(attributeKey, attributeValue);
        String attrValue = node.attr(attributeKey);
        assertEquals(attributeValue, attrValue);
    }

    @Test
    public void testAttributes() {
        Attributes attributes = node.attributes();
        assertNotNull(attributes);
    }

    @Test
    public void testHasAttr() {
        String attributeKey = "attributeKey";
        assertFalse(node.hasAttr(attributeKey));

        node.attr(attributeKey);
        assertTrue(node.hasAttr(attributeKey));
    }

    @Test
    public void testRemoveAttr() {
        String attributeKey = "attributeKey";
        String attributeValue = "attributeValue";
        
        node.attr(attributeKey, attributeValue);
        assertTrue(node.hasAttr(attributeKey));

        node.removeAttr(attributeKey);
        assertFalse(node.hasAttr(attributeKey));
    }

    @Test
    public void testBaseUri() {
        String baseUri = node.baseUri();
        assertNotNull(baseUri);
    }

    @Test
    public void testAbsUrl() {
        String attributeKey = "attributeKey";
        String attributeValue = "attributeValue";
        node.attr(attributeKey, attributeValue);
        
        String absUrl = node.absUrl(attributeKey);
        assertNotNull(absUrl);
    }

    @Test
    public void testChildNode() {
        List<Node> childNodes = node.childNodes();
        assertEquals(0, childNodes.size());
        
        Node childNode = new Node();
        node.addChildren(childNode);
        
        Node retrievedChildNode = node.childNodes().get(0);
        assertEquals(childNode, retrievedChildNode);
    }

    @Test
    public void testParent() {
        Node parentNode = node.parent();
        assertNull(parentNode);
        
        Node parentNode = new Node();
        node.setParentNode(parentNode);
        
        Node retrievedParentNode = node.parent();
        assertEquals(parentNode, retrievedParentNode);
    }

    @Test
    public void testOwnerDocument() {
        Document ownerDocument = node.ownerDocument();
        assertNull(ownerDocument);
        
        Document ownerDocument = new Document();
        node.setOwnerDocument(ownerDocument);
        
        Document retrievedOwnerDocument = node.ownerDocument();
        assertEquals(ownerDocument, retrievedOwnerDocument);
    }

    @Test
    public void testRemove() {
        Node parentNode = new Node();
        node.setParentNode(parentNode);
        
        node.remove();
        
        assertNull(node.parent());
    }

    @Test
    public void testReplaceWith() {
        Node replacementNode = new Node();
        
        Node parentNode = new Node();
        node.setParentNode(parentNode);
        node.replaceWith(replacementNode);
        
        assertNull(node.parent());
        assertEquals(parentNode, replacementNode.parent());
        assertEquals(replacementNode, parentNode.childNodes().get(0));
    }

    @Test
    public void testSiblingNodes() {
        List<Node> siblingNodes = node.siblingNodes();
        assertNotNull(siblingNodes);
    }

    @Test
    public void testNextSibling() {
        Node nextSibling = node.nextSibling();
        assertNull(nextSibling);
        
        Node siblingNode = new Node();
        Node parentNode = new Node();
        parentNode.addChildren(node, siblingNode);
        
        nextSibling = node.nextSibling();
        assertEquals(siblingNode, nextSibling);
    }

    @Test
    public void testPreviousSibling() {
        Node previousSibling = node.previousSibling();
        assertNull(previousSibling);
        
        Node siblingNode = new Node();
        Node parentNode = new Node();
        parentNode.addChildren(siblingNode, node);
        
        previousSibling = node.previousSibling();
        assertEquals(siblingNode, previousSibling);
    }

    @Test
    public void testSiblingIndex() {
        Integer siblingIndex = node.siblingIndex();
        assertNull(siblingIndex);
        
        Node siblingNode = new Node();
        Node parentNode = new Node();
        parentNode.addChildren(siblingNode, node);
        
        siblingIndex = node.siblingIndex();
        assertEquals(new Integer(1), siblingIndex);
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = node.outerHtml();
        assertNotNull(outerHtml);
        assertFalse(outerHtml.isEmpty());
    }

    @Test
    public void testEquals() {
        assertTrue(node.equals(node));
        assertFalse(node.equals(null));
        assertFalse(node.equals(new Object()));
        // TODO: test equality against other nodes
    }

    @Test
    public void testHashCode() {
        int hashCode = node.hashCode();
        assertEquals(hashCode, node.hashCode());
        // TODO: test hashcode consistency
    }

}