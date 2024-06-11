package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testCreateIteratorWithSimpleAttribute() {
        Element rootElement = new Element("root");
        Attribute simpleAttribute = new Attribute("simpleAttribute", "simpleValue", null);
        rootElement.addContent(simpleAttribute);

        NodePointer parentPointer = new JDOMPointer(new Element("parent"), null);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*", null));

        iterator.next();
        NodePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals(simpleAttribute, attributePointer.getNode());
    }

    @Test
    public void testCreateIteratorWithNamespaceAndAttribute() {
        Element rootElement = new Element("root");
        String namespaceUri = "http://example.com/";
        Namespace namespace = new Namespace(namespaceUri);
        Attribute namespaceAttribute = new Attribute("namespaceAttribute", "namespaceValue", namespace);
        rootElement.addNamespaceDeclaration(namespace);
        rootElement.addContent(namespaceAttribute);

        NodePointer parentPointer = new JDOMPointer(new Element("parent"), null);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("namespaceAttribute", namespaceUri));

        iterator.next();
        NodePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals(namespaceAttribute, attributePointer.getNode());
    }

    @Test
    public void testCreateIteratorWithEmptyList() {
        Element rootElement = new Element("root");
        NodePointer parentPointer = new JDOMPointer(rootElement, null);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*", null));

        assertNull(iterator.next());
    }

    @Test
    public void testSetPositionWithExistingAttribute() {
        Element rootElement = new Element("root");
        Attribute attribute1 = new Attribute("attribute1", "value1", null);
        Attribute attribute2 = new Attribute("attribute2", "value2", null);
        rootElement.addContent(attribute1);
        rootElement.addContent(attribute2);

        NodePointer parentPointer = new JDOMPointer(new Element("parent"), null);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*", null));

        iterator.next();
        iterator.setPosition(1);
        NodePointer secondAttributePointer = iterator.getNodePointer();
        assertEquals(attribute2, secondAttributePointer.getNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithInvalidPosition() {
        Element rootElement = new Element("root");
        Attribute attribute1 = new Attribute("attribute1", "value1", null);
        Attribute attribute2 = new Attribute("attribute2", "value2", null);
        rootElement.addContent(attribute1);
        rootElement.addContent(attribute2);

        NodePointer parentPointer = new JDOMPointer(new Element("parent"), null);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*", null));

        iterator.next();
        iterator.setPosition(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIteratorWithNullParent() {
        NodePointer parentPointer = null;
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIteratorWithNullQName() {
        NodePointer parentPointer = new JDOMPointer(new Element("parent"), null);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, null);
    }
}