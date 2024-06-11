package org.apache.commons.cli.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        List attributes = iterator.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attr = (Attribute) attributes.get(0);
        assertEquals("value1", attr.getValue());

        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasPrevious());
        assertEquals(attr, iterator.previous());

        iterator.setPosition(2);
        assertNull(iterator.previous());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        root.addAttribute(new Attribute("attr2", "value2"));
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        List attributes = iterator.getAttributes();
        assertEquals(2, attributes.size());

        assertTrue(iterator.hasNext());
        Attribute attr1 = (Attribute) iterator.next();
        assertEquals("value1", attr1.getValue());

        assertTrue(iterator.hasNext());
        Attribute attr2 = (Attribute) iterator.next();
        assertEquals("value2", attr2.getValue());

        assertTrue(iterator.hasPrevious());
        assertEquals(attr2, iterator.previous());

        assertTrue(iterator.hasPrevious());
        assertEquals(attr1, iterator.previous());

        iterator.setPosition(3);
        assertNull(iterator.previous());
    }

    @Test
    public void testIteratorWithNamespace() {
        Element root = new Element("root");
        root.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
        root.addAttribute(new Attribute("xml:attr1", "value1"));
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml:attr1"));
        List attributes = iterator.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attr = (Attribute) attributes.get(0);
        assertEquals("value1", attr.getValue());

        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasPrevious());
        assertEquals(attr, iterator.previous());

        iterator.setPosition(2);
        assertNull(iterator.previous());
    }
}