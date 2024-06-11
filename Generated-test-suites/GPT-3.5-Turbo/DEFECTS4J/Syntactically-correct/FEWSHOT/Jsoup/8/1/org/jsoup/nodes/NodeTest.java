package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NodeTest {
    private Node parentNode;
    private Node childNode;

    @Before
    public void setUp() {
        Attributes attributes = new Attributes();
        parentNode = new Element("parent", "baseUri", attributes);
        childNode = new Element("child", "baseUri", attributes);
    }

    @Test
    public void testAttr() {
        parentNode.attr("attributeKey", "attributeValue");
        assertEquals("attributeValue", parentNode.attr("attributeKey"));
    }

    @Test
    public void testHasAttr() {
        parentNode.attr("attributeKey", "attributeValue");
        assertTrue(parentNode.hasAttr("attributeKey"));
        assertFalse(parentNode.hasAttr("nonexistentAttributeKey"));
    }

    @Test
    public void testRemoveAttr() {
        parentNode.attr("attributeKey", "attributeValue");
        parentNode.removeAttr("attributeKey");
        assertFalse(parentNode.hasAttr("attributeKey"));
    }

    @Test
    public void testBaseUri() {
        parentNode.setBaseUri("newBaseUri");
        assertEquals("newBaseUri", parentNode.baseUri());
    }

    @Test
    public void testChildNode() {
        parentNode.addChildren(childNode);
        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testChildNodes() {
        parentNode.addChildren(childNode);
        assertEquals(1, parentNode.childNodes().size());
    }

    @Test
    public void testParent() {
        parentNode.addChildren(childNode);
        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document("");
        parentNode.addChildren(childNode);
        assertEquals(document, parentNode.ownerDocument());
        assertNull(childNode.ownerDocument());
    }

    @Test
    public void testRemove() {
        parentNode.addChildren(childNode);
        childNode.remove();
        assertEquals(0, parentNode.childNodes().size());
    }

    @Test
    public void testReplaceWith() {
        Node newNode = new Element("new", "baseUri", new Attributes());
        parentNode.addChildren(childNode);
        childNode.replaceWith(newNode);
        assertEquals(newNode, parentNode.childNode(0));
    }

    @Test
    public void testSiblingNodes() {
        Node siblingNode = new Element("sibling", "baseUri", new Attributes());
        parentNode.addChildren(childNode, siblingNode);
        assertEquals(2, childNode.siblingNodes().size());
        assertEquals(childNode, siblingNode.nextSibling());
        assertEquals(siblingNode, childNode.previousSibling());
    }

    @Test
    public void testNextSibling() {
        Node siblingNode = new Element("sibling", "baseUri", new Attributes());
        parentNode.addChildren(childNode, siblingNode);
        assertEquals(siblingNode, childNode.nextSibling());
        assertNull(siblingNode.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node siblingNode = new Element("sibling", "baseUri", new Attributes());
        parentNode.addChildren(childNode, siblingNode);
        assertEquals(childNode, siblingNode.previousSibling());
        assertNull(childNode.previousSibling());
    }
}