package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DOMMockElement();
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        assertNotNull(iterator);
    }

    @Test
    public void testEmptyIterator() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DOMMockElement();
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorPosition() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DOMMockElement();
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        iterator.setPosition(1);
        Attr attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(attribute.getNodeValue(), "test1");
        iterator.setPosition(2);
        attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(attribute.getNodeValue(), "test2");
    }

    @Test
    public void testIteratorGetFirstAttribute() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DOMMockElement();
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        iterator.setPosition(1);
        Attr attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(attribute.getNodeValue(), "test1");
        iterator.setPosition(0);
        attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(attribute.getNodeValue(), "test0");
    }

    @Test
    public void testIteratorGetLastAttribute() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DOMMockElement();
        element.setAttributeNode(new Attr("test2", "test2"));
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        iterator.setPosition(2);
        Attr attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(attribute.getNodeValue(), "test2");
        iterator.setPosition(3);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorGetAttributeByPrefixAndLocalName() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DOMMockElement();
        element.setAttributeNode(new Attr("xmlns:x", "http://test.com/x"));
        element.setAttributeNode(new Attr("x:test1", "test1"));
        element.setAttributeNode(new Attr("test2", "test2"));
        QName name = new QName("x", "http://test.com", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        iterator.setPosition(1);
        Attr attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(attribute.getNodeValue(), "test1");
    }

    @Test
    public void testIteratorGetAttributeByLocalName() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DOMMockElement();
        element.setAttributeNode(new Attr("test1", "test1"));
        element.setAttributeNode(new Attr("test2", "test2"));
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        iterator.setPosition(1);
        Attr attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(attribute.getNodeValue(), "test1");
        iterator.setPosition(2);
        attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(attribute.getNodeValue(), "test2");
    }

    @Test
    public void testIteratorGetAttributeByLocalNameAndNamespaceUri() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DOMMockElement();
        element.setAttributeNode(new Attr("xmlns:x", "http://test.com/x"));
        element.setAttributeNode(new Attr("x:test1", "test1"));
        element.setAttributeNode(new Attr("test2", "test2"));
        QName name = new QName("x", "http://test.com", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        iterator.setPosition(1);
        Attr attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(attribute.getNodeValue(), "test1");
    }

    @Test
    public void testIteratorGetAttributeByPrefixAndLocalNameAndNamespaceUri() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DOMMockElement();
        element.setAttributeNode(new Attr("xmlns:x", "http://test.com/x"));
        element.setAttributeNode(new Attr("x:test1", "test1"));
        element.setAttributeNode(new Attr("test2", "test2"));
        QName name = new QName("x", "http://test.com", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        iterator.setPosition(1);
        Attr attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(attribute.getNodeValue(), "test1");
    }
}

class DOMMockElement implements Element {
    private List<Attr> attributes = new ArrayList<>();

    @Override
    public String getAttribute(String name) {
        for (Attr attr : attributes) {
            if (attr.getNodeName().equals(name)) {
                return attr.getNodeValue();
            }
        }
        return null;
    }

    @Override
    public String getAttributeNS(String namespaceURI, String localName) {
        for (Attr attr : attributes) {
            if (attr.getNamespaceURI().equals(namespaceURI) && attr.getLocalName().equals(localName)) {
                return attr.getNodeValue();
            }
        }
        return null;
    }

    @Override
    public Attr getAttributeNode(String name) {
        for (Attr attr : attributes) {
            if (attr.getNodeName().equals(name)) {
                return attr;
            }
        }
        return null;
    }

    @Override
    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
        for (Attr attr : attributes) {
            if (attr.getNamespaceURI().equals(namespaceURI) && attr.getLocalName().equals(localName)) {
                return attr;
            }
        }
        return null;
    }

    @Override
    public void setAttributeNode(Attr attr) {
        if (attr != null) {
            attributes.add(attr);
        }
    }

    @Override
    public void setAttributeNodeNS(Attr attr) {
        if (attr != null) {
            attributes.add(attr);
        }
    }
}

class DOMPointer implements NodePointer {
    private Node node;

    public DOMPointer(Node node) {
        this.node = node;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public String getNamespaceURI(String prefix) {
        return node.getNamespaceURI();
    }
}