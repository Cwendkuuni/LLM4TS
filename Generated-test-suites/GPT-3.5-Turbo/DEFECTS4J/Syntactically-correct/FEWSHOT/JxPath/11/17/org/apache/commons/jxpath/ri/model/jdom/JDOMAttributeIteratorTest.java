package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.BeforeClass;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private static Element element;
    private static Namespace namespace;
    private static NodePointer parentPointer;

    @BeforeClass
    public static void setUp() {
        element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        namespace = Namespace.getNamespace("prefix", "http://www.example.com");
        element.setAttribute("prefix:attr3", "value3", namespace);
        parentPointer = new JDOMElementPointer(element);
    }

    @Test
    public void testConstructor() {
        QName qname = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, qname);
        assertEquals(3, iterator.attributes.size());

        qname = new QName("attr2");
        iterator = new JDOMAttributeIterator(parentPointer, qname);
        assertEquals(1, iterator.attributes.size());

        qname = new QName("prefix:attr3", namespace);
        iterator = new JDOMAttributeIterator(parentPointer, qname);
        assertEquals(1, iterator.attributes.size());

        iterator = new JDOMAttributeIterator(parentPointer, null);
        assertTrue(iterator.attributes.isEmpty());
    }

    @Test
    public void testGetNodePointer() {
        QName qname = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, qname);

        NodePointer pointer = iterator.getNodePointer();
        assertTrue(pointer instanceof JDOMAttributePointer);
        Attribute attribute = ((JDOMAttributePointer) pointer).getAttribute();
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());

        pointer = iterator.getNodePointer();
        assertTrue(pointer instanceof JDOMAttributePointer);
        attribute = ((JDOMAttributePointer) pointer).getAttribute();
        assertEquals("attr2", attribute.getName());
        assertEquals("value2", attribute.getValue());

        iterator.setPosition(3);
        pointer = iterator.getNodePointer();
        assertTrue(pointer instanceof JDOMAttributePointer);
        attribute = ((JDOMAttributePointer) pointer).getAttribute();
        assertEquals("prefix:attr3", attribute.getName());
        assertEquals("value3", attribute.getValue());

        iterator.setPosition(4);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        QName qname = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, qname);

        assertEquals(0, iterator.getPosition());

        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());

        iterator.setPosition(10);
        assertEquals(10, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        QName qname = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, qname);

        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());

        assertFalse(iterator.setPosition(4));
        assertEquals(0, iterator.getPosition());

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(3));
        assertEquals(3, iterator.getPosition());
    }
}