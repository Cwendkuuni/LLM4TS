package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;

    @Before
    public void setUp() {
        Element element = new Element("element");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        parent = new JDOMNodePointer(null, element);
        QName attributeName = new QName("", "attr1");
        attributeIterator = new JDOMAttributeIterator(parent, attributeName);
    }

    @Test
    public void testConstructor() {
        assertNull(attributeIterator.getNodePointer());
        assertEquals(0, attributeIterator.getPosition());
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        attributeIterator.setPosition(1);
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(parent, nodePointer.getParent());
        assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
        assertEquals("value1", ((Attribute) nodePointer.getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
        assertFalse(attributeIterator.setPosition(0));
        assertEquals(1, attributeIterator.getPosition());
        assertFalse(attributeIterator.setPosition(3));
        assertEquals(1, attributeIterator.getPosition());
    }
}