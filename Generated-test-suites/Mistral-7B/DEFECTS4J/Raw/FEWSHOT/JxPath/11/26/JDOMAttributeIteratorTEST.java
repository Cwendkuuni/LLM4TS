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
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Test cases for JDOMAttributeIterator class.
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testSingleAttributeIterator() {
        Element element = new Element("root");
        element.addAttribute("attribute1", "value1");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attribute1"));
        assertTrue(iterator.hasNext());
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.next();
        assertEquals("attribute1", attributePointer.getName().getLocalPart());
        assertEquals("value1", attributePointer.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleAttributesIterator() {
        Element element = new Element("root");
        element.addAttribute("attribute1", "value1");
        element.addAttribute("attribute2", "value2");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        List<JDOMAttributePointer> attributes = new ArrayList<>();
        while (iterator.hasNext()) {
            JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.next();
            attributes.add(attributePointer);
        }
        assertEquals(2, attributes.size());
        JDOMAttributePointer attribute1Pointer = attributes.get(0);
        assertEquals("attribute1", attribute1Pointer.getName().getLocalPart());
        assertEquals("value1", attribute1Pointer.getValue());

        JDOMAttributePointer attribute2Pointer = attributes.get(1);
        assertEquals("attribute2", attribute2Pointer.getName().getLocalPart());
        assertEquals("value2", attribute2Pointer.getValue());
    }

    @Test
    public void testNamespaceAttributeIterator() {
        Element element = new Element("root");
        Namespace ns = Namespace.getXmlNamespace();
        element.addNamespaceDeclaration(ns);
        element.addAttribute("xml:attribute1", "value1");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "attribute1"));
        assertTrue(iterator.hasNext());
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.next();
        assertEquals("xml:attribute1", attributePointer.getName().getQualifiedName().toString());
        assertEquals("value1", attributePointer.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testWildcardAttributeIterator() {
        Element element = new Element("root");
        element.addAttribute("attribute1", "value1");
        element.addAttribute("attribute2", "value2");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        List<JDOMAttributePointer> attributes = new ArrayList<>();
        while (iterator.hasNext()) {
            JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.next();
            attributes.add(attributePointer);
        }
        assertEquals(2, attributes.size());
        for (JDOMAttributePointer attributePointer : attributes) {
            if (attributePointer.getName().getLocalPart().equals("attribute1")) {
                assertEquals("attribute1", attributePointer.getName().getLocalPart());
                assertEquals("value1", attributePointer.getValue());
            } else if (attributePointer.getName().getLocalPart().equals("attribute2")) {
                assertEquals("attribute2", attributePointer.getName().getLocalPart());
                assertEquals("value2", attributePointer.getValue());
            }
        }
    }
}