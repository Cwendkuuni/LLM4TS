package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.jxpath.ri.NodePointerFactory;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private Element element;
    private NodePointer parent;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setup() {
        element = new Element("testElement");
        parent = NodePointerFactory.createNodePointer(element);
    }

    @Test
    public void testNullParent() {
        attributeIterator = new JDOMAttributeIterator(null, new QName("test"));
        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testElementWithNoAttributes() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("test"));
        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testElementWithAttribute() {
        element.setAttribute("testAttribute", "testValue");
        attributeIterator = new JDOMAttributeIterator(parent, new QName("testAttribute"));
        assertNotNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testElementWithMultipleAttributes() {
        element.setAttribute("testAttribute1", "testValue1");
        element.setAttribute("testAttribute2", "testValue2");
        attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertNotNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        element.setAttribute("testAttribute1", "testValue1");
        element.setAttribute("testAttribute2", "testValue2");
        attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testSetPositionInvalid() {
        element.setAttribute("testAttribute1", "testValue1");
        element.setAttribute("testAttribute2", "testValue2");
        attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertFalse(attributeIterator.setPosition(0));
    }

    @Test
    public void testSetPosition() {
        element.setAttribute("testAttribute1", "testValue1");
        element.setAttribute("testAttribute2", "testValue2");
        attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertTrue(attributeIterator.setPosition(2));
        assertEquals(2, attributeIterator.getPosition());
    }
}