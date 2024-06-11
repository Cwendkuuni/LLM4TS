package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorWithSingleAttribute() {
        Element rootElement = new Element("root");
        rootElement.addAttribute(new Attribute("attr1", "value1", "ns1"));

        NodePointer parentNode = new NodePointer(rootElement);
        QName attributeName = new QName("ns1:attr1");

        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);

        NodePointer attributeNodePointer = attributeIterator.getNodePointer();
        assertEquals(attributeName.getLocalPart(), attributeNodePointer.getNodeName().getLocalPart());
        assertEquals("value1", attributeNodePointer.getNodeValue());

        attributeIterator.setPosition(1);
        assertNull(attributeIterator.getNodePointer());

        attributeIterator.setPosition(0);
        assertEquals(attributeNodePointer, attributeIterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithMultipleAttributes() {
        Element rootElement = new Element("root");
        rootElement.addAttribute(new Attribute("attr1", "value1", "ns1"));
        rootElement.addAttribute(new Attribute("attr2", "value2", "ns2"));

        NodePointer parentNode = new NodePointer(rootElement);
        QName attributeName = new QName("*");

        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);

        NodePointer firstAttributeNodePointer = attributeIterator.getNodePointer();
        assertEquals("attr1", firstAttributeNodePointer.getNodeName().getLocalPart());
        assertEquals("value1", firstAttributeNodePointer.getNodeValue());

        NodePointer secondAttributeNodePointer = attributeIterator.getNodePointer();
        assertEquals("attr2", secondAttributeNodePointer.getNodeName().getLocalPart());
        assertEquals("value2", secondAttributeNodePointer.getNodeValue());

        NodePointer thirdAttributeNodePointer = attributeIterator.getNodePointer();
        assertNull(thirdAttributeNodePointer);

        attributeIterator.setPosition(2);
        assertNull(attributeIterator.getNodePointer());

        attributeIterator.setPosition(1);
        assertEquals(secondAttributeNodePointer, attributeIterator.getNodePointer());

        attributeIterator.setPosition(0);
        assertEquals(firstAttributeNodePointer, attributeIterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithEmptyElement() {
        Element rootElement = new Element("root");

        NodePointer parentNode = new NodePointer(rootElement);
        QName attributeName = new QName("*");

        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);

        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithEmptyList() {
        Element rootElement = new Element("root");

        NodePointer parentNode = new NodePointer(rootElement);
        QName attributeName = new QName("ns1:attr1");

        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);

        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithNullParent() {
        QName attributeName = new QName("ns1:attr1");

        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(null, attributeName);

        assertNull(attributeIterator.getNodePointer());
    }
}