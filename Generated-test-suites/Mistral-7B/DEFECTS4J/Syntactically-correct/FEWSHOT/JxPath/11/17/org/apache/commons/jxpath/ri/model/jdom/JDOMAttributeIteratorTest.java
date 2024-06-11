package org.apache.commons.cli.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodePointerFactory;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_whenNamespaceIsProvidedAndAttributeExists_thenCorrectlyIterates() {
        // Given
        Element element = new Element("root");
        Namespace namespace = new Namespace("ns", "http://example.com/");
        String attributeName = "attributeName";
        Attribute attribute = new Attribute("name", "value", namespace);
        attribute.setNamespace(namespace);
        element.addContent(attribute);

        NodePointer nodePointer = new JDOMNodePointer(element);
        NodePointerFactory nodePointerFactory = new JDOMNodePointerFactory();

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(namespace.getNamespaceURI(), attributeName));

        // Then
        NodePointer nodePointerExpected = new JDOMAttributePointer(nodePointer, attribute);
        NodePointer nodePointerActual;
        int position = 0;

        for (int i = 0; i < 2; i++) {
            nodePointerActual = iterator.getNodePointer();
            assertEquals(nodePointerExpected, nodePointerActual);
            nodePointerExpected = new JDOMAttributePointer(nodePointer, (Attribute) iterator.attributes.get(1));
            position++;
            iterator.setPosition(position);
        }

        // It should return null, since the position is greater than the size of the attributes
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_whenNamespaceIsProvidedAndNoAttributeExists_thenReturnsEmptyList() {
        // Given
        Element element = new Element("root");
        Namespace namespace = new Namespace("ns", "http://example.com/");
        String attributeName = "attributeName";
        Element emptyElement = new Element("root");

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(emptyElement), new QName(namespace.getNamespaceURI(), attributeName));

        // Then
        assertFalse(iterator.hasNext());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_whenNamespaceIsNotProvidedAndAttributeExists_thenCorrectlyIterates() {
        // Given
        Element element = new Element("root");
        String attributeName = "attributeName";
        Attribute attribute = new Attribute("name", "value");
        element.addContent(attribute);

        NodePointer nodePointer = new JDOMNodePointer(element);
        NodePointerFactory nodePointerFactory = new JDOMNodePointerFactory();

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(attributeName));

        // Then
        NodePointer nodePointerExpected = new JDOMAttributePointer(nodePointer, attribute);
        NodePointer nodePointerActual;
        int position = 0;

        for (int i = 0; i < 2; i++) {
            nodePointerActual = iterator.getNodePointer();
            assertEquals(nodePointerExpected, nodePointerActual);
            nodePointerExpected = new JDOMAttributePointer(nodePointer, (Attribute) iterator.attributes.get(1));
            position++;
            iterator.setPosition(position);
        }

        // It should return null, since the position is greater than the size of the attributes
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_whenNamespaceIsNotProvidedAndNoAttributeExists_thenReturnsEmptyList() {
        // Given
        Element element = new Element("root");
        String attributeName = "attributeName";
        Element emptyElement = new Element("root");

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(emptyElement), new QName(attributeName));

        // Then
        assertFalse(iterator.hasNext());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_whenNamespaceIsNotProvidedAndAttributeNameIsWildcard_thenCorrectlyIterates() {
        // Given
        Element element = new Element("root");
        Namespace namespace = new Namespace("ns", "http://example.com/");
        Attribute attribute1 = new Attribute("name1", "value1", namespace);
        attribute1.setNamespace(namespace);
        Attribute attribute2 = new Attribute("name2", "value2", namespace);
        attribute2.setNamespace(namespace);
        element.addContent(attribute1);
        element.addContent(attribute2);

        NodePointer nodePointer = new JDOMNodePointer(element);
        NodePointerFactory nodePointerFactory = new JDOMNodePointerFactory();

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(""));

        // Then
        List<Attribute> attributes = (List<Attribute>) element.getAttributes();
        int position = 0;

        while (iterator.hasNext()) {
            NodePointer nodePointerActual = iterator.getNodePointer();
            Attribute attributeActual = (Attribute) nodePointerActual.getNode();
            Attribute attributeExpected = attributes.get(position);

            assertEquals(attributeExpected.getName(), attributeActual.getName());
            assertEquals(attributeExpected.getValue(), attributeActual.getValue());
            position++;
            iterator.setPosition(position);
        }

        // It should return false, since the position is greater than the size of the attributes
        assertFalse(iterator.hasNext());
        assertNull(iterator.getNodePointer());
    }
}