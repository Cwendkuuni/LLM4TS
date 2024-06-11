package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private Element rootElement;
    private NodePointer rootNodePointer;

    @Before
    public void setUp() {
        rootElement = new Element("root");
        rootElement.addAttribute(new Attribute("attr1", "val1", "ns1"));
        rootElement.addAttribute(new Attribute("attr2", "val2", "ns1"));
        rootElement.addAttribute(new Attribute("attr3", "val3", "ns2"));

        rootNodePointer = new NodePointer(rootElement);
    }

    @Test
    public void testEmptyIterator() {
        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, QName.createQName(""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithSingleAttribute() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, QName.createQName("attr1", "ns1"));
        Attribute attribute = iterator.getNodePointer().getNode();
        assertEquals("val1", attribute.getValue());
        assertEquals("ns1", attribute.getNamespace().getNamespaceURI());
        assertEquals("attr1", attribute.getName());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, QName.createQName("*", "ns1"));

        for (int i = 0; i < 3; i++) {
            Attribute attribute = iterator.getNodePointer().getNode();
            if (i == 0) {
                assertEquals("val1", attribute.getValue());
                assertEquals("ns1", attribute.getNamespace().getNamespaceURI());
                assertEquals("attr1", attribute.getName());
            } else if (i == 1) {
                assertEquals("val2", attribute.getValue());
                assertEquals("ns1", attribute.getNamespace().getNamespaceURI());
                assertEquals("attr2", attribute.getName());
            } else {
                assertEquals("val3", attribute.getValue());
                assertEquals("ns2", attribute.getNamespace().getNamespaceURI());
                assertEquals("attr3", attribute.getName());
            }
            iterator.next();
        }

        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorWithNegativePosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, QName.createQName("attr1", "ns1"));
        iterator.setPosition(-1);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testIteratorWithPositionGreaterThanSize() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, QName.createQName("attr1", "ns1"));
        iterator.setPosition(4);
    }

    @Test
    public void testIteratorWithDifferentNamespace() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, QName.createQName("attr3", "ns2"));
        Attribute attribute = iterator.getNodePointer().getNode();
        assertEquals("val3", attribute.getValue());
        assertEquals("ns2", attribute.getNamespace().getNamespaceURI());
        assertEquals("attr3", attribute.getName());
    }
}