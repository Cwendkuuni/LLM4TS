package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new DOMPointNodePointer(null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        String xml = "<root xmlns:x=\"http://www.w3.org/2000/xmlns/\">" +
                "<element attr1=\"value1\" xmlns:y=\"http://www.w3.org/2001/XMLSchema\"/>" +
                "</root>";
        Document doc = JaxpHelper.parseStringToDocument(xml);
        NodePointer root = new DOMPointNodePointer(doc.getDocumentElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr1"));
        NodePointer attributePointer = iterator.next();
        assertNotNull(attributePointer);
        assertInstanceOf(DOMAttributePointer.class, attributePointer);
        Attr attr = (Attr) attributePointer.getNode();
        assertEquals("value1", attr.getNodeValue());
        assertEquals("attr1", attr.getNodeName());
        assertEquals("http://www.w3.org/2000/xmlns/", attr.getNamespaceURI());
        assertEquals("xmlns:x", attr.getPrefix());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        String xml = "<root xmlns:x=\"http://www.w3.org/2000/xmlns/\">" +
                "<element attr1=\"value1\" attr2=\"value2\" xmlns:y=\"http://www.w3.org/2001/XMLSchema\"/>" +
                "</root>";
        Document doc = JaxpHelper.parseStringToDocument(xml);
        NodePointer root = new DOMPointNodePointer(doc.getDocumentElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr"));
        NodePointer attrPointer1 = iterator.next();
        assertNotNull(attrPointer1);
        assertInstanceOf(DOMAttributePointer.class, attrPointer1);
        Attr attr1 = (Attr) attrPointer1.getNode();
        assertEquals("value1", attr1.getNodeValue());
        assertEquals("attr1", attr1.getNodeName());
        assertEquals("http://www.w3.org/2000/xmlns/", attr1.getNamespaceURI());
        assertEquals("xmlns:x", attr1.getPrefix());

        NodePointer attrPointer2 = iterator.next();
        assertNotNull(attrPointer2);
        assertInstanceOf(DOMAttributePointer.class, attrPointer2);
        Attr attr2 = (Attr) attrPointer2.getNode();
        assertEquals("value2", attr2.getNodeValue());
        assertEquals("attr2", attr2.getNodeName());
        assertEquals("http://www.w3.org/2001/XMLSchema", attr2.getNamespaceURI());
        assertEquals("xmlns:y", attr2.getPrefix());

        NodePointer attrPointer3 = iterator.next();
        assertNull(attrPointer3);
    }

    @Test
    public void testIteratorWithNamespacePrefix() {
        String xml = "<root xmlns:x=\"http://www.w3.org/2000/xmlns/\" xmlns:y=\"http://www.w3.org/2001/XMLSchema\"><element attr1:x=\"value1\" attr2:y=\"value2\"/></root>";
        Document doc = JaxpHelper.parseStringToDocument(xml);
        NodePointer root = new DOMPointNodePointer(doc.getDocumentElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr1"));
        NodePointer attrPointer = iterator.next();
        assertNotNull(attrPointer);
        assertInstanceOf(DOMAttributePointer.class, attrPointer);
        Attr attr = (Attr) attrPointer.getNode();
        assertEquals("value1", attr.getNodeValue());
        assertEquals("attr1", attr.getNodeName());
        assertEquals("http://www.w3.org/2000/xmlns/", attr.getNamespaceURI());
        assertEquals("x", attr.getPrefix());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithWildcard() {
        String xml = "<root xmlns:x=\"http://www.w3.org/2000/xmlns/\">" +
                "<element attr1=\"value1\" attr2=\"value2\" xmlns:y=\"http://www.w3.org/2001/XMLSchema\"/>" +
                "</root>";
        Document doc = JaxpHelper.parseStringToDocument(xml);
        NodePointer root = new DOMPointNodePointer(doc.getDocumentElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName(""));
        NodePointer attrPointer1 = iterator.next();
        assertNotNull(attrPointer1);
        assertInstanceOf(DOMAttributePointer.class, attrPointer1);
        Attr attr1 = (Attr) attrPointer1.getNode();
        assertEquals("value1", attr1.getNodeValue());
        assertEquals("attr1", attr1.getNodeName());
        assertEquals("http://www.w3.org/2000/xmlns/", attr1.getNamespaceURI());
        assertEquals("xmlns:x", attr1.getPrefix());

        NodePointer attrPointer2 = iterator.next();
        assertNotNull(attrPointer2);
        assertInstanceOf(DOMAttributePointer.class, attrPointer2);
        Attr attr2 = (Attr) attrPointer2.getNode();
        assertEquals("value2", attr2.getNodeValue());
        assertEquals("attr2", attr2.getNodeName());
        assertEquals("http://www.w3.org/2001/XMLSchema", attr2.getNamespaceURI());
        assertEquals("xmlns:y", attr2.getPrefix());

        NodePointer attrPointer3 = iterator.next();
        assertNull(attrPointer3);
    }

    @Test
    public void testIteratorWithNamespaceURI() {
        String xml = "<root xmlns:x=\"http://www.w3.org/2000/xmlns/\" xmlns:y=\"http://www.w3.org/2001/XMLSchema\"><element attr1:x=\"value1\" attr2:y=\"value2\"/></root>";
        Document doc = JaxpHelper.parseStringToDocument(xml);
        NodePointer root = new DOMPointNodePointer(doc.getDocumentElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr1", "http://www.w3.org/2000/xmlns/"));
        NodePointer attrPointer = iterator.next();
        assertNotNull(attrPointer);
        assertInstanceOf(DOMAttributePointer.class, attrPointer);
        Attr attr = (Attr) attrPointer.getNode();
        assertEquals("value1", attr.getNodeValue());
        assertEquals("attr1", attr.getNodeName());
        assertEquals("http://www.w3.org/2000/xmlns/", attr.getNamespaceURI());
        assertEquals("xmlns:x", attr.getPrefix());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithInvalidNamespacePrefix() {
        String xml = "<root xmlns:x=\"http://www.w3.org/2000/xmlns/\" xmlns:y=\"http://www.w3.org/2001/XMLSchema\"><element attr1:z=\"value1\" attr2:y=\"value2\"/></root>";
        Document doc = JaxpHelper.parseStringToDocument(xml);
        NodePointer root = new DOMPointNodePointer(doc.getDocumentElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr1", "z"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithInvalidNamespaceURI() {
        String xml = "<root xmlns:x=\"http://www.w3.org/2000/xmlns/\" xmlns:y=\"http://www.w3.org/2001/XMLSchema\"><element attr1:x=\"value1\" attr2:y=\"value2\"/></root>";
        Document doc = JaxpHelper.parseStringToDocument(xml);
        NodePointer root = new DOMPointNodePointer(doc.getDocumentElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr1", "http://www.w3.org/2002/XMLSchema"));
        assertFalse(iterator.hasNext());
    }
}