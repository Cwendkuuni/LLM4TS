package org.apache.commons.cli.junit; // make sure to use a different package name for testing to avoid potential conflicts

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.apache.commons.cli.JDOMAttributeIterator;

public class JDOMAttributeIteratorTest {
    @Test
    public void testEmptyIterator() {
        NodePointer parent = new NodePointer(new Element("root")); // create a dummy parent element
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleAttributeIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr1"));
        NodePointer attributePointer = iterator.next();
        assertEquals(attributePointer.getNode().getValue(), "value1");
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        NodePointer attribute1Pointer = iterator.next();
        assertEquals(attribute1Pointer.getNode().getValue(), "value1");
        NodePointer attribute2Pointer = iterator.next();
        assertEquals(attribute2Pointer.getNode().getValue(), "value2");
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNamespace() {
        Element root = new Element("root");
        root.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
        root.addAttribute("xml:attr1", "value1");
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "attr1"));
        NodePointer attributePointer = iterator.next();
        assertEquals(attributePointer.getNode().getValue(), "value1");
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithWildcard() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        NodePointer attribute1Pointer = iterator.next();
        assertEquals(attribute1Pointer.getNode().getValue(), "value1");
        NodePointer attribute2Pointer = iterator.next();
        assertEquals(attribute2Pointer.getNode().getValue(), "value2");
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithEmptyList() {
        Element root = new Element("root");
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullParent() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("", "*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullName() {
        NodePointer parent = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
    }
}