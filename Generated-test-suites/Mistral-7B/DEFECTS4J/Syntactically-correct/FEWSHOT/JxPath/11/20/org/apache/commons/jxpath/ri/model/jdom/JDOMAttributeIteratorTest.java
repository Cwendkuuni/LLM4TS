package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testCreateIteratorWithEmptyElement() {
        Element emptyElement = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(emptyElement), new QName("*"));
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @Test
    public void testCreateIteratorWithSingleAttributeElement() {
        String namespaceUri = "http://example.com/";
        String attributeName = "attribute";
        String attributeValue = "value";
        Element element = new Element("root");
        Attribute attribute = new Attribute(attributeName, attributeValue, new QName(namespaceUri, attributeName));
        element.addContent(attribute);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(namespaceUri, attributeName));
        assertEquals(attribute, iterator.next());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @Test
    public void testCreateIteratorWithMultipleAttributesElement() {
        String namespaceUri = "http://example.com/";
        String attributeName1 = "attribute1";
        String attributeValue1 = "value1";
        String attributeName2 = "attribute2";
        String attributeValue2 = "value2";
        Element element = new Element("root");
        Attribute attribute1 = new Attribute(attributeName1, attributeValue1, new QName(namespaceUri, attributeName1));
        Attribute attribute2 = new Attribute(attributeName2, attributeValue2, new QName(namespaceUri, attributeName2));
        element.addContent(attribute1);
        element.addContent(attribute2);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(namespaceUri, attributeName1));
        assertEquals(attribute1, iterator.next());
        assertEquals(attribute2, iterator.next());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @Test
    public void testCreateIteratorWithEmptyNamespaceElement() {
        String attributeName = "attribute";
        String attributeValue = "value";
        Element element = new Element("root");
        Attribute attribute = new Attribute(attributeName, attributeValue, new QName(""));
        element.addContent(attribute);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", attributeName));
        assertEquals(attribute, iterator.next());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @Test
    public void testCreateIteratorWithWildcardNamespaceElement() {
        String namespaceUri = "http://example.com/";
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("attribute1", "value1", new QName(namespaceUri, "attribute1"));
        Attribute attribute2 = new Attribute("attribute2", "value2", new QName(namespaceUri, "attribute2"));
        attribute1.setNamespace(new Namespace(namespaceUri));
        attribute2.setNamespace(new Namespace(namespaceUri));
        element.addContent(attribute1);
        element.addContent(attribute2);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(namespaceUri, "*"));
        assertEquals(attribute1, iterator.next());
        assertEquals(attribute2, iterator.next());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @Test
    public void testCreateIteratorWithEmptyNamespaceWildcardAttributeElement() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("attribute1", "value1", new QName(""));
        Attribute attribute2 = new Attribute("attribute2", "value2", new QName(""));
        element.addContent(attribute1);
        element.addContent(attribute2);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
        assertEquals(attribute1, iterator.next());
        assertEquals(attribute2, iterator.next());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @Test
    public void testCreateIteratorWithEmptyAttributesElement() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("*"));
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

}