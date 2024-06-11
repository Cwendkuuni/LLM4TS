package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.BeforeClass;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private static Element element;

    @BeforeClass
    public static void setUpClass() {
        element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new JDOMElementPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertNotNull(pointer);
        Attribute attribute = pointer.getAttribute();
        assertNotNull(attribute);
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new JDOMElementPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new JDOMElementPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }
}