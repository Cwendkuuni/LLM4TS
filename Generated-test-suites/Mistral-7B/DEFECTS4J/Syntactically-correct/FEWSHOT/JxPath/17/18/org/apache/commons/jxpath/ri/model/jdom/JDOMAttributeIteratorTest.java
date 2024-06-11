package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorWithNamespace() {
        Element rootElement = new Element("root");
        rootElement.addNamespaceDeclaration("ns1", "http://test.com/namespace1");
        rootElement.addNamespaceDeclaration("ns2", "http://test.com/namespace2");

        Element childElement = new Element("child");
        childElement.addNamespaceDeclaration("ns1", "http://test.com/namespace1");
        childElement.addAttribute("attr1", "value1", "ns1");
        childElement.addAttribute("attr2", "value2", "ns2");
        rootElement.addContent(childElement);

        NodePointer nodePointer = new NodePointer(rootElement);
        QName qname = new QName("ns1:attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);

        NodePointer firstAttribute = iterator.getNodePointer();
        assertEquals("attr1", firstAttribute.getName().getName());
        assertEquals("value1", firstAttribute.getValue());
        assertEquals("http://test.com/namespace1", firstAttribute.getNamespaceURI());

        NodePointer secondAttribute = iterator.getNodePointer();
        assertEquals("attr2", secondAttribute.getName().getName());
        assertEquals("value2", secondAttribute.getValue());
        assertEquals("http://test.com/namespace2", secondAttribute.getNamespaceURI());

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithWildcard() {
        Element rootElement = new Element("root");
        rootElement.addAttribute("attr1", "value1");
        rootElement.addAttribute("attr2", "value2");
        rootElement.addAttribute("attr3", "value3");

        NodePointer nodePointer = new NodePointer(rootElement);
        QName qname = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);

        NodePointer firstAttribute = iterator.getNodePointer();
        assertEquals("attr1", firstAttribute.getName().getName());
        assertEquals("value1", firstAttribute.getValue());
        assertNull(firstAttribute.getNamespaceURI());

        NodePointer secondAttribute = iterator.getNodePointer();
        assertEquals("attr2", secondAttribute.getName().getName());
        assertEquals("value2", secondAttribute.getValue());
        assertNull(secondAttribute.getNamespaceURI());

        NodePointer thirdAttribute = iterator.getNodePointer();
        assertEquals("attr3", thirdAttribute.getName().getName());
        assertEquals("value3", thirdAttribute.getValue());
        assertNull(thirdAttribute.getNamespaceURI());

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithEmptyList() {
        Element rootElement = new Element("root");
        NodePointer nodePointer = new NodePointer(rootElement);
        QName qname = new QName("ns:attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);

        assertNull(iterator.getNodePointer());
    }
}