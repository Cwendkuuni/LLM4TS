package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_NameWithNamespace() {
        Element rootElement = new Element("root");
        rootElement.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
        rootElement.addAttribute("attr1", "value1", "xml");

        NodePointer parentPointer = new JDOMPointer(rootElement);
        QName qname = new QName("xml", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, qname);

        Attribute expectedAttribute = rootElement.getAttribute("attr1", "xml");
        NodePointer expectedNodePointer = new JDOMAttributePointer(parentPointer, expectedAttribute);
        NodePointer actualNodePointer = iterator.getNodePointer();

        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testJDOMAttributeIterator_NameWithoutNamespace() {
        Element rootElement = new Element("root");
        rootElement.addAttribute("attr1", "value1");

        NodePointer parentPointer = new JDOMPointer(rootElement);
        QName qname = new QName("", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, qname);

        Attribute expectedAttribute = rootElement.getAttribute("attr1");
        NodePointer expectedNodePointer = new JDOMAttributePointer(parentPointer, expectedAttribute);
        NodePointer actualNodePointer = iterator.getNodePointer();

        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testJDOMAttributeIterator_NameWildcardWithNamespace() {
        Element rootElement = new Element("root");
        rootElement.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
        rootElement.addAttribute("attr1_xml", "value1_xml", "xml");
        rootElement.addAttribute("attr2_xml", "value2_xml", "xml");

        NodePointer parentPointer = new JDOMPointer(rootElement);
        QName qname = new QName("xml", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, qname);

        List<Attribute> attributes = rootElement.getAttributes();
        List<NodePointer> expectedNodePointers = new ArrayList<>();

        for (Attribute attribute : attributes) {
            if (attribute.getNamespace().equals(Namespace.XML_NAMESPACE)) {
                expectedNodePointers.add(new JDOMAttributePointer(parentPointer, attribute));
            }
        }

        NodePointer expectedNodePointer;
        NodePointer actualNodePointer;

        for (int i = 0; i < expectedNodePointers.size(); i++) {
            expectedNodePointer = expectedNodePointers.get(i);
            actualNodePointer = iterator.getNodePointer();

            assertEquals(expectedNodePointer, actualNodePointer);
            iterator.setPosition(iterator.getPosition() + 1);
        }

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_NameWildcardWithoutNamespace() {
        Element rootElement = new Element("root");
        rootElement.addAttribute("attr1", "value1");
        rootElement.addAttribute("attr2", "value2");

        NodePointer parentPointer = new JDOMPointer(rootElement);
        QName qname = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, qname);

        List<Attribute> attributes = rootElement.getAttributes();
        List<NodePointer> expectedNodePointers = new ArrayList<>();

        for (Attribute attribute : attributes) {
            expectedNodePointers.add(new JDOMAttributePointer(parentPointer, attribute));
        }

        NodePointer expectedNodePointer;
        NodePointer actualNodePointer;

        for (int i = 0; i < expectedNodePointers.size(); i++) {
            expectedNodePointer = expectedNodePointers.get(i);
            actualNodePointer = iterator.getNodePointer();

            assertEquals(expectedNodePointer, actualNodePointer);
            iterator.setPosition(iterator.getPosition() + 1);
        }

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteator_EmptyList() {
        Element rootElement = new Element("root");
        NodePointer parentPointer = new JDOMPointer(rootElement);
        QName qname = new QName("", "attr1");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, qname);

        assertNull(iterator.getNodePointer());
    }
}