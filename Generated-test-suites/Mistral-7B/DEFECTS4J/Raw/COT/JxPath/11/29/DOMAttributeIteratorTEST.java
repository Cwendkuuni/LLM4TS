package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.apache.commons.jxpath.ri.test.TestUtils.createElement;
import static org.apache.commons.jxpath.ri.test.TestUtils.createMockNode;
import static org.apache.commons.jxpath.ri.test.TestUtils.createMockQName;
import static org.apache.commons.jxpath.ri.test.TestUtils.createMockNodeIterator;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor_empty() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructor_singleAttribute() {
        Element element = createElement("root", null);
        Attr attribute = createMockAttr("test", "testValue");
        when(element.getAttributeNode(anyString())).thenReturn(attribute);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.next();
        Assert.assertEquals(nodePointer, new DOMAttributePointer(parent, attribute));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testConstructor_multipleAttributes() {
        Element element = createElement("root", null);
        Attr attribute1 = createMockAttr("test1", "testValue1");
        Attr attribute2 = createMockAttr("test2", "testValue2");
        Attr attribute3 = createMockAttr("test3", "testValue3");
        when(element.getAttributeNode(anyString())).thenReturn(attribute1).thenReturn(attribute2).thenReturn(attribute3);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer1 = iterator.next();
        Assert.assertEquals(nodePointer1, new DOMAttributePointer(parent, attribute1));
        NodePointer nodePointer2 = iterator.next();
        Assert.assertEquals(nodePointer2, new DOMAttributePointer(parent, attribute2));
        NodePointer nodePointer3 = iterator.next();
        Assert.assertEquals(nodePointer3, new DOMAttributePointer(parent, attribute3));
        Assert.assertFalse(iterator.hasPrevious());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointer_outOfBounds() {
        Element element = createElement("root", null);
        Attr attribute1 = createMockAttr("test1", "testValue1");
        Attr attribute2 = createMockAttr("test2", "testValue2");

        when(element.getAttributeNode(anyString())).thenReturn(attribute1).thenReturn(attribute2);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next(); // IndexOutOfBoundsException should be thrown here
    }

    @Test
    public void testGetPosition() {
        Element element = createElement("root", null);
        Attr attribute1 = createMockAttr("test1", "testValue1");
        Attr attribute2 = createMockAttr("test2", "testValue2");

        when(element.getAttributeNode(anyString())).thenReturn(attribute1).thenReturn(attribute2);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        Assert.assertEquals(iterator.getPosition(), 2);
    }

    @Test
    public void testSetPosition_success() {
        Element element = createElement("root", null);
        Attr attribute1 = createMockAttr("test1", "testValue1");
        Attr attribute2 = createMockAttr("test2", "testValue2");

        when(element.getAttributeNode(anyString())).thenReturn(attribute1).thenReturn(attribute2);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();

        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(iterator.getPosition(), 1);
        Assert.assertEquals(iterator.getCurrentNode(), attribute1);
    }

    @Test
    public void testSetPosition_failure() {
        Element element = createElement("root", null);
        Attr attribute1 = createMockAttr("test1", "testValue1");
        Attr attribute2 = createMockAttr("test2", "testValue2");

        when(element.getAttributeNode(anyString())).thenReturn(attribute1).thenReturn(attribute2);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();

        Assert.assertFalse(iterator.setPosition(3));
        Assert.assertEquals(iterator.getPosition(), 2);
    }

    @Test
    public void testHasNext_empty() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext_singleAttribute() {
        Element element = createElement("root", null);
        Attr attribute = createMockAttr("test", "testValue");
        when(element.getAttributeNode(anyString())).thenReturn(attribute);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testHasNext_multipleAttributes() {
        Element element = createElement("root", null);
        Attr attribute1 = createMockAttr("test1", "testValue1");
        Attr attribute2 = createMockAttr("test2", "testValue2");
        Attr attribute3 = createMockAttr("test3", "testValue3");
        when(element.getAttributeNode(anyString())).thenReturn(attribute1).thenReturn(attribute2).thenReturn(attribute3);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testHasPrevious_empty() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        Assert.assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testHasPrevious_singleAttribute() {
        Element element = createElement("root", null);
        Attr attribute = createMockAttr("test", "testValue");
        when(element.getAttributeNode(anyString())).thenReturn(attribute);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();

        Assert.assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testHasPrevious_multipleAttributes() {
        Element element = createElement("root", null);
        Attr attribute1 = createMockAttr("test1", "testValue1");
        Attr attribute2 = createMockAttr("test2", "testValue2");
        Attr attribute3 = createMockAttr("test3", "testValue3");
        when(element.getAttributeNode(anyString())).thenReturn(attribute1).thenReturn(attribute2).thenReturn(attribute3);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();

        Assert.assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testHasPrevious_lastAttribute() {
        Element element = createElement("root", null);
        Attr attribute1 = createMockAttr("test1", "testValue1");
        Attr attribute2 = createMockAttr("test2", "testValue2");
        Attr attribute3 = createMockAttr("test3", "testValue3");
        when(element.getAttributeNode(anyString())).thenReturn(attribute1).thenReturn(attribute2).thenReturn(attribute3);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        iterator.next();

        Assert.assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testHasPrevious_previousAttribute() {
        Element element = createElement("root", null);
        Attr attribute1 = createMockAttr("test1", "testValue1");
        Attr attribute2 = createMockAttr("test2", "testValue2");
        Attr attribute3 = createMockAttr("test3", "testValue3");
        when(element.getAttributeNode(anyString())).thenReturn(attribute1).thenReturn(attribute2).thenReturn(attribute3);

        NodePointer parent = createMockNodePointer("root", null);
        QName name = createMockQName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        iterator.next();

        Assert.assertTrue(iterator.hasPrevious());
        Assert.assertEquals(iterator.getCurrentNode(), attribute2);
        iterator.previous();
        Assert.assertTrue(iterator.hasPrevious());
        Assert.assertEquals(iterator.getCurrentNode(), attribute1);
        iterator.previous();
        Assert.assertFalse(iterator.hasPrevious());
    }

    private Attr createMockAttr(String localName, String value) {
        Attr attr = mock(Attr.class);
        when(attr.getName()).thenReturn(createMockQName(localName));
        when(attr.getValue()).thenReturn(value);
        return attr;
    }

    private Element createElement(String tagName, String namespaceURI) {
        Element element = mock(Element.class);
        when(element.getTagName()).thenReturn(tagName);
        when(element.getNamespaceURI()).thenReturn(namespaceURI);
        return element;
    }

    private Node createMockNode() {
        Node node = mock(Node.class);
        NodeList nodeList = createMockNodeList();
        when(node.getChildNodes()).thenReturn(nodeList);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        return node;
    }

    private NodeList createMockNodeList() {
        NodeList nodeList = mock(NodeList.class);
        when(nodeList.getLength()).thenReturn(3);
        return nodeList;
    }

    private NodePointer createMockNodePointer(String tagName, String namespaceURI) {
        Node node = createMockNode();
        when(node.getNodeName()).thenReturn(tagName);
        when(node.getNamespaceURI()).thenReturn(namespaceURI);
        return new DOMAttributePointer(createMockNodePointer("root", null), node);
    }

    private QName createMockQName(String localName) {
        QName qname = new QName();
        qname.setLocalPart(localName);
        return qname;
    }

    private NodePointer createMockNodePointer(String nodeName, Node node) {
        NodePointer nodePointer = mock(NodePointer.class);
        when(nodePointer.getNodeName()).thenReturn(new QName(null, null, nodeName));
        when(nodePointer.getNode()).thenReturn(node);
        return nodePointer;
    }
}