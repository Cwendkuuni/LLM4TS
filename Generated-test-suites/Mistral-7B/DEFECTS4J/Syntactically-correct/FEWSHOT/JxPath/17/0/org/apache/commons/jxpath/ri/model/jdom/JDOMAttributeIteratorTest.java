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

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMNodePointer(null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr1"));
        List attributes = iterator.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attribute = (Attribute) attributes.get(0);
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("attr1", nodePointer.getName());
        assertEquals("value1", nodePointer.getValue());
        assertTrue(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        List attributes = iterator.getAttributes();
        assertEquals(2, attributes.size());
        Attribute attribute1 = (Attribute) attributes.get(0);
        Attribute attribute2 = (Attribute) attributes.get(1);
        assertEquals("attr1", attribute1.getName());
        assertEquals("value1", attribute1.getValue());
        assertEquals("attr2", attribute2.getName());
        assertEquals("value2", attribute2.getValue());
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertNotNull(nodePointer1);
        assertEquals("attr1", nodePointer1.getName());
        assertEquals("value1", nodePointer1.getValue());
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertNotNull(nodePointer2);
        assertEquals("attr2", nodePointer2.getName());
        assertEquals("value2", nodePointer2.getValue());
        assertTrue(iterator.setPosition(3)); // should throw an exception
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithNamespace() {
        Element element = new Element("root");
        element.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
        element.addAttribute("xml:attr1", "value1");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "attr1"));
        List attributes = iterator.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attribute = (Attribute) attributes.get(0);
        assertEquals("xml:attr1", attribute.getQualifiedName().toString());
        assertEquals("value1", attribute.getValue());
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("xml:attr1", nodePointer.getName().getLocalPart());
        assertEquals("value1", nodePointer.getValue());
    }

    @Test
    public void testIteratorWithNullParent() {
        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
    }
}