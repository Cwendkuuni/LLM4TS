package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Namespace;
import org.jdom.Element;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator jdomAttributeIterator;
    private NodePointer parent;

    @Before
    public void setUp() {
        Element element = new Element("test");
        Attribute attribute1 = new Attribute("attr1", "value1");
        element.setAttribute(attribute1);
        Attribute attribute2 = new Attribute("attr2", "value2");
        element.setAttribute(attribute2);
        parent = new JDOMNodePointer(element, new ArrayList(), 1);
        QName name = new QName("attr1");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(Attribute.class, nodePointer.getBaseValue().getClass());
        Attribute attribute = (Attribute) nodePointer.getBaseValue();
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetPosition() {
        int position = jdomAttributeIterator.getPosition();
        assertEquals(0,position);
    }

    @Test
    public void testSetPosition() {
        boolean result = jdomAttributeIterator.setPosition(1);
        assertTrue(result);
        assertEquals(1, jdomAttributeIterator.getPosition());
    }
}