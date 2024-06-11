package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        NodePointer parent = new DOMNodePointer(null, null, null, "root", null);
        QName name = new QName("test", "namespace", "prefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertEquals(false, iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorWithAttribute() {
        NodePointer parent = new DOMNodePointer(null, null, null, "root", null);
        Element element = new org.w3c.dom.ElementImpl();
        element.setNodeName("root");
        parent.setNode(element);
        QName name = new QName("test", "namespace", "prefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attribute = new AttrImpl(); // Assuming AttrImpl is a valid implementation of Attr
        attribute.setNodeName("attrName");
        attribute.setNodeValue("attrValue");
        element.setAttribNode(attribute);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        assertEquals("attrName", attributePointer.getName().getName());
        assertEquals("attrValue", attributePointer.getValue());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorWithMultipleAttributes() {
        NodePointer parent = new DOMNodePointer(null, null, null, "root", null);
        Element element = new org.w3c.dom.ElementImpl();
        element.setNodeName("root");
        parent.setNode(element);
        QName name = new QName("test", "namespace", "prefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attribute1 = new AttrImpl(); // Assuming AttrImpl is a valid implementation of Attr
        attribute1.setNodeName("attr1");
        attribute1.setNodeValue("attr1Value");
        Element.setAttributeNode(element, attribute1);
        Attr attribute2 = new AttrImpl(); // Assuming AttrImpl is a valid implementation of Attr
        attribute2.setNodeName("attr2");
        attribute2.setNodeValue("attr2Value");
        Element.setAttributeNode(element, attribute2);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        DOMAttributePointer attributePointer1 = (DOMAttributePointer) nodePointer;
        assertEquals("attr1", attributePointer1.getName().getName());
        assertEquals("attr1Value", attributePointer1.getValue());
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertNotNull(nodePointer2);
        DOMAttributePointer attributePointer2 = (DOMAttributePointer) nodePointer2;
        assertEquals("attr2", attributePointer2.getName().getName());
        assertEquals("attr2Value", attributePointer2.getValue());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespaces() {
        NodePointer parent = new DOMNodePointer(null, null, null, "root", null);
        Element element = new org.w3c.dom.ElementImpl();
        element.setNodeName("root");
        parent.setNode(element);
        String namespaceURI = "http://example.com/namespace";
        QName name = new QName("test", namespaceURI, "prefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attribute = new AttrImpl(); // Assuming AttrImpl is a valid implementation of Attr
        attribute.setNodeName("attr");
        attribute.setNodeValue("attrValue");
        attribute.setNamespaceURI(namespaceURI);
        Element.setAttributeNode(element, attribute);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        assertEquals(namespaceURI, attributePointer.getNamespaceURI());
        assertEquals("attr", attributePointer.getName().getName());
        assertEquals("attrValue", attributePointer.getValue());
    }

    @Test
    public void testDOMAttributeIteratorWithMixedAttributes() {
        NodePointer parent = new DOMNodePointer(null, null, null, "root", null);
        Element element = new org.w3c.dom.ElementImpl();
        element.setNodeName("root");
        parent.setNode(element);
        String namespaceURI = "http://example.com/namespace";
        QName name = new QName("test", namespaceURI, "prefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attr1 = new AttrImpl(); // Assuming AttrImpl is a valid implementation of Attr
        attr1.setNodeName("attr1");
        attr1.setNodeValue("attr1Value");
        attr1.setNamespaceURI(namespaceURI);
        Element.setAttributeNode(element, attr1);
        Attr attr2 = new AttrImpl(); // Assuming AttrImpl is a valid implementation of Attr
        attr2.setNodeName("attr2");
        attr2.setNodeValue("attr2Value");
        attr2.setPrefix("xmlns");
        attr2.setNamespaceURI("http://example.com/namespace");
        Element.setAttributeNode(element, attr2);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        DOMAttributePointer attributePointer1 = (DOMAttributePointer) nodePointer;
        assertEquals(namespaceURI, attributePointer1.getNamespaceURI());
        assertEquals("attr1", attributePointer1.getName().getName());
        assertEquals("attr1Value", attributePointer1.getValue());
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertNotNull(nodePointer2);
        DOMAttributePointer attributePointer2 = (DOMAttributePointer) nodePointer2;
        assertEquals("xmlns", attributePointer2.getName().getPrefix());
        assertEquals("http://example.com/namespace", attributePointer2.getNamespaceURI());
        assertEquals("attr2", attributePointer2.getName().getName());
        assertEquals("attr2Value", attributePointer2.getValue());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyList() {
        NodePointer parent = new DOMNodePointer(null, null, null, "root", null);
        QName name = new QName("test", "namespace", "prefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

}