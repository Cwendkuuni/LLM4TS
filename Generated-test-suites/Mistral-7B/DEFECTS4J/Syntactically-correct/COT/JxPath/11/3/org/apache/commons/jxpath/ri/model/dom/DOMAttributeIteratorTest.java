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

import static org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator.equalStrings;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_withValidArguments() {
        Node node = mockNode();
        Element element = mockElement(node);
        QName name = new QName("test", "testNS", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(node), name);
        NodePointer expected = new DOMAttributePointer(new DOMPoint(node), mockAttr(node, "test", "testNS", "testPrefix"));
        NodePointer actual = iterator.getNodePointer();
        Assert.assertSame("getNodePointer should return expected NodePointer", expected, actual);
    }

    @Test
    public void testDOMAttributeIterator_withEmptyElement() {
        Node node = mockNode();
        Element element = mockEmptyElement(node);
        QName name = new QName("test", "testNS", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(node), name);
        Assert.assertFalse("getNodePointer should return null when empty Element", iterator.getNodePointer() instanceof DOMAttributePointer);
    }

    @Test
    public void testDOMAttributeIterator_withNullParent() {
        QName name = new QName("test", "testNS", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, name);
        Assert.assertNull("iterator should be null when parent is null");
    }

    @Test
    public void testDOMAttributeIterator_withNullName() {
        Node node = mockNode();
        Element element = mockElement(node);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(node), null);
        Assert.assertNull("iterator should be null when name is null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIterator_withNullParentAndName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        Assert.fail("Should throw IllegalArgumentException when both parent and name are null");
    }

    @Test
    public void testDOMAttributeIterator_withEmptyList() {
        Node node = mockNode();
        Element element = mockElement(node);
        QName name = new QName("test", "testNS", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(node), name);
        iterator.setPosition(attributesSize() + 1);
        NodePointer actual = iterator.getNodePointer();
        Assert.assertNull("getNodePointer should return null when position is greater than the size of the list");
    }

    @Test
    public void testDOMAttributeIterator_withInvalidPosition() {
        Node node = mockNode();
        Element element = mockElement(node);
        QName name = new QName("test", "testNS", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(node), name);
        iterator.setPosition(-1);
        NodePointer actual = iterator.getNodePointer();
        Assert.assertNull("getNodePointer should return null when position is less than 0");
    }

    @Test
    public void testDOMAttributeIterator_withSamePosition() {
        Node node = mockNode();
        Element element = mockElement(node);
        QName name = new QName("test", "testNS", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(node), name);
        iterator.setPosition(1);
        NodePointer expected = new DOMAttributePointer(new DOMPoint(node), mockAttr(node, "test", "testNS", "testPrefix"));
        NodePointer actual = iterator.getNodePointer();
        Assert.assertSame("getNodePointer should return expected NodePointer", expected, actual);
        NodePointer next = iterator.getNodePointer();
        Assert.assertSame("getNodePointer should return the same NodePointer when position is not changed", expected, next);
    }

    @Test
    public void testDOMAttributeIterator_withNextPosition() {
        Node node = mockNode();
        Element element = mockElement(node);
        QName name = new QName("test", "testNS", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(node), name);
        iterator.setPosition(1);
        NodePointer expected = new DOMAttributePointer(new DOMPoint(node), mockAttr(node, "test2", "testNS2", "testPrefix2"));
        NodePointer actual = iterator.getNodePointer();
        Assert.assertSame("getNodePointer should return expected NodePointer", expected, actual);
    }

    @Test
    public void testDOMAttributeIterator_withPreviousPosition() {
        Node node = mockNode();
        Element element = mockElement(node);
        QName name = new QName("test", "testNS", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(node), name);
        iterator.setPosition(2);
        NodePointer expected = new DOMAttributePointer(new DOMPoint(node), mockAttr(node, "test", "testNS", "testPrefix"));
        NodePointer actual = iterator.getNodePointer();
        Assert.assertSame("getNodePointer should return expected NodePointer", expected, actual);
    }

    private Node mockNode() {
        return mock(Node.class);
    }

    private Element mockElement(Node node) {
        return mock(Element.class, new Object[]{node});
    }

    private Element mockEmptyElement(Node node) {
        Element element = mock(Element.class, new Object[]{node});
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(element.getAttributes().getLength()).thenReturn(0);
        return element;
    }

    private Attr mockAttr(Node node, String localName, String namespaceURI, String prefix) {
        Attr attr = mock(Attr.class, new Object[]{node, localName, namespaceURI, prefix});
        when(attr.getNodeName()).thenReturn(localName);
        when(attr.getNamespaceURI()).thenReturn(namespaceURI);
        when(attr.getPrefix()).thenReturn(prefix);
        when(attr.getValue()).thenReturn("value");
        return attr;
    }

    private int attributesSize() {
        return 3;
    }

    private static boolean equalStrings(String s1, String s2) {
        return DOMAttributeIteratorTest.equalStrings(s1, s2);
    }
}