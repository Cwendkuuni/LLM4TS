package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator.DOMAttributePointer;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        Node root = new Element("root");
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("*"));
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testConstructorWithSpecificName() {
        Element root = new Element("root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
        root.setAttributeNS("", "class", "test");
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("http://www.w3.org/2000/xmlns/", "xmlns"));
        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", "class"));
        NodePointer next = iterator.next();
        Assert.assertNotNull(next);
        Assert.assertEquals("test", next.getNodeValue());
    }

    @Test
    public void testNextAndHasNext() {
        Element root = new Element("root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
        root.setAttributeNS("", "class", "test1");
        root.setAttributeNS("", "class", "test2");
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", "class"));
        Assert.assertNotNull(iterator.next());
        Assert.assertEquals("test1", iterator.next().getNodeValue());
        Assert.assertEquals("test2", iterator.next().getNodeValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNextWhenNoMoreElements() {
        Element root = new Element("root");
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", "class"));
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testGetPosition() {
        Element root = new Element("root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
        root.setAttributeNS("", "class", "test1");
        root.setAttributeNS("", "class", "test2");
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", "class"));
        Assert.assertEquals(0, iterator.getPosition());
        iterator.next();
        Assert.assertEquals(1, iterator.getPosition());
        iterator.next();
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element root = new Element("root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
        root.setAttributeNS("", "class", "test1");
        root.setAttributeNS("", "class", "test2");
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", "class"));
        iterator.setPosition(1);
        Assert.assertEquals("test1", iterator.next().getNodeValue());
        iterator.setPosition(2);
        Assert.assertEquals("test2", iterator.next().getNodeValue());
        iterator.setPosition(3);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSetPositionOutOfBounds() {
        Element root = new Element("root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
        root.setAttributeNS("", "class", "test1");
        root.setAttributeNS("", "class", "test2");
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", "class"));
        iterator.setPosition(10);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetNodePointer() {
        Element root = new Element("root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
        root.setAttributeNS("", "class", "test1");
        root.setAttributeNS("", "class", "test2");
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", "class"));
        NodePointer nodePointer = iterator.next();
        Assert.assertEquals("test1", nodePointer.getNodeValue());
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        Assert.assertEquals("test1", attributePointer.getAttributeName());
        Assert.assertEquals(root, attributePointer.getParent());
    }

    @Test
    public void testAttributeIteratorWithEmptyElement() {
        Element root = new Element("root");
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", "*"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAttributeIteratorWithEmptyAttribute() {
        Element root = new Element("root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
        root.setAttributeNode(new Attr("class", ""));
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", "class"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAttributeIteratorWithEmptyNamespace() {
        Element root = new Element("root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
        root.setAttributeNode(new Attr("class", ""));
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", ""));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAttributeIteratorWithNamespace() {
        Element root = new Element("root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
        root.setAttributeNode(new Attr("xml:class", "test"));
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("http://www.w3.org/2000/xmlns/", "xml"));
        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new NodePointer(root), new QName("http://www.w3.org/2000/xmlns/", "xml:class"));
        Assert.assertEquals("test", iterator.next().getNodeValue());
    }

    @Test
    public void testAttributeIteratorWithMultipleAttributes() {
        Element root = new Element("root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
        root.setAttributeNode(new Attr("a", "value1"));
        root.setAttributeNode(new Attr("b", "value2"));
        root.setAttributeNode(new Attr("c", "value3"));
        NodeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("", "*"));
        List<NodePointer> nodes = new ArrayList<>();
        while (iterator.hasNext()) {
            nodes.add(iterator.next());
        }
        Assert.assertEquals(3, nodes.size());
        Assert.assertEquals("value1", nodes.get(0).getNodeValue());
        Assert.assertEquals("value2", nodes.get(1).getNodeValue());
        Assert.assertEquals("value3", nodes.get(2).getNodeValue());
    }
}