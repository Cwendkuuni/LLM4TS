package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorConstruction() {
        // ...
    }

    @Test
    public void testGetNextNodePointer() {
        // ...
    }

    @Test
    public void testGetPositionAndSetPosition() {
        // ...
    }

    @Test
    public void testDOMAttributeIteratorEmptyList() {
        Node node = createTestElementEmpty();
        NodePointer nodePointer = new NodePointer(node);
        QName name = new QName("testAttribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNullNodePointer() {
        NodePointer nodePointer = null;
        QName name = new QName("testAttribute");
        try {
            DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDOMAttributeIteratorWithNullQName() {
        Node node = createTestElement();
        NodePointer nodePointer = new NodePointer(node);
        try {
            DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDOMAttributeIteratorWithTextNodeOrCommentNode() {
        Node node = createTestElementWithTextNode();
        NodePointer nodePointer = new NodePointer(node);
        QName name = new QName("testAttribute");
        try {
            DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDOMAttributeIteratorOutOfBounds() {
        Node node = createTestElement();
        NodePointer nodePointer = new NodePointer(node);
        QName name = new QName("testAttribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

        iterator.next();
        iterator.next();
        iterator.setPosition(-1);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyAttributeName() {
        Node node = createTestElement();
        NodePointer nodePointer = new NodePointer(node);
        QName name = new QName("");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNullAttributeNode() {
        Node node = createTestElement();
        NodePointer nodePointer = new NodePointer(node);
        QName name = new QName("testAttribute");
        try {
            DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
            iterator.next();
            Attr attribute = null;
            iterator.setNode(new DOMAttributePointer(nodePointer, attribute));
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDOMAttributeIteratorWithNullAttributeName() {
        Node node = createTestElement();
        NodePointer nodePointer = new NodePointer(node);
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNullNamespaceURI() {
        Node node = createTestElementWithNamespaces();
        NodePointer nodePointer = new NodePointer(node);
        QName name = new QName("", "testPrefix:testLocalName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNullParentNamespaceURI() {
        Node node = createTestElementWithNamespaces();
        NodePointer nodePointer = new NodePointer(node);
        QName name = new QName("testPrefix:testLocalName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespaceMismatch() {
        Node node = createTestElementWithNamespaces();
        NodePointer nodePointer = new NodePointer(node);
        QName name = new QName("testPrefix2:testLocalName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
        Assert.assertFalse(iterator.hasNext());
    }

    private Node createTestElement() {
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newElement("testElement");
        element.setAttribute("testAttribute", "testValue");
        return element;
    }

    private Node createTestElementEmpty() {
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newElement("testElement");
        return element;
    }

    private Node createTestElementWithTextNode() {
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newElement("testElement");
        Text textNode = document.createTextNode("Test Text Node");
        element.appendChild(textNode);
        return element;
    }

    private Node createTestElementWithNamespaces() {
        Document document = DocumentBuilderFactory.newInstance().newDocument();
        Element root = document.createElement("root");
        root.setAttribute("xmlns:testPrefix", "http://test.com/prefix");
        document.appendChild(root);

        Element element = document.createElement("testElement");
        element.setAttribute("testAttribute", "testValue");
        root.appendChild(element);

        return document;
    }
}