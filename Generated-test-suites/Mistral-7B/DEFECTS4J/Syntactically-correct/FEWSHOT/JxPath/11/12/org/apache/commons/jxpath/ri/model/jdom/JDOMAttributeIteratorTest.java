package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorCreationWithEmptyElement() {
        NodePointer parentNode = new JDOMNodePointer(new Element(""));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName(""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorCreationWithEmptyNamespace() {
        Element element = new Element("root");
        NodePointer parentNode = new JDOMNodePointer(element);
        QName qname = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, qname);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorCreationWithSingleNamespaceAndName() {
        Element element = new Element("root", "xmlns:x");
        Namespace xmlnsX = element.getNamespace("x");
        NodePointer parentNode = new JDOMNodePointer(element);
        QName qname = new QName("x", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, qname);
        Attribute attribute = new Attribute("name", "value");
        attribute.setNamespace(xmlnsX);
        assertEquals(new JDOMAttributePointer(parentNode, attribute), iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorCreationWithSingleNamespaceAndWildcardName() {
        Element element = new Element("root", "xmlns:x");
        Namespace xmlnsX = element.getNamespace("x");
        NodePointer parentNode = new JDOMNodePointer(element);
        QName qname = new QName("x", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, qname);
        List attributes = element.getAttributes();
        Attribute firstAttribute = (Attribute) attributes.get(0);
        Attribute secondAttribute = (Attribute) attributes.get(1);
        if (firstAttribute.getNamespace().equals(xmlnsX)) {
            assertEquals(new JDOMAttributePointer(parentNode, firstAttribute), iterator.getNodePointer());
            assertTrue(iterator.setPosition(1));
            assertEquals(new JDOMAttributePointer(parentNode, secondAttribute), iterator.getNodePointer());
        } else {
            assertEquals(new JDOMAttributePointer(parentNode, secondAttribute), iterator.getNodePointer());
            assertTrue(iterator.setPosition(1));
            assertEquals(new JDOMAttributePointer(parentNode, firstAttribute), iterator.getNodePointer());
        }
    }

    @Test
    public void testJDOMAttributeIteratorCreationWithMultipleNamespacesAndName() {
        Element element = new Element("root");
        Namespace xmlnsX = Namespace.getNamespace("x");
        Namespace xmlnsY = Namespace.getNamespace("y");
        element.setNamespaceBinding("xmlns:x", xmlnsX);
        element.setNamespaceBinding("xmlns:y", xmlnsY);
        NodePointer parentNode = new JDOMNodePointer(element);
        QName qname = new QName("x", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, qname);
        Attribute attribute = new Attribute("name", "value");
        attribute.setNamespace(xmlnsX);
        assertEquals(new JDOMAttributePointer(parentNode, attribute), iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorCreationWithMultipleNamespacesAndWildcardName() {
        Element element = new Element("root");
        Namespace xmlnsX = Namespace.getNamespace("x");
        Namespace xmlnsY = Namespace.getNamespace("y");
        element.setNamespaceBinding("xmlns:x", xmlnsX);
        element.setNamespaceBinding("xmlns:y", xmlnsY);
        NodePointer parentNode = new JDOMNodePointer(element);
        QName qname = new QName("*", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, qname);
        List attributes = element.getAttributes();
        int size = attributes.size();
        for (int i = 0; i < size; i++) {
            Attribute attribute = (Attribute) attributes.get(i);
            if (attribute.getNamespace().equals(xmlnsX)) {
                assertEquals(new JDOMAttributePointer(parentNode, attribute), iterator.getNodePointer());
                assertTrue(iterator.setPosition(i + 1));
            }
        }
    }

    @Test
    public void testJDOMAttributeIteratorGettingNextAttribute() {
        Element element = new Element("root");
        NodePointer parentNode = new JDOMNodePointer(element);
        QName qname = new QName("", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, qname);
        List attributes = new ArrayList();
        attributes.add(new Attribute("name1", "value1"));
        attributes.add(new Attribute("name2", "value2"));
        element.setAttributes(attributes);
        for (int i = 0; i < 3; i++) {
            NodePointer nextAttribute = iterator.getNodePointer();
            if (i == 0) {
                assertEquals(new JDOMAttributePointer(parentNode, (Attribute) attributes.get(0)), nextAttribute);
            } else {
                assertEquals(new JDOMAttributePointer(parentNode, (Attribute) attributes.get(1)), nextAttribute);
            }
            assertTrue(iterator.setPosition(iterator.getPosition() + 1));
        }
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorSettingPositionToOutOfBoundsValue() {
        Element element = new Element("root");
        NodePointer parentNode = new JDOMNodePointer(element);
        QName qname = new QName("", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, qname);
        List attributes = new ArrayList();
        attributes.add(new Attribute("name1", "value1"));
        attributes.add(new Attribute("name2", "value2"));
        element.setAttributes(attributes);
        assertFalse(iterator.setPosition(10));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorSettingPositionToZeroAfterLastAttribute() {
        Element element = new Element("root");
        NodePointer parentNode = new JDOMNodePointer(element);
        QName qname = new QName("", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, qname);
        List attributes = new ArrayList();
        attributes.add(new Attribute("name1", "value1"));
        attributes.add(new Attribute("name2", "value2"));
        element.setAttributes(attributes);
        assertTrue(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.setPosition(0));
        assertEquals(new JDOMAttributePointer(parentNode, (Attribute) attributes.get(0)), iterator.getNodePointer());
    }
}