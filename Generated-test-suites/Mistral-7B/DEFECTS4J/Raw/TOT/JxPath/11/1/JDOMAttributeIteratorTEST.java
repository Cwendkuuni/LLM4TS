package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Attribute;
import org.jdom2.Namespace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

    private Document document;
    private Element rootElement;

    @Before
    public void setUp() {
        document = new Document();
        rootElement = new Element("root");
        document.setRootElement(rootElement);

        Element childElement1 = new Element("child1");
        childElement1.setAttribute("name1", "value1");
        rootElement.addContent(childElement1);

        Element childElement2 = new Element("child2");
        rootElement.addContent(childElement2);

        rootElement.setAttribute("name2", "value2");
    }

    @Test
    public void testEmptyIterator() {
        NodeIterator iterator = new JDOMAttributeIterator(new NodePointer(document), new QName("", "*"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleAttributeIterator() {
        NodeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("", "name1"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        assertEquals("name1", nodePointer.getNodeName().getLocalPart());
        assertEquals("value1", nodePointer.getNodeValue().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleAttributesIterator() {
        NodeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("", "*"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        assertEquals("name1", nodePointer.getNodeName().getLocalPart());
        assertEquals("value1", nodePointer.getNodeValue().getValue());
        assertTrue(iterator.hasNext());
        nodePointer = iterator.next();
        assertEquals("name2", nodePointer.getNodeName().getLocalPart());
        assertEquals("value2", nodePointer.getNodeValue().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNamespaceAttributeIterator() {
        Namespace ns = new Namespace("xml", "http://www.w3.org/XML/1998/namespace");
        QName name = new QName("", "xml:version", ns);
        NodeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), name);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNonExistentAttributeIterator() {
        NodeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("", "nonExistentName"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSetPosition() {
        NodeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("", "*"));
        iterator.next();
        iterator.next();
        iterator.setPosition(0);
        NodePointer nodePointer = iterator.next();
        assertEquals("name1", nodePointer.getNodeName().getLocalPart());
    }

    @Test
    public void testSetPositionOutOfRange() {
        NodeIterator iterator = new JDOMAttributeIterator(new NodePointer(rootElement), new QName("", "*"));
        iterator.next();
        iterator.next();
        iterator.setPosition(3);
        NodePointer nodePointer = iterator.next();
        assertNull(nodePointer);
    }
}