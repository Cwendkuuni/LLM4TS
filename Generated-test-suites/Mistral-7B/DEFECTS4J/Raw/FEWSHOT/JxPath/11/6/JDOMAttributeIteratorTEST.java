package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testSingleAttributeIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute = iterator.getNodePointer();
        assertSame(attribute, root.getAttribute("attr1"));
        assertEquals("value1", attribute.getValue());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testMultipleAttributesIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute1 = iterator.getNodePointer();
        Attribute attribute2 = iterator.getNodePointer();
        assertSame(attribute1, root.getAttribute("attr1"));
        assertEquals("value1", attribute1.getValue());
        assertSame(attribute2, root.getAttribute("attr2"));
        assertEquals("value2", attribute2.getValue());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testNamespaceIterator() {
        Element root = new Element("root");
        root.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
        root.addAttribute("xml:attr1", "value1");
        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("http://www.w3.org/XML/1998/namespace", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute = iterator.getNodePointer();
        assertSame(attribute, root.getAttribute("xml:attr1"));
        assertEquals("value1", attribute.getValue());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testNextAttribute() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute1 = iterator.getNodePointer();
        Attribute attribute2 = iterator.getNodePointer();
        assertSame(attribute1, root.getAttribute("attr1"));
        assertEquals("value1", attribute1.getValue());
        assertSame(attribute2, root.getAttribute("attr2"));
        assertEquals("value2", attribute2.getValue());
        assertFalse(iterator.hasNext());
        iterator.close();

        iterator = new JDOMAttributeIterator(parent, name);
        assertFalse(iterator.hasNext());
        iterator.close();

        iterator = new JDOMAttributeIterator(parent, name);
        attribute1 = iterator.getNodePointer();
        assertSame(attribute1, root.getAttribute("attr1"));
        assertEquals("value1", attribute1.getValue());
        assertSame(iterator.getNodePointer(), root.getAttribute("attr2"));
        assertEquals("value2", iterator.getNodePointer().getValue());
        assertTrue(iterator.hasNext());
        iterator.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttribute() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.removeAttribute();
    }
}