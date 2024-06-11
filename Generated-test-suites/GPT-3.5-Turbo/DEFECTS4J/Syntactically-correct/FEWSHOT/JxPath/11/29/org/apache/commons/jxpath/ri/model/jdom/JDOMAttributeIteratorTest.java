package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private Element element;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("*"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertEquals(element.getAttribute("attr1"), ((JDOMAttributePointer) nodePointer1).getAttribute());
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertEquals(element.getAttribute("attr2"), ((JDOMAttributePointer) nodePointer2).getAttribute());
    }

    @Test
    public void testGetPosition() {
        iterator.getNodePointer();
        assertEquals(1, iterator.getPosition());
        iterator.getNodePointer();
        assertEquals(2, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        iterator.setPosition(1);
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertEquals(element.getAttribute("attr1"), ((JDOMAttributePointer) nodePointer1).getAttribute());
        iterator.setPosition(2);
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertEquals(element.getAttribute("attr2"), ((JDOMAttributePointer) nodePointer2).getAttribute());
    }
}