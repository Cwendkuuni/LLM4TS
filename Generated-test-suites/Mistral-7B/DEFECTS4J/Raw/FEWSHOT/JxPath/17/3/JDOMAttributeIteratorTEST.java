package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    public void testJDOMAttributeIterator_whenElementHasNoAttributes_thenReturnsEmptyIterator() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(new Element("parent"), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);

        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_whenElementHasOneAttribute_thenIteratorReturnsAttribute() {
        String namespaceUri = "http://example.com";
        Namespace namespace = Namespace.getNamespace("ns", namespaceUri);
        Element element = new Element("root")
                .addAttribute(new Attribute("name", "value", namespace));
        String attributeName = "name";
        QName attributeNameQName = new QName("ns", attributeName);
        NodePointer parent = new NodePointer(element, null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeNameQName);

        assertEquals(1, iterator.getPosition());
        Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
        assertEquals("value", attribute.getValue());
        assertEquals(namespaceUri, attribute.getNamespace().getURI());
        assertEquals(attributeName, attribute.getName());
    }

    @Test
    public void testJDOMAttributeIterator_whenElementHasMultipleAttributes_thenIteratorReturnsAttributes() {
        String namespaceUri = "http://example.com";
        Namespace namespace = Namespace.getNamespace("ns", namespaceUri);
        Element element = new Element("root")
                .addAttribute(new Attribute("name1", "value1", namespace))
                .addAttribute(new Attribute("name2", "value2", namespace));
        String attributeName = "*";
        QName attributeNameQName = new QName(namespaceUri, attributeName);
        NodePointer parent = new NodePointer(element, null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeNameQName);

        assertEquals(2, iterator.getPosition());
        Attribute attribute1 = (Attribute) iterator.getNodePointer().getNode();
        assertEquals("value1", attribute1.getValue());
        assertEquals(namespaceUri, attribute1.getNamespace().getURI());
        assertEquals("name1", attribute1.getName());
        Attribute attribute2 = (Attribute) iterator.getNodePointer().getNode();
        assertEquals("value2", attribute2.getValue());
        assertEquals(namespaceUri, attribute2.getNamespace().getURI());
        assertEquals("name2", attribute2.getName());
    }

    @Test
    public void testJDOMAttributeIterator_whenPositionIsOutOfBounds_thenReturnsNull() {
        String namespaceUri = "http://example.com";
        Namespace namespace = Namespace.getNamespace("ns", namespaceUri);
        Element element = new Element("root")
                .addAttribute(new Attribute("name1", "value1", namespace))
                .addAttribute(new Attribute("name2", "value2", namespace));
        String attributeName = "*";
        QName attributeNameQName = new QName(namespaceUri, attributeName);
        NodePointer parent = new NodePointer(element, null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeNameQName);

        iterator.setPosition(3);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_whenElementHasNoAttributesMatchingPrefix_thenReturnsEmptyIterator() {
        Element element = new Element("root");
        String namespaceUri = "http://example.com";
        Namespace namespace = Namespace.getNamespace("ns", namespaceUri);
        NodePointer parent = new NodePointer(element, null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns", "nonExistentName"));

        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_whenElementHasAttributesWithDifferentNamespaces_thenIteratorReturnsAttributesWithMatchingNamespace() {
        String namespaceUri1 = "http://example1.com";
        Namespace namespace1 = Namespace.getNamespace("ns1", namespaceUri1);
        String namespaceUri2 = "http://example2.com";
        Namespace namespace2 = Namespace.getNamespace("ns2", namespaceUri2);
        Element element = new Element("root")
                .addAttribute(new Attribute("name1", "value1", namespace1))
                .addAttribute(new Attribute("name2", "value2", namespace2));
        String attributeName = "*";
        QName attributeNameQName = new QName(namespaceUri1, attributeName);
        NodePointer parent = new NodePointer(element, null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeNameQName);

        assertEquals(1, iterator.getPosition());
        Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
        assertEquals("value1", attribute.getValue());
        assertEquals(namespaceUri1, attribute.getNamespace().getURI());
        assertEquals("name1", attribute.getName());
    }
}